import glob
import os
import subprocess
import re
import shutil
import tempfile
import csv
from datetime import datetime
from config import *


class TestRunner:

    def __init__(self, test_path, target_path, tool="jacoco"):
        """
        :param tool: coverage tool (Only support cobertura or jacoco)
        :param test_path: test cases directory path e.g.:
        /data/share/TestGPT_ASE/result/scope_test%20230414210243%d3_1/ (all test)
        /data/share/TestGPT_ASE/result/scope_test%20230414210243%d3_1/1460%lang_1_f%ToStringBuilder%append%d3/5 (single test)
        :param target_path: target project path
        """
        self.coverage_tool = tool
        self.test_path = test_path
        self.target_path = target_path

        # Preprocess
        self.dependencies = self.make_dependency()
        self.build_dir_name = "target/classes"
        self.build_dir = self.process_single_repo()

        self.COMPILE_ERROR = 0
        self.TEST_RUN_ERROR = 0
        self.SYNTAX_TOTAL = 0
        self.SYNTAX_ERROR = 0
        # 可选：指定当前运行时的 jacoco exec 输出路径（用于单个用例覆盖）
        self.jacoco_destfile = None

    def start_single_test(self):
        """
        Run a single method test case with a thread.
        """
        temp_dir = os.path.join(self.test_path, "temp")
        compiled_test_dir = os.path.join(self.test_path, "runtemp")
        os.makedirs(compiled_test_dir, exist_ok=True)
        try:
            self.instrument(compiled_test_dir, compiled_test_dir)
            test_file = os.path.abspath(glob.glob(temp_dir + '/*.java')[0])
            compiler_output = os.path.join(temp_dir, 'compile_error')
            test_output = os.path.join(temp_dir, 'runtime_error')
            if not self.run_single_test(test_file, compiled_test_dir, compiler_output, test_output):
                return False
            else:
                self.report(compiled_test_dir, temp_dir)
        except Exception as e:
            print(e)
            return False
        return True

    def start_all_test(self):
        """
        Initialize configurations and run all tests
        """
        if os.path.isdir(self.test_path) and os.path.isdir(os.path.join(self.test_path, 'test_cases')):
            tests_dir = self.test_path
            compiler_output_dir = os.path.join(tests_dir, "compiler_output")
            test_output_dir = os.path.join(tests_dir, "test_output")
            report_dir = os.path.join(tests_dir, "report")

            compiler_output = os.path.join(compiler_output_dir, "CompilerOutput")
            test_output = os.path.join(test_output_dir, "TestOutput")
            compiled_test_dir = os.path.join(tests_dir, "tests_ChatGPT")

            logs_dir = os.path.join(tests_dir, "logs")
            os.makedirs(logs_dir, exist_ok=True)
            logs = self._make_logs(logs_dir)

            return self.run_all_tests(tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs)

        date = datetime.now().strftime("%Y%m%d%H%M%S")
        tests_dir = os.path.join(self.target_path, f"tests%{date}")
        compiler_output_dir = os.path.join(tests_dir, "compiler_output")
        test_output_dir = os.path.join(tests_dir, "test_output")
        report_dir = os.path.join(tests_dir, "report")

        compiler_output = os.path.join(compiler_output_dir, "CompilerOutput")
        test_output = os.path.join(test_output_dir, "TestOutput")
        compiled_test_dir = os.path.join(tests_dir, "tests_ChatGPT")

        self.copy_tests(tests_dir)

        logs_dir = os.path.join(tests_dir, "logs")
        os.makedirs(logs_dir, exist_ok=True)
        logs = self._make_logs(logs_dir)

        return self.run_all_tests(tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs)

    @staticmethod
    def _make_logs(logs_dir):
        """创建并初始化 logs 字典"""
        paths = {
            "syntax":          os.path.join(logs_dir, "syntax.log"),
            "compile":         os.path.join(logs_dir, "compile.log"),
            "exec":            os.path.join(logs_dir, "test_exec.log"),
            "coverage":        os.path.join(logs_dir, "coverage.log"),
            "execution_stats": os.path.join(logs_dir, "execution_stats.log"),
            "compile_failed":  os.path.join(logs_dir, "compile_failed.txt"),
        }
        for p in paths.values():
            open(p, 'w').close()
        return paths

    # ------------------------------------------------------------------
    # 辅助：从 test_cases 目录文件名推断 target_class（modified_class）
    # 优先级: modified_classes.src > defects4j.build.properties > 文件名前缀
    # ------------------------------------------------------------------
    def _resolve_target_class(self, tests_dir):
        """返回 target_class 简单类名（如 CSVRecord），失败返回空字符串"""
        project_root = self.target_path

        # 优先1：modified_classes.src（Defects4J 元数据）
        meta_file = os.path.join(project_root, 'modified_classes.src')
        if os.path.exists(meta_file):
            try:
                with open(meta_file) as f:
                    line = f.readline().strip()
                if line:
                    return line.split('.')[-1]
            except Exception:
                pass

        # 优先2：defects4j.build.properties
        prop_file = os.path.join(project_root, 'defects4j.build.properties')
        if os.path.exists(prop_file):
            try:
                with open(prop_file) as f:
                    for l in f:
                        if 'd4j.classes.modified' in l and '=' in l:
                            val = l.split('=', 1)[1].strip()
                            first_class = val.split(',')[0].strip()
                            if first_class:
                                return first_class.split('.')[-1]
            except Exception:
                pass

        # 优先3：从 test_cases 文件名前缀推断
        tc_dir = os.path.join(tests_dir, 'test_cases')
        if os.path.isdir(tc_dir):
            for fname in os.listdir(tc_dir):
                if fname.endswith('Test.java'):
                    # 匹配 ClassName_数字_数字Test.java，兼容类名含下划线
                    m = re.match(r'^(.+?)_\d+_\d+Test\.java$', fname)
                    if m:
                        return m.group(1)
                    # 兜底：取第一个下划线前的部分
                    return fname.split('_')[0]

        return ''

    def run_all_tests(self, tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs=None):
        """
        Run all test cases in a project.
        """
        tests = os.path.join(tests_dir, "test_cases")
        self.instrument(compiled_test_dir, compiled_test_dir)
        start_time = datetime.now()

        total_compile = 0
        total_test_run = 0
        total_tests = 0
        syntax_errors = 0
        compile_failed_list = []

        # ── 推断 target_class ──────────────────────────────────────────
        target_class = self._resolve_target_class(tests_dir)
        project_name = os.path.basename(self.target_path.rstrip('/'))
        global_csv_parent_dir = os.path.abspath(os.path.join(self.target_path, ".."))
        os.makedirs(global_csv_parent_dir, exist_ok=True)

        # ── per_test_status 内存字典（key=full_class_name）─────────────
        # 结构: { full_class_name: {compile_status, exec_status, exec_timeout,
        #                           jacoco_exec_size, compile_score, exec_score} }
        per_test_status_map = {}

        per_test_records = []

        for t in range(1, 1 + test_number):
            print("Processing attempt: ", str(t))
            for test_case_file in os.listdir(tests):
                if str(t) != test_case_file.split('_')[-1].replace('Test.java', ''):
                    continue

                total_compile += 1
                total_tests += 1
                test_file = os.path.join(tests, test_case_file)
                full_name = self.get_full_name(test_file)

                # ── 1) Syntax check ───────────────────────────────────
                syntax_tmp = tempfile.mkdtemp()
                try:
                    syntax_cmd = self.javac_cmd(syntax_tmp, test_file)
                    syntax_cmd.insert(1, '-Xlint:all')
                    proc = subprocess.run(syntax_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                    stderr = proc.stderr or ""
                    self.SYNTAX_TOTAL += 1

                    syntax_pattern = re.compile(
                        r"(illegal start of expression|';' expected|unclosed string literal|"
                        r"unterminated string literal|unclosed comment|illegal character|"
                        r"identifier expected|expected '\}'|expected '\)'|expected '\]'|"
                        r"missing ';'|syntax error)",
                        re.IGNORECASE
                    )
                    if proc.returncode != 0:
                        if syntax_pattern.search(stderr):
                            self.SYNTAX_ERROR += 1
                            syntax_errors += 1
                            if logs:
                                with open(logs['syntax'], 'a') as f:
                                    f.write(f"[SYNTAX_ERROR] {test_case_file}: {stderr.splitlines()[0] if stderr else 'syntax error'}\n")
                        else:
                            if logs:
                                with open(logs['syntax'], 'a') as f:
                                    f.write(f"[SYNTAX_SEMANTIC] {test_case_file}: {stderr.splitlines()[0] if stderr else 'compile error'}\n")
                    else:
                        if logs:
                            with open(logs['syntax'], 'a') as f:
                                f.write(f"[SYNTAX_OK] {test_case_file}\n")
                finally:
                    if os.path.exists(syntax_tmp):
                        shutil.rmtree(syntax_tmp)

                # ── 2) Compile ────────────────────────────────────────
                os.makedirs(compiled_test_dir, exist_ok=True)
                cmd = self.javac_cmd(compiled_test_dir, test_file)
                result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                compiled_ok = (result.returncode == 0)

                if not compiled_ok:
                    self.COMPILE_ERROR += 1
                    compile_failed_list.append(test_case_file)

                    if logs:
                        with open(logs['compile'], 'a') as f:
                            f.write(f"[COMPILE_FAILED] {full_name}: {result.stderr.splitlines()[0] if result.stderr else 'compile error'}\n")
                        with open(logs['compile_failed'], 'a') as f:
                            f.write(f"{full_name}\t{test_case_file}\n")

                    # 写详细编译错误文件
                    if os.path.basename(compiler_output) == 'compile_error':
                        compiler_output_file = f"{compiler_output}.txt"
                    else:
                        compiler_output_file = f"{compiler_output}-{os.path.basename(test_file)}.txt"
                    with open(compiler_output_file, "w") as f:
                        f.write(result.stdout)
                        f.write(result.stderr)

                    # ★ 记录编译失败状态
                    per_test_status_map[full_name] = {
                        'compile_status':  'fail',
                        'exec_status':     'skip',
                        'exec_timeout':    False,
                        'jacoco_exec_size': 0,
                        'compile_score':   0.0,
                        'exec_score':      0.0,
                    }
                    continue  # 编译失败跳过执行

                else:
                    if logs:
                        with open(logs['compile'], 'a') as f:
                            f.write(f"[COMPILE_OK] {full_name}\n")

                    # ★ 初始化编译成功状态（exec 待填）
                    per_test_status_map[full_name] = {
                        'compile_status':  'pass',
                        'exec_status':     'pending',
                        'exec_timeout':    False,
                        'jacoco_exec_size': 0,
                        'compile_score':   1.0,
                        'exec_score':      0.0,   # 执行结果出来后更新
                    }

                # ── 3) Run test ───────────────────────────────────────
                test_basename = os.path.splitext(test_case_file)[0]
                per_test_exec = os.path.join(compiled_test_dir, f"jacoco_{test_basename}.exec")
                try:
                    if os.path.exists(per_test_exec):
                        os.remove(per_test_exec)
                except Exception:
                    pass

                self.jacoco_destfile = per_test_exec
                exec_ok, is_timeout = self.run_test_only_with_reason(test_file, compiled_test_dir, test_output, logs)
                self.jacoco_destfile = None

                # ★ 获取 jacoco exec 文件大小
                try:
                    exec_size = os.path.getsize(per_test_exec) if os.path.exists(per_test_exec) else 0
                except Exception:
                    exec_size = 0

                # ★ 更新执行状态
                per_test_status_map[full_name]['exec_status']     = 'pass' if exec_ok else 'fail'
                per_test_status_map[full_name]['exec_timeout']    = is_timeout
                per_test_status_map[full_name]['jacoco_exec_size'] = exec_size
                per_test_status_map[full_name]['exec_score']      = 1.0 if exec_ok else 0.0

                # ── 4) 调试信息 ───────────────────────────────────────
                if logs:
                    try:
                        with open(logs['execution_stats'], 'a') as f:
                            f.write(f"[PER_TEST_RUN] {test_case_file} exec_ok={exec_ok} exec_size={exec_size}\n")
                    except Exception:
                        pass

                # ── 5) 单测覆盖率（针对 modified_class）─────────────────
                if exec_size > 0:
                    per_test_report_dir = os.path.join(report_dir, "per_test_reports", test_basename)
                    self.report(compiled_test_dir, per_test_report_dir, jacoco_exec_override=per_test_exec)

                    # 只提取 modified_class 对应节点的行/分支数据
                    m_per_line_cov = m_per_line_total = None
                    m_per_branch_cov = m_per_branch_total = None
                    try:
                        jacoco_xml_path = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
                        if os.path.exists(jacoco_xml_path) and target_class:
                            import xml.etree.ElementTree as ET
                            tree = ET.parse(jacoco_xml_path)
                            root_elem = tree.getroot()
                            # 在所有 <class> 节点中找 modified_class（匹配简单类名结尾）
                            for class_elem in root_elem.findall('.//class'):
                                cname = class_elem.attrib.get('name', '')
                                # 匹配 org/apache/.../ClassName 或内部类 ClassName$Inner
                                simple = cname.split('/')[-1].split('$')[0]
                                if simple == target_class or cname.endswith('/' + target_class):
                                    for c in class_elem.findall('counter'):
                                        ctype = c.attrib.get('type', '')
                                        covered = int(c.attrib.get('covered', 0))
                                        missed  = int(c.attrib.get('missed', 0))
                                        if ctype == 'LINE':
                                            if m_per_line_cov is None:
                                                m_per_line_cov   = covered
                                                m_per_line_total = covered + missed
                                            else:  # 累加内部类
                                                m_per_line_cov   += covered
                                                m_per_line_total += covered + missed
                                        elif ctype == 'BRANCH':
                                            if m_per_branch_cov is None:
                                                m_per_branch_cov   = covered
                                                m_per_branch_total = covered + missed
                                            else:
                                                m_per_branch_cov   += covered
                                                m_per_branch_total += covered + missed
                    except Exception as _xml_err:
                        if logs:
                            with open(logs.get('coverage', os.devnull), 'a') as _f:
                                _f.write(f"[PER_TEST_XML_ERR] {test_case_file}: {_xml_err}\n")

                    per_test_records.append({
                        'test_class':        full_name,
                        'exec_file':         per_test_exec,
                        'm_per_line_cov':    m_per_line_cov,
                        'm_per_line_total':  m_per_line_total,
                        'm_per_branch_cov':  m_per_branch_cov,
                        'm_per_branch_total': m_per_branch_total,
                    })

        # ── 写出 per_test_status.csv ───────────────────────────────────
        self._write_per_test_status(
            global_csv_parent_dir, project_name, target_class, per_test_status_map, logs
        )

        # ── 合并所有 exec，生成全项目覆盖报告 ─────────────────────────
        report_target = os.path.join(report_dir, "final")
        line_cov = branch_cov = line_total = branch_total = line_rate = branch_rate = None
        m_line_cov = m_branch_cov = m_line_total = m_branch_total = m_line_rate = m_branch_rate = None
        modified_class_name = target_class or None

        merged_exec = os.path.join(compiled_test_dir, "jacoco_merged.exec")
        exec_files = [r['exec_file'] for r in per_test_records
                      if r.get('exec_file') and os.path.exists(r.get('exec_file'))]
        res = None
        if exec_files:
            if JACOCO_CLI and os.path.exists(JACOCO_CLI):
                merge_cmd = ["java", "-jar", JACOCO_CLI, "merge"] + exec_files + ["--destfile", merged_exec]
                try:
                    subprocess.run(merge_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
                except Exception:
                    pass
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=merged_exec)
            else:
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=exec_files[0])
        else:
            default_exec = os.path.join(compiled_test_dir, "jacoco.exec")
            if os.path.exists(default_exec):
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=default_exec)
            else:
                res = self.report(compiled_test_dir, report_target)

        if logs and res is not None:
            try:
                out = res.stdout.decode() if isinstance(res.stdout, bytes) else (res.stdout or "")
                err = res.stderr.decode() if isinstance(res.stderr, bytes) else (res.stderr or "")
                with open(logs['coverage'], 'a') as f:
                    f.write("===== Coverage report (all attempts) =====\n")
                    if out:
                        f.write("--- STDOUT ---\n" + out + "\n")
                    if err:
                        f.write("--- STDERR ---\n" + err + "\n")
            except Exception:
                pass

        jacoco_xml_path = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
        if os.path.exists(jacoco_xml_path):
            import xml.etree.ElementTree as ET
            tree = ET.parse(jacoco_xml_path)
            root_elem = tree.getroot()
            counters = root_elem.findall('.//counter')
            line_counters   = [c for c in counters if c.attrib.get('type') == 'LINE']
            branch_counters = [c for c in counters if c.attrib.get('type') == 'BRANCH']
            if line_counters:
                lc = line_counters[-1]
                line_cov   = int(lc.attrib.get('covered', 0))
                line_total = line_cov + int(lc.attrib.get('missed', 0))
                line_rate  = round(100 * line_cov / line_total, 2) if line_total else 0.0
            if branch_counters:
                bc = branch_counters[-1]
                branch_cov   = int(bc.attrib.get('covered', 0))
                branch_total = branch_cov + int(bc.attrib.get('missed', 0))
                branch_rate  = round(100 * branch_cov / branch_total, 2) if branch_total else 0.0

            if modified_class_name:
                for class_elem in root_elem.findall('.//class'):
                    if class_elem.attrib.get('name', '').endswith(modified_class_name):
                        for c in class_elem.findall('counter'):
                            if c.attrib.get('type') == 'LINE':
                                m_line_cov   = int(c.attrib.get('covered', 0))
                                m_line_total = m_line_cov + int(c.attrib.get('missed', 0))
                                m_line_rate  = round(100 * m_line_cov / m_line_total, 2) if m_line_total else 0.0
                            if c.attrib.get('type') == 'BRANCH':
                                m_branch_cov   = int(c.attrib.get('covered', 0))
                                m_branch_total = m_branch_cov + int(c.attrib.get('missed', 0))
                                m_branch_rate  = round(100 * m_branch_cov / m_branch_total, 2) if m_branch_total else 0.0
                        break

        # ── testcase_coverage_detail_by_target.csv ────────────────────
        # 每行代表一个测试类对 modified_class 的覆盖情况
        # 字段: project, target_class, test_class,
        #        m_per_line_cov, m_per_line_total, m_per_line_rate,
        #        m_per_branch_cov, m_per_branch_total, m_per_branch_rate,
        #        branch_contrib_pct, coverage_score
        try:
            detail_csv = os.path.join(global_csv_parent_dir,
                                      "testcase_coverage_detail_by_target.csv")
            header = [
                'project', 'target_class', 'test_class',
                'm_per_line_cov', 'm_per_line_total', 'm_per_line_rate',
                'm_per_branch_cov', 'm_per_branch_total', 'm_per_branch_rate',
                'branch_contrib_pct', 'coverage_score',
            ]
            file_exists = os.path.exists(detail_csv)
            with open(detail_csv, 'a', newline='', encoding='utf-8') as csvf:
                writer = csv.writer(csvf)
                if not file_exists:
                    writer.writerow(header)
                for rec in per_test_records:
                    # ── modified_class 行覆盖率 ──────────────────────────
                    mlc = rec.get('m_per_line_cov')   or 0
                    mlt = rec.get('m_per_line_total') or 0
                    mlr = round(100.0 * mlc / mlt, 4) if mlt else 0.0

                    # ── modified_class 分支覆盖率 ─────────────────────────
                    mbc = rec.get('m_per_branch_cov')   or 0
                    mbt = rec.get('m_per_branch_total') or 0
                    mbr = round(100.0 * mbc / mbt, 4) if mbt else 0.0

                    # ── branch_contrib_pct：本测试类贡献的分支数占该类总分支的比例 ──
                    # 分母用全项目 modified_class 分支总数（m_branch_total），
                    # 若不可用则退回到本测试自身分母
                    denom_branch = (m_branch_total if m_branch_total else mbt) or 0
                    branch_contrib_pct = (
                        round(100.0 * mbc / denom_branch, 4)
                        if denom_branch and mbc else 0.0
                    )

                    # ── coverage_score：综合行 & 分支覆盖率的归一化得分 ───
                    # 公式：0.6 * line_rate/100 + 0.4 * branch_rate/100，
                    # 若某一项缺失则只用另一项
                    if mlt and mbt:
                        coverage_score = round(0.6 * (mlr / 100.0) + 0.4 * (mbr / 100.0), 6)
                    elif mlt:
                        coverage_score = round(mlr / 100.0, 6)
                    elif mbt:
                        coverage_score = round(mbr / 100.0, 6)
                    else:
                        coverage_score = 0.0

                    writer.writerow([
                        project_name,
                        target_class,
                        rec.get('test_class', ''),
                        mlc if mlt else '',
                        mlt if mlt else '',
                        mlr if mlt else '',
                        mbc if mbt else '',
                        mbt if mbt else '',
                        mbr if mbt else '',
                        branch_contrib_pct,
                        coverage_score,
                    ])
        except Exception as e:
            print('Failed to write testcase_coverage_detail_by_target.csv:', e)

        # ── execution_stats log ───────────────────────────────────────
        if logs:
            with open(logs['execution_stats'], 'a') as f:
                f.write(f"[ALL ATTEMPTS]\n")
                f.write(f"  total_tests={total_tests}\n")
                f.write(f"  syntax_errors={syntax_errors}\n")
                f.write(f"  compile_errors={self.COMPILE_ERROR}\n")
                f.write(f"  test_run_errors={self.TEST_RUN_ERROR}\n")
                if line_rate is not None:
                    f.write(f"  全项目行覆盖率: {line_rate}% ({line_cov}/{line_total})\n")
                if branch_rate is not None:
                    f.write(f"  全项目分支覆盖率: {branch_rate}% ({branch_cov}/{branch_total})\n")
                if modified_class_name:
                    f.write(f"  target_class: {modified_class_name}\n")
                    if m_line_rate is not None:
                        f.write(f"    行覆盖率: {m_line_rate}% ({m_line_cov}/{m_line_total})\n")
                    if m_branch_rate is not None:
                        f.write(f"    分支覆盖率: {m_branch_rate}% ({m_branch_cov}/{m_branch_total})\n")

        # ── project_test_summary.csv ──────────────────────────────────
        try:
            run_time_seconds = round((datetime.now() - start_time).total_seconds(), 2)
            Attempts   = total_tests
            Aborted    = max(0, Attempts - total_compile)
            SyntaxError = self.SYNTAX_ERROR
            CompileError = self.COMPILE_ERROR
            RuntimeError = self.TEST_RUN_ERROR
            denom      = Attempts - Aborted if (Attempts - Aborted) > 0 else None
            run_denom  = (Attempts - Aborted - CompileError) if denom else None

            SyntaxRate  = (1.0 - SyntaxError / denom)       if denom else None
            CompileRate = (1.0 - CompileError / denom)       if denom else None
            RunRate     = (1.0 - RuntimeError / run_denom)   if run_denom and run_denom > 0 else None
            Passed      = max(0, Attempts - Aborted - CompileError - RuntimeError)
            PassRate    = (Passed / denom)                   if denom else None

            summary_headers = [
                'project', 'modified_class',
                'Attempts', 'Aborted', 'SyntaxError', 'SyntaxRate',
                'CompileError', 'CompileRate', 'RuntimeError', 'RunRate', 'Passed', 'PassRate',
                'line_cov', 'line_total', 'line_rate',
                'branch_cov', 'branch_total', 'branch_rate',
                'm_line_cov', 'm_line_total', 'm_line_rate',
                'm_branch_cov', 'm_branch_total', 'm_branch_rate', 'run_time'
            ]
            summary_fname = os.path.join(global_csv_parent_dir, 'project_test_summary.csv')
            file_exists = os.path.exists(summary_fname)
            with open(summary_fname, 'a', newline='', encoding='utf-8') as sf:
                w = csv.writer(sf)
                if not file_exists:
                    w.writerow(summary_headers)
                w.writerow([
                    project_name, modified_class_name or "",
                    Attempts, Aborted, SyntaxError,
                    round(SyntaxRate,  4) if SyntaxRate  is not None else '',
                    CompileError,
                    round(CompileRate, 4) if CompileRate is not None else '',
                    RuntimeError,
                    round(RunRate,     4) if RunRate     is not None else '',
                    Passed,
                    round(PassRate,    4) if PassRate    is not None else '',
                    line_cov   if line_cov   is not None else '',
                    line_total if line_total is not None else '',
                    line_rate  if line_rate  is not None else '',
                    branch_cov   if branch_cov   is not None else '',
                    branch_total if branch_total is not None else '',
                    branch_rate  if branch_rate  is not None else '',
                    m_line_cov   if m_line_cov   is not None else '',
                    m_line_total if m_line_total is not None else '',
                    m_line_rate  if m_line_rate  is not None else '',
                    m_branch_cov   if m_branch_cov   is not None else '',
                    m_branch_total if m_branch_total is not None else '',
                    m_branch_rate  if m_branch_rate  is not None else '',
                    run_time_seconds,
                ])
            if logs:
                with open(logs['execution_stats'], 'a') as f:
                    f.write(f"[CSV_TEST_SUMMARY] saved to {summary_fname}\n")
        except Exception as e:
            print('Failed to write project_test_summary.csv:', e)

        total_test_run = total_compile - self.COMPILE_ERROR
        print("SYNTAX TOTAL COUNT:", self.SYNTAX_TOTAL)
        print("SYNTAX ERROR COUNT:", self.SYNTAX_ERROR)
        print("COMPILE TOTAL COUNT:", total_compile)
        print("COMPILE ERROR COUNT:", self.COMPILE_ERROR)
        print("TEST RUN TOTAL COUNT:", total_test_run)
        print("TEST RUN ERROR COUNT:", self.TEST_RUN_ERROR)
        print("-" * 50)
        print("COVERAGE STATISTICS (ALL ATTEMPTS):")
        if line_rate is not None:
            print(f"  全项目 行覆盖率: {line_rate}% ({line_cov}/{line_total})")
        if branch_rate is not None:
            print(f"  全项目 分支覆盖率: {branch_rate}% ({branch_cov}/{branch_total})")
        if modified_class_name:
            print(f"  target_class: {modified_class_name}")
            if m_line_rate is not None:
                print(f"    行覆盖率: {m_line_rate}% ({m_line_cov}/{m_line_total})")
            if m_branch_rate is not None:
                print(f"    分支覆盖率: {m_branch_rate}% ({m_branch_cov}/{m_branch_total})")
        if line_rate is None and branch_rate is None and not modified_class_name:
            print("  未获取到有效覆盖率数据")
        print("-" * 50)
        return total_compile, total_test_run

    # ------------------------------------------------------------------
    # ★ 新增：写出 per_test_status.csv
    # 表头: project, target_class, test_class,
    #        compile_status, exec_status, exec_timeout,
    #        jacoco_exec_size, compile_score, exec_score
    # ------------------------------------------------------------------
    def _write_per_test_status(self, output_dir, project_name, target_class,
                                status_map, logs=None):
        """将 per_test_status_map 写出到 per_test_status.csv（追加模式）"""
        if not status_map:
            return
        try:
            csv_path = os.path.join(output_dir, 'per_test_status.csv')
            header = [
                'project', 'target_class', 'test_class',
                'compile_status', 'exec_status', 'exec_timeout',
                'jacoco_exec_size', 'compile_score', 'exec_score',
            ]
            file_exists = os.path.exists(csv_path)
            with open(csv_path, 'a', newline='', encoding='utf-8') as f:
                writer = csv.writer(f)
                if not file_exists:
                    writer.writerow(header)
                for full_name, s in status_map.items():
                    writer.writerow([
                        project_name,
                        target_class,
                        full_name,
                        s['compile_status'],
                        s['exec_status'],
                        s['exec_timeout'],
                        s['jacoco_exec_size'],
                        s['compile_score'],
                        s['exec_score'],
                    ])
            if logs and 'execution_stats' in logs:
                with open(logs['execution_stats'], 'a') as f:
                    f.write(f"[PER_TEST_STATUS] saved {len(status_map)} rows to {csv_path}\n")
        except Exception as e:
            print('Failed to write per_test_status.csv:', e)

    # ------------------------------------------------------------------
    # run_test_only_with_reason：返回 (exec_ok, is_timeout)
    # ------------------------------------------------------------------
    def run_test_only_with_reason(self, test_file, compiled_test_dir, test_output, logs=None):
        """
        Run a compiled test and log failure reason.
        Returns (exec_ok: bool, is_timeout: bool)
        """
        if os.path.basename(test_output) == 'runtime_error':
            test_output_file = f"{test_output}.txt"
        else:
            test_output_file = f"{test_output}-{os.path.basename(test_file)}.txt"

        cmd = self.java_cmd(compiled_test_dir, test_file)
        try:
            result = subprocess.run(cmd, timeout=TIMEOUT,
                                    stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
            if result.returncode != 0:
                self.TEST_RUN_ERROR += 1
                self.export_runtime_output(result, test_output_file)
                if logs:
                    stderr_content = result.stderr or ""
                    stdout_content = result.stdout or ""
                    core_exception = "未提取到明确异常信息"
                    if stderr_content:
                        for pat in [
                            re.compile(r"=>\s*([\w\.]+Exception):\s*(.*?)(?=\n\s+at|$)", re.DOTALL),
                            re.compile(r"([\w\.]+Exception):\s*(.*?)(?=\n\s+at|$)", re.DOTALL),
                        ]:
                            m = pat.search(stderr_content)
                            if m:
                                core_exception = f"{m.group(1)}：{m.group(2).strip()}"
                                break
                    with open(logs['exec'], 'a') as f:
                        f.write(f"[EXEC_FAILED] {test_file}:\n")
                        f.write(f"[EXEC_RETURN_CODE] {result.returncode}\n")
                        f.write(f"[EXEC_CORE_ERROR] {core_exception}\n")
                        for i, line in enumerate(stderr_content.splitlines()):
                            if i > 50:
                                f.write(f"    [...截断，完整见 {test_output_file}]\n")
                                break
                            f.write(f"    {line}\n")
                        f.write("=" * 80 + "\n")
                return False, False

            else:
                if logs:
                    with open(logs['exec'], 'a') as f:
                        f.write(f"[EXEC_OK] {test_file}\n")
                return True, False

        except subprocess.TimeoutExpired:
            self.TEST_RUN_ERROR += 1
            if logs:
                with open(logs['exec'], 'a') as f:
                    f.write(f"[EXEC_TIMEOUT] {test_file}\n")
            return False, True

        except Exception as e:
            self.TEST_RUN_ERROR += 1
            if logs:
                with open(logs['exec'], 'a') as f:
                    f.write(f"[EXEC_ERROR] {test_file}: {e}\n")
            return False, False

    def run_single_test(self, test_file, compiled_test_dir, compiler_output, test_output):
        if not self.compile(test_file, compiled_test_dir, compiler_output):
            return False
        if os.path.basename(test_output) == 'runtime_error':
            test_output_file = f"{test_output}.txt"
        else:
            test_output_file = f"{test_output}-{os.path.basename(test_file)}.txt"
        cmd = self.java_cmd(compiled_test_dir, test_file)
        try:
            result = subprocess.run(cmd, timeout=TIMEOUT,
                                    stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
            if result.returncode != 0:
                self.TEST_RUN_ERROR += 1
                self.export_runtime_output(result, test_output_file)
                return False
        except subprocess.TimeoutExpired:
            return False
        return True

    @staticmethod
    def export_runtime_output(result, test_output_file):
        with open(test_output_file, "w") as f:
            f.write(result.stdout)
            error_msg = re.sub(r'log4j:WARN.*\n?', '', result.stderr)
            if error_msg:
                f.write(error_msg)

    def compile(self, test_file, compiled_test_dir, compiler_output):
        os.makedirs(compiled_test_dir, exist_ok=True)
        cmd = self.javac_cmd(compiled_test_dir, test_file)
        result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
        if result.returncode != 0:
            self.COMPILE_ERROR += 1
            if os.path.basename(compiler_output) == 'compile_error':
                compiler_output_file = f"{compiler_output}.txt"
            else:
                compiler_output_file = f"{compiler_output}-{os.path.basename(test_file)}.txt"
            with open(compiler_output_file, "w") as f:
                f.write(result.stdout)
                f.write(result.stderr)
            return False
        return True

    def process_single_repo(self):
        if self.has_submodule(self.target_path):
            modules = self.get_submodule(self.target_path)
            postfixed_modules = [f'{self.target_path}/{module}/{self.build_dir_name}' for module in modules]
            build_dir = ':'.join(postfixed_modules)
        else:
            build_dir = os.path.join(self.target_path, self.build_dir_name)
        return build_dir

    @staticmethod
    def get_package(test_file):
        pkg = ''
        try:
            with open(test_file, "r") as f:
                for line in f:
                    line = line.strip()
                    if line.startswith('package '):
                        pkg = line.replace('package ', '').replace(';', '').strip()
                        break
        except Exception:
            pass
        return pkg

    @staticmethod
    def is_module(project_path):
        if not os.path.isdir(project_path):
            return False
        if 'pom.xml' in os.listdir(project_path) and 'target' in os.listdir(project_path):
            return True
        return False

    def get_submodule(self, project_path):
        return [d for d in os.listdir(project_path) if self.is_module(os.path.join(project_path, d))]

    def has_submodule(self, project_path):
        for d in os.listdir(project_path):
            if self.is_module(os.path.join(project_path, d)):
                return True
        return False

    def javac_cmd(self, compiled_test_dir, test_file):
        classpath = f"{JUNIT_JAR}:{MOCKITO_JAR}:{LOG4J_JAR}:{self.dependencies}:{self.build_dir}:."
        classpath_file = os.path.join(compiled_test_dir, 'classpath.txt')
        self.export_classpath(classpath_file, classpath)
        return ["javac", "-d", compiled_test_dir, f"@{classpath_file}", test_file]

    def java_cmd(self, compiled_test_dir, test_file):
        full_test_name = self.get_full_name(test_file)
        classpath = (
            f"{COBERTURA_DIR}/cobertura-2.1.1.jar:{compiled_test_dir}/instrumented:{compiled_test_dir}:"
            f"{JUNIT_JAR}:{MOCKITO_JAR}:{LOG4J_JAR}:{self.dependencies}:{self.build_dir}:."
        )
        classpath_file = os.path.join(compiled_test_dir, 'classpath.txt')
        self.export_classpath(classpath_file, classpath)
        if self.coverage_tool == "cobertura":
            return ["java", f"@{classpath_file}",
                    f"-Dnet.sourceforge.cobertura.datafile={compiled_test_dir}/cobertura.ser",
                    "org.junit.platform.console.ConsoleLauncher", "--disable-banner",
                    "--disable-ansi-colors", "--fail-if-no-tests", "--details=none",
                    "--select-class", full_test_name]
        else:
            jacoco_dest = self.jacoco_destfile if self.jacoco_destfile else os.path.join(compiled_test_dir, 'jacoco.exec')
            javaagent = f"-javaagent:{JACOCO_AGENT}=destfile={jacoco_dest},append=true"
            return ["java", javaagent, f"@{classpath_file}",
                    "org.junit.platform.console.ConsoleLauncher", "--disable-banner",
                    "--disable-ansi-colors", "--fail-if-no-tests", "--details=none",
                    "--select-class", full_test_name]

    @staticmethod
    def export_classpath(classpath_file, classpath):
        with open(classpath_file, 'w') as f:
            f.write("-cp " + classpath)

    def get_full_name(self, test_file):
        package = self.get_package(test_file)
        test_case = os.path.splitext(os.path.basename(test_file))[0]
        return f"{package}.{test_case}" if package else test_case

    def instrument(self, instrument_dir, datafile_dir):
        if self.coverage_tool == "jacoco":
            return
        os.makedirs(instrument_dir, exist_ok=True)
        os.makedirs(datafile_dir, exist_ok=True)
        if 'instrumented' in os.listdir(instrument_dir):
            return
        if self.has_submodule(self.target_path):
            target_classes = os.path.join(self.target_path, '**/target/classes')
        else:
            target_classes = os.path.join(self.target_path, 'target/classes')
        subprocess.run(["bash", os.path.join(COBERTURA_DIR, "cobertura-instrument.sh"),
                        "--basedir", self.target_path,
                        "--destination", f"{instrument_dir}/instrumented",
                        "--datafile", f"{datafile_dir}/cobertura.ser",
                        target_classes], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    def report(self, datafile_dir, report_dir, jacoco_exec_override=None):
        os.makedirs(report_dir, exist_ok=True)
        result = None
        if self.coverage_tool == "cobertura":
            result = subprocess.run(
                ["bash", os.path.join(COBERTURA_DIR, "cobertura-report.sh"),
                 "--format", REPORT_FORMAT,
                 "--datafile", f"{datafile_dir}/cobertura.ser",
                 "--destination", report_dir],
                stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        else:
            jacoco_exec_path = jacoco_exec_override or os.path.join(datafile_dir, "jacoco.exec")
            if os.path.exists(jacoco_exec_path) and os.path.getsize(jacoco_exec_path) > 0:
                mvn_cmd = [
                    "mvn", "jacoco:report",
                    f"-Djacoco.dataFile={jacoco_exec_path}",
                    "-Dmaven.bundle.skip=true",
                    "-f", os.path.join(self.target_path, "pom.xml"),
                ]
                result = subprocess.run(mvn_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                        cwd=self.target_path, text=True)
                jacoco_xml = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
                if os.path.exists(jacoco_xml):
                    print(f"✅ Jacoco 报告生成成功：{os.path.dirname(jacoco_xml)}")
                else:
                    print(f"⚠️  Jacoco 报告生成失败")
            else:
                print(f"⚠️  jacoco.exec 无效或不存在：{jacoco_exec_path}")
        return result

    def make_dependency(self):
        mvn_dependency_dir = 'target/dependency'
        if not self.has_made():
            subprocess.run(
                f"mvn dependency:copy-dependencies -DoutputDirectory={mvn_dependency_dir} -f {self.target_path}/pom.xml",
                shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            subprocess.run(
                f"mvn install -DskipTests -f {self.target_path}/pom.xml",
                shell=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        dep_jars = glob.glob(self.target_path + "/**/*.jar", recursive=True)
        return ':'.join(list(set(dep_jars)))

    def has_made(self):
        for dirpath, dirnames, filenames in os.walk(self.target_path):
            if 'pom.xml' in filenames and 'target' in dirnames:
                if 'dependency' in os.listdir(os.path.join(dirpath, 'target')):
                    return True
        return False

    def copy_tests(self, target_dir):
        tests = glob.glob(self.test_path + "/**/*Test.java", recursive=True)
        target_project = os.path.basename(self.target_path.rstrip('/'))
        for dir_name in ("test_cases", "compiler_output", "test_output", "report"):
            os.makedirs(os.path.join(target_dir, dir_name), exist_ok=True)
        print("Copying tests to", target_project, '...')
        for tc in tests:
            tc_norm = os.path.normpath(tc)
            parts = tc_norm.split(os.sep)
            tc_project = None
            for part in reversed(parts[:-1]):
                if '%' in part:
                    tokens = part.split('%')
                    if len(tokens) >= 2 and tokens[1]:
                        tc_project = tokens[1]
                        break
            if not tc_project and target_project in parts:
                tc_project = target_project
            if not tc_project:
                print(f"Skipping test with unexpected path: {tc}")
                continue
            if tc_project != target_project or not os.path.exists(self.target_path):
                continue
            os.system(f"cp {tc} {os.path.join(target_dir, 'test_cases')}")