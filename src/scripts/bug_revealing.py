#!/usr/bin/env python3
"""Compute bug_revealing tests for Defects4J-like projects.

Definition: A generated test is "bug-revealing" if it FAILS on the buggy (b) version
and PASSES on the fixed (f) version.

Usage (examples):
  python3 bug_revealing.py --buggy /path/to/Csv_1_b --fixed /path/to/Csv_1_f --tests /path/to/tests_dir --out summary.csv

Behavior summary:
- Auto-detect tests if --tests omitted: looks for newest tests* dir under buggy project.
- For each test .java: determine full class name from package+basename.
- Optionally copy tests into both projects' src/test/java (use --copy). If not copied, script will compile and run tests from a temporary location.
- Execution uses Maven to run a single test class per run:
    mvn -Dtest=full.test.ClassName test
  This ensures project classpath and surefire behavior.
- A test is considered PASS if Maven returns 0 and surefire reports no failures; FAIL otherwise.
- Output: CSV (default) with per-test b_result/f_result and boolean bug_revealing, and an aggregate summary printed.

Note: This script favors correctness and simplicity using Maven runs per test. It is slower but robust across projects.
"""
# python3 bug_revealing.py --buggy /home/chenlu/ChatUniTest/defect4j_projects/Csv_1_b --fixed /home/chenlu/ChatUniTest/defect4j_projects/Csv_1_f --tests "/home/chenlu/ChatUniTest/defect4j_projects/Csv_1_b/tests%20260116214455/test_cases"

import argparse
import os
import shutil
import subprocess
import sys
import tempfile
import csv
import xml.etree.ElementTree as ET
from datetime import datetime
import urllib.parse
# make sure parent `src` directory is on sys.path so sibling modules can be imported
here_dir = os.path.dirname(os.path.abspath(__file__))
parent_dir = os.path.abspath(os.path.join(here_dir, '..'))
if parent_dir not in sys.path:
    sys.path.insert(0, parent_dir)

# reuse TestRunner compilation / execution helpers
from test_runner import TestRunner
from config import JUNIT_JAR, MOCKITO_JAR, LOG4J_JAR

# 当用户未指定 --tests 路径时，自动从 buggy 项目根目录找最新的 tests* 目录（按修改时间排序）
def find_newest_tests_dir(project_root):
    # find directories named tests* (sorted by mtime descending)
    parent = project_root
    candidates = []
    try:
        for name in os.listdir(parent):
            if name.startswith('tests') and os.path.isdir(os.path.join(parent, name)):
                candidates.append(os.path.join(parent, name))
    except Exception:
        return None
    if not candidates:
        return None
    candidates.sort(key=lambda p: os.path.getmtime(p), reverse=True)
    return candidates[0]

# 递归扫描指定目录下所有测试文件，仅保留 Test.java 结尾的文件。
def discover_test_files(tests_dir):
    tests = []
    for root, _, files in os.walk(tests_dir):
        for f in files:
            if f.endswith('Test.java'):
                tests.append(os.path.join(root, f))
    return sorted(tests)

# 解析 Java 文件的 package 声明 + 文件名，生成全限定类名
def get_full_class_name(java_file):
    # parse package declaration + class name
    pkg = ''
    try:
        with open(java_file, 'r', errors='ignore') as fh:
            for line in fh:
                line = line.strip()
                if line.startswith('package '):
                    pkg = line.replace('package', '').replace(';', '').strip()
                    break
    except Exception:
        pass
    base = os.path.splitext(os.path.basename(java_file))[0]
    if pkg:
        return f"{pkg}.{base}"
    return base


# 执行整个测试类，返回类级执行状态（pass/fail/timeout）及原始输出
def java_run_test(project_dir, full_class_name, class_path, timeout=120):
    """
    用 java + ConsoleLauncher 运行单个测试类，解析输出判断 pass/fail。
    """
    cmd = [
        'java',
        '-cp', class_path,
        'org.junit.platform.console.ConsoleLauncher',
        '--disable-banner',
        '--disable-ansi-colors',
        '--details=summary',
        '--select-class', full_class_name
    ]
    try:
        proc = subprocess.run(cmd, cwd=project_dir, stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=timeout, text=True)
    except subprocess.TimeoutExpired:
        return {'status': 'timeout', 'returncode': None, 'stdout': '', 'stderr': ''}
    rc = proc.returncode
    # 解析 ConsoleLauncher 输出
    out = proc.stdout + proc.stderr
    # JUnit5 ConsoleLauncher: 0=全部通过，1=有失败
    if rc == 0:
        status = 'pass'
    else:
        status = 'fail'
    # 可进一步解析 out 统计失败数等
    return {'status': status, 'returncode': rc, 'stdout': proc.stdout, 'stderr': proc.stderr}

# 单个 @Test 方法独立执行
def java_run_test_method(project_dir, full_class_name, method_name, class_path, timeout=120):
    """Run a specific test method using ConsoleLauncher --select-method.
    Returns similar dict as java_run_test().
    """
    selector = f"{full_class_name}#{method_name}"
    cmd = [
        'java',
        '-cp', class_path,
        'org.junit.platform.console.ConsoleLauncher',
        '--disable-banner',
        '--disable-ansi-colors',
        '--details=summary',
        '--select-method', selector
    ]
    try:
        proc = subprocess.run(cmd, cwd=project_dir, stdout=subprocess.PIPE, stderr=subprocess.PIPE, timeout=timeout, text=True)
    except subprocess.TimeoutExpired:
        return {'status': 'timeout', 'returncode': None, 'stdout': '', 'stderr': ''}
    rc = proc.returncode
    out = proc.stdout + proc.stderr
    if rc == 0:
        status = 'pass'
    else:
        status = 'fail'
    return {'status': status, 'returncode': rc, 'stdout': proc.stdout, 'stderr': proc.stderr}

# 提取测试类中的所有 @Test 方法
def discover_test_methods(java_file):
    """Discover method names annotated with @Test in a Java test file.
    Returns a list of method names in order of appearance.
    Supports JUnit4 and JUnit5 style @Test annotations.
    """
    methods = []
    try:
        with open(java_file, 'r', errors='ignore') as fh:
            lines = fh.readlines()
    except Exception:
        return methods

    i = 0
    n = len(lines)
    while i < n:
        line = lines[i].strip()
        if line.startswith('@Test') or line.startswith('@org.junit.Test') or line.startswith('@org.junit.jupiter.api.Test'):
            # scan forward to find the method declaration
            j = i + 1
            while j < n:
                decl = lines[j].strip()
                # skip annotations and empty lines
                if decl.startswith('@') or decl == '':
                    j += 1
                    continue
                # 正则匹配方法名：匹配 public void testXxx( 或 void testXxx(
                import re
                m = re.search(r'([a-zA-Z_][a-zA-Z0-9_]*)\s*\(', decl)
                if m:
                    name = m.group(1)
                    methods.append(name)
                    break
                else:
                    j += 1
            i = j
        else:
            i += 1
    return methods

# 复制测试文件到项目目录下的 src/test/java，保持包路径
def copy_test_to_project(java_file, project_root, tests_subpath='src/test/java'):
    # 保留包路径结构，将测试文件复制到项目的 src/test/java 目录
    pkg = ''
    try:
        with open(java_file, 'r', errors='ignore') as fh:
            for line in fh:
                line = line.strip()
                if line.startswith('package '):
                    pkg = line.replace('package', '').replace(';', '').strip()
                    break
    except Exception:
        pass
    # 包名转路径：如 org.apache.csv → org/apache/csv
    rel_dir = pkg.replace('.', os.sep) if pkg else ''
    # 目标目录：项目根目录/src/test/java/包路径
    dest_dir = os.path.join(project_root, tests_subpath, rel_dir)
    os.makedirs(dest_dir, exist_ok=True)
    # 目标文件路径：目标目录/文件名
    dest = os.path.join(dest_dir, os.path.basename(java_file))
    shutil.copy2(java_file, dest)
    return dest


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--buggy', required=True, help='Path to buggy (b) project, e.g. /path/to/Csv_1_b')
    parser.add_argument('--fixed', required=True, help='Path to fixed (f) project, e.g. /path/to/Csv_1_f')
    parser.add_argument('--tests', required=False, help='Path to generated tests directory (folder containing Test.java files). If omitted tries to find newest tests* under buggy project.')
    parser.add_argument('--copy', action='store_true', help='Copy tests into both projects under src/test/java before running')
    parser.add_argument('--out', default='bug_revealing_summary.csv', help='CSV output path')
    parser.add_argument('--timeout', type=int, default=120, help='Timeout seconds per test run (mvn)')
    parser.add_argument('--fail-if-no-tests', action='store_true', help='Exit non-zero if no tests found')
    parser.add_argument('--force-clean', action='store_true', help='Force clean existing target directories or copied tests (optional)')
    args = parser.parse_args()

    buggy = os.path.abspath(args.buggy)
    fixed = os.path.abspath(args.fixed)

    tests_dir = args.tests
    if not tests_dir:
        tests_dir = find_newest_tests_dir(buggy)
        if not tests_dir:
            print('No tests dir found under buggy project and --tests not specified.')
            if args.fail_if_no_tests:
                sys.exit(2)
            else:
                # continue but no tests
                tests = []
        else:
            print(f'Using tests dir: {tests_dir}')
    else:
        # accept either literal path or URL-encoded path (e.g., tests%20...)
        raw = tests_dir
        # prefer the path as given if it exists
        if os.path.exists(raw):
            tests_dir = os.path.abspath(raw)
        else:
            # try URL-unquoting (convert %20 -> space) and use if exists
            un = urllib.parse.unquote(raw)
            if os.path.exists(un):
                tests_dir = os.path.abspath(un)
            else:
                # fallback to original absolute path (may be non-existing)
                tests_dir = os.path.abspath(raw)
                print(f'Warning: provided --tests path not found as given or unquoted: {raw}')

    # if tests dir contains a test_cases subdirectory, prefer that
    if tests_dir and os.path.isdir(os.path.join(tests_dir, 'test_cases')):
        tests_dir = os.path.join(tests_dir, 'test_cases')

    # determine top-level tests directory (the tests* directory)
    top_tests_dir = None
    if tests_dir:
        if os.path.basename(tests_dir) == 'test_cases':
            top_tests_dir = os.path.dirname(tests_dir)
        elif os.path.basename(tests_dir).startswith('tests'):
            top_tests_dir = tests_dir
        else:
            top_tests_dir = os.path.dirname(tests_dir)

    # If user didn't set an explicit --out (left default), place output CSV into
    # the containing tests* directory (not the nested test_cases). This keeps
    # results next to generated tests.
    default_out = 'bug_revealing_summary.csv'
    try:
        # project prefix from buggy project directory name (strip trailing _b/_f)
        proj_basename = os.path.basename(buggy).lower()
        if proj_basename.endswith('_b') or proj_basename.endswith('_f'):
            proj_prefix = proj_basename[:-2]
        else:
            proj_prefix = proj_basename

        # ensure output filename is prefixed with project name
        if args.out == default_out and tests_dir:
            if top_tests_dir:
                args.out = os.path.join(top_tests_dir, f'{proj_prefix}_bugrevealing.csv')
        else:
            # user provided an output name/path: prefix the basename if not already
            out_dir = os.path.dirname(os.path.abspath(args.out)) or os.getcwd()
            out_base = os.path.basename(args.out)
            if not out_base.startswith(proj_prefix + '_'):
                out_base = f'{proj_prefix}_{out_base}'
            args.out = os.path.join(out_dir, out_base)
    except Exception:
        pass

    if tests_dir and os.path.isdir(tests_dir):
        test_files = discover_test_files(tests_dir)
    else:
        test_files = []

    if not test_files:
        print('No Test.java files found. Exiting.' )
        if args.fail_if_no_tests:
            sys.exit(2)
        else:
            # write empty CSV
            with open(args.out, 'w', newline='') as csvfile:
                writer = csv.writer(csvfile)
                writer.writerow(['test_class','buggy_status','fixed_status','bug_revealing','buggy_rc','fixed_rc','notes'])
            print(f'Wrote empty summary to {args.out}')
            return

    # prepare output
    out_rows = []
    total_files = len(test_files)
    total_methods = 0
    bug_revealing_count = 0

    # prepare detailed log file
    log_path = os.path.splitext(os.path.abspath(args.out))[0] + '.details.txt'
    try:
        logfh = open(log_path, 'w', encoding='utf-8')
        logfh.write(f'bug_revealing log started: {datetime.now().isoformat()}\n')
        logfh.write(f'buggy={buggy}\nfixed={fixed}\ntests_dir={tests_dir}\n\n')
    except Exception as e:
        print(f'Warning: cannot open detail log {log_path}: {e}')
        logfh = None

    def write_log_entry(fh, project_label, full_class, method_name, res):
        if not fh:
            return
        fh.write('---\n')
        fh.write(f'[{datetime.now().isoformat()}] {project_label} {full_class}')
        if method_name:
            fh.write(f'#{method_name}')
        fh.write(f' status={res.get("status")} returncode={res.get("returncode")}\n')
        out = res.get('stdout') or ''
        err = res.get('stderr') or ''
        if out:
            fh.write('STDOUT:\n')
            fh.write(out)
            if not out.endswith('\n'):
                fh.write('\n')
        if err:
            fh.write('STDERR:\n')
            fh.write(err)
            if not err.endswith('\n'):
                fh.write('\n')
        fh.write('\n')

    # detect common discovery/classpath errors that are noisy and should be
    # treated as 'discovery_failed' (do not dump full stack)
    def is_discovery_error(res):
        if not res:
            return False
        text = ''
        try:
            text = (res.get('stderr') or '') + '\n' + (res.get('stdout') or '')
        except Exception:
            return False
        t = text.lower()
        if 'failed to discover tests' in t:
            return True
        # if "could not load class with name" in t:
        #     return True
        # if 'classnotfoundexception' in t:
        #     return True
        # # JUnit engine load failures
        # if 'testengine' in t and 'failed' in t and 'discover' in t:
        #     return True
        return False

    def write_short_log(fh, project_label, full_class, method_name, res):
        if not fh:
            return
        fh.write('---\n')
        fh.write(f'[{datetime.now().isoformat()}] {project_label} {full_class}')
        if method_name:
            fh.write(f'#{method_name}')
        fh.write(' discovery_failed\n')

    # helper: print to stdout and also write to the detailed log file (if open)
    def echo_and_log(msg):
        try:
            print(msg)
        except Exception:
            pass
        if logfh:
            try:
                logfh.write(msg)
                if not msg.endswith('\n'):
                    logfh.write('\n')
                logfh.flush()
            except Exception:
                # ignore logging errors to avoid interrupting main flow
                pass

    # optionally copy tests into projects
    if args.copy:
        # Ignore copying: tests .java remain in tests*/test_cases as requested.
        echo_and_log('Note: --copy requested but ignored. Using tests from tests*/test_cases and compiling into tests_ChatGPT.')


    print('Note: TestRunner initialization skipped; using precompiled classes in tests_ChatGPT if available.')
    if logfh:
        try:
            logfh.write('Note: TestRunner initialization skipped; using precompiled classes in tests_ChatGPT if available.\n')
        except Exception:
            pass

    # 编译和运行：复用 TestRunner 的 compile/run 实现以兼容多种 tests 目录结构
    def compile_tests_with_runner(runner, proj_root, src_root=None):
        # Compilation is intentionally disabled. Precompiled .class files in
        # tests_ChatGPT will be used instead. This function is a no-op.
        print(f'Skipping compilation for project {proj_root}; using tests_ChatGPT if present.')
        if logfh:
            try:
                logfh.write(f'[{datetime.now().isoformat()}] Skipping compilation for project {proj_root}; using tests_ChatGPT if present.\n')
            except Exception:
                pass
        return

    # Compilation step skipped: rely on existing .class files in tests_ChatGPT
    print('Skipping compilation step; using compiled classes in tests_ChatGPT (if present).')
    if logfh:
        try:
            logfh.write(f'[{datetime.now().isoformat()}] Skipping compilation step; using compiled classes in tests_ChatGPT (if present).\n')
        except Exception:
            pass

    # Recompute classpaths now that compiled tests may exist under tests_ChatGPT
    def build_classpath(project_root):
        m2 = os.path.expanduser('~/.m2/repository')
        def jar(*path):
            return os.path.join(m2, *path)

        cp = [
            # project production classes
            os.path.join(project_root, 'target', 'classes'),

            # place for compiled generated tests (tests_ChatGPT) will be inserted below

            # JUnit 4
            jar('junit', 'junit', '4.13.2', 'junit-4.13.2.jar'),
            jar('org', 'hamcrest', 'hamcrest-core', '1.3', 'hamcrest-core-1.3.jar'),

            # commons-io (commons-csv tests often need this)
            jar('commons-io', 'commons-io', '2.11.0', 'commons-io-2.11.0.jar'),

            # JUnit 5 (Jupiter)
            jar('org', 'junit', 'jupiter', 'junit-jupiter-api', '5.9.2',
                'junit-jupiter-api-5.9.2.jar'),
            jar('org', 'junit', 'jupiter', 'junit-jupiter-engine', '5.9.2',
                'junit-jupiter-engine-5.9.2.jar'),
            jar('org', 'junit', 'jupiter', 'junit-jupiter-params', '5.9.2',
                'junit-jupiter-params-5.9.2.jar'),

            # JUnit Platform
            jar('org', 'junit', 'platform', 'junit-platform-commons', '1.9.2',
                'junit-platform-commons-1.9.2.jar'),
            jar('org', 'junit', 'platform', 'junit-platform-engine', '1.9.2',
                'junit-platform-engine-1.9.2.jar'),

            # Vintage (run JUnit4 on JUnit5 platform)
            jar('org', 'junit', 'vintage', 'junit-vintage-engine', '5.9.2',
                'junit-vintage-engine-5.9.2.jar'),

            # Mockito + ByteBuddy
            jar('org', 'mockito', 'mockito-core', '3.12.4', 'mockito-core-3.12.4.jar'),
            jar('org', 'mockito', 'mockito-junit-jupiter', '3.12.4', 'mockito-junit-jupiter-3.12.4.jar'),
            jar('net', 'bytebuddy', 'byte-buddy', '1.14.6',
                'byte-buddy-1.14.6.jar'),
            jar('net', 'bytebuddy', 'byte-buddy-agent', '1.14.6',
                'byte-buddy-agent-1.14.6.jar'),
            jar('org', 'objenesis', 'objenesis', '3.3',
                'objenesis-3.3.jar'),

            # JUnit ConsoleLauncher（等价于 $JUNIT_CONSOLE_JAR）
            jar('org', 'junit', 'platform',
                'junit-platform-console-standalone', '1.9.2',
                'junit-platform-console-standalone-1.9.2.jar'),
        ]
        # include compiled tests placed under the top-level tests dir (tests_ChatGPT)
        try:
            if top_tests_dir:
                alt = os.path.join(top_tests_dir, 'tests_ChatGPT')
                if os.path.isdir(alt):
                    # put compiled tests early in classpath
                    cp.insert(1, alt)
        except Exception:
            pass

        cp = [p for p in cp if os.path.exists(p)]
        return os.pathsep.join(cp)

    buggy_cp = build_classpath(buggy)
    fixed_cp = build_classpath(fixed)
    
    # echo_and_log already defined earlier near log setup

    # project name fields
    proj_name = os.path.basename(buggy)
    try:
        proj_prefix = proj_prefix
    except NameError:
        proj_prefix = os.path.splitext(proj_name)[0]

    for jf in test_files:
        full = get_full_class_name(jf)
        methods = discover_test_methods(jf)
        class_has_revealing = False
        if not methods:
            # fallback: run the whole class as before
            echo_and_log(f'Running test class {full} on buggy...')
            res_b = java_run_test(buggy, full, buggy_cp, timeout=args.timeout)
            echo_and_log(f'  -> {res_b["status"]}')
            echo_and_log(f'Running test class {full} on fixed...')
            res_f = java_run_test(fixed, full, fixed_cp, timeout=args.timeout)

            bstat = res_b.get('status')
            fstat = res_f.get('status')
            b_rc = res_b.get('returncode')
            f_rc = res_f.get('returncode')
            notes = ''
            if bstat == 'timeout' or fstat == 'timeout':
                notes = 'timeout'
            is_bug_revealing = (bstat == 'fail') and (fstat == 'pass')
            if is_bug_revealing:
                bug_revealing_count += 1
                class_has_revealing = True
            total_methods += 1
            out_rows.append([proj_name, full, '', bstat, fstat, 'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])
            # detect and record discovery errors (always set notes regardless of logfh)
            if bstat != 'pass':
                if is_discovery_error(res_b):
                    notes = 'discovery_failed' if not notes else notes + ';discovery_failed'
                    if logfh:
                        write_short_log(logfh, 'buggy', full, '', res_b)
                else:
                    if logfh:
                        write_log_entry(logfh, 'buggy', full, '', res_b)
            if fstat != 'pass':
                if is_discovery_error(res_f):
                    notes = 'discovery_failed' if not notes else notes + ';discovery_failed'
                    if logfh:
                        write_short_log(logfh, 'fixed', full, '', res_f)
                else:
                    if logfh:
                        write_log_entry(logfh, 'fixed', full, '', res_f)
            # ensure notes are reflected in CSV row
            try:
                out_rows[-1][8] = notes
            except Exception:
                pass
        else:
            for m in methods:
                echo_and_log(f'Running {full}#{m} on buggy...')
                res_b = java_run_test_method(buggy, full, m, buggy_cp, timeout=args.timeout)
                echo_and_log(f'  -> {res_b["status"]}')
                echo_and_log(f'Running {full}#{m} on fixed...')
                res_f = java_run_test_method(fixed, full, m, fixed_cp, timeout=args.timeout)
                echo_and_log(f'  -> {res_f["status"]}')

                bstat = res_b.get('status')
                fstat = res_f.get('status')
                b_rc = res_b.get('returncode')
                f_rc = res_f.get('returncode')
                notes = ''
                if bstat == 'timeout' or fstat == 'timeout':
                    notes = 'timeout'
                is_bug_revealing = (bstat == 'fail') and (fstat == 'pass')
                if is_bug_revealing:
                    bug_revealing_count += 1
                    class_has_revealing = True
                total_methods += 1
                out_rows.append([proj_prefix, full, m, bstat, fstat, 'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])
                # detect and record discovery errors per-method (set notes even if no logfh)
                if bstat != 'pass':
                    if is_discovery_error(res_b):
                        notes = 'discovery_failed' if not notes else notes + ';discovery_failed'
                        if logfh:
                            write_short_log(logfh, 'buggy', full, m, res_b)
                    else:
                        if logfh:
                            write_log_entry(logfh, 'buggy', full, m, res_b)
                if fstat != 'pass':
                    if is_discovery_error(res_f):
                        notes = 'discovery_failed' if not notes else notes + ';discovery_failed'
                        if logfh:
                            write_short_log(logfh, 'fixed', full, m, res_f)
                    else:
                        if logfh:
                            write_log_entry(logfh, 'fixed', full, m, res_f)
                # ensure notes are reflected in CSV row
                try:
                    out_rows[-1][8] = notes
                except Exception:
                    pass


        

    # write CSV (per-method rows; test_method can be empty for class-run fallback)
    with open(args.out, 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['project_name','test_class','test_method','buggy_status','fixed_status','bug_revealing','buggy_rc','fixed_rc','notes'])
        for r in out_rows:
            writer.writerow(r)

    # write per-project counts into a shared CSV located in the common
    # parent directory of the projects (append mode so multiple runs add rows)
    try:
        try:
            tm = total_methods
        except NameError:
            tm = total_files
        # parent directory that contains all project folders (e.g. defect4j_projects)
        shared_dir = os.path.dirname(buggy)
        counts_path = os.path.join(shared_dir, 'bugrevealing_counts.csv')
        write_header = not os.path.exists(counts_path)
        with open(counts_path, 'a', newline='') as cf:
            cwriter = csv.writer(cf)
            if write_header:
                cwriter.writerow(['project','testcount','revealingcount'])
            cwriter.writerow([proj_prefix, tm, bug_revealing_count])
        if logfh:
            try:
                logfh.write(f'Appended per-project counts to: {os.path.abspath(counts_path)}\n')
            except Exception:
                pass
    except Exception:
        pass

    # summary
    echo_and_log('\n===== SUMMARY =====')
    # if methods were discovered, report methods count, otherwise files
    try:
        tm = total_methods
    except NameError:
        tm = total_files
    echo_and_log(f'Total tests evaluated: {tm}')
    echo_and_log(f'Bug revealing tests: {bug_revealing_count}')
    echo_and_log(f'Summary CSV: {os.path.abspath(args.out)}')

    # also write summary into the detailed log file (if open)
    if logfh:
        try:
            logfh.write('\n===== SUMMARY =====\n')
            logfh.write(f'Total tests evaluated: {tm}\n')
            logfh.write(f'Bug revealing tests: {bug_revealing_count}\n')
            logfh.write(f'Summary CSV: {os.path.abspath(args.out)}\n')
            logfh.write(f'Per-project counts CSV: {os.path.abspath(counts_path)}\n')
            logfh.write(f'\nlog finished: {datetime.now().isoformat()}\n')
            logfh.close()
            print(f'Detailed log: {os.path.abspath(log_path)}')
        except Exception:
            pass

    # done

if __name__ == '__main__':
    main()