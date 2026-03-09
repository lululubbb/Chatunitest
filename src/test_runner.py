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
        tests directory path, e.g.:
        /data/share/TestGPT_ASE/result/scope_test%20230414210243%d3_1/1460%lang_1_f%ToStringBuilder%append%d3/5
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
        # If self.test_path already points to an existing tests directory (contains test_cases),
        # use it directly. This allows running tests against an existing `tests%...` folder.
        if os.path.isdir(self.test_path) and os.path.isdir(os.path.join(self.test_path, 'test_cases')):
            tests_dir = self.test_path
            compiler_output_dir = os.path.join(tests_dir, "compiler_output")
            test_output_dir = os.path.join(tests_dir, "test_output")
            report_dir = os.path.join(tests_dir, "report")

            compiler_output = os.path.join(compiler_output_dir, "CompilerOutput")
            test_output = os.path.join(test_output_dir, "TestOutput")
            compiled_test_dir = os.path.join(tests_dir, "tests_ChatGPT")

            # Prepare logs directory and files
            logs_dir = os.path.join(tests_dir, "logs")
            os.makedirs(logs_dir, exist_ok=True)
            syntax_log = os.path.join(logs_dir, "syntax.log")
            compile_log = os.path.join(logs_dir, "compile.log")
            test_exec_log = os.path.join(logs_dir, "test_exec.log")
            coverage_log = os.path.join(logs_dir, "coverage.log")
            execution_stats_log = os.path.join(logs_dir, "execution_stats.log")
            compile_failed_txt = os.path.join(logs_dir, "compile_failed.txt")

            logs = {
                "syntax": syntax_log,
                "compile": compile_log,
                "exec": test_exec_log,
                "coverage": coverage_log,
                "execution_stats": execution_stats_log,
                "compile_failed": compile_failed_txt
            }

            # Do not copy tests when tests_dir already contains test cases
            return self.run_all_tests(tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs)

        # Otherwise create a new tests directory under the target project and copy tests into it
        date = datetime.now().strftime("%Y%m%d%H%M%S")

        # Directories for the test cases, outputs, and reports
        tests_dir = os.path.join(self.target_path, f"tests%{date}")
        compiler_output_dir = os.path.join(tests_dir, "compiler_output")
        test_output_dir = os.path.join(tests_dir, "test_output")
        report_dir = os.path.join(tests_dir, "report")

        compiler_output = os.path.join(compiler_output_dir, "CompilerOutput")
        test_output = os.path.join(test_output_dir, "TestOutput")
        compiled_test_dir = os.path.join(tests_dir, "tests_ChatGPT")

        self.copy_tests(tests_dir)

        # Prepare logs directory and files for newly created tests_dir
        logs_dir = os.path.join(tests_dir, "logs")
        os.makedirs(logs_dir, exist_ok=True)
        syntax_log = os.path.join(logs_dir, "syntax.log")
        compile_log = os.path.join(logs_dir, "compile.log")
        test_exec_log = os.path.join(logs_dir, "test_exec.log")
        coverage_log = os.path.join(logs_dir, "coverage.log")
        execution_stats_log = os.path.join(logs_dir, "execution_stats.log")
        compile_failed_txt = os.path.join(logs_dir, "compile_failed.txt")

        logs = {
            "syntax": syntax_log,
            "compile": compile_log,
            "exec": test_exec_log,
            "coverage": coverage_log,
            "execution_stats": execution_stats_log,
            "compile_failed": compile_failed_txt
        }

        return self.run_all_tests(tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs)

    def run_all_tests(self, tests_dir, compiled_test_dir, compiler_output, test_output, report_dir, logs=None):
        """
        Run all test cases in a project.
        """
        tests = os.path.join(tests_dir, "test_cases")
        self.instrument(compiled_test_dir, compiled_test_dir)
        # 记录运行开始时间，用于计算 run_time
        start_time = datetime.now()
        total_compile = 0
        total_test_run = 0
        total_tests = 0
        syntax_errors = 0
        compile_failed_list = []

        # Prepare log files if logs dict provided
        if logs:
            # ensure cleared logs
            open(logs['syntax'], 'w').close()
            open(logs['compile'], 'w').close()
            open(logs['exec'], 'w').close()
            open(logs['coverage'], 'w').close()
            open(logs['execution_stats'], 'w').close()
            open(logs['compile_failed'], 'w').close()

        # ====== 新结构：所有测试用例逐个执行并即时生成单用例覆盖报告 ======
        # 1. 先遍历所有 attempt 和所有测试用例，编译和执行
        # 为每个测试生成独立的 jacoco exec（jacoco_<test>.exec），生成报告并解析最后一个 counter
        per_test_records = []
        for t in range(1, 1 + test_number):
            print("Processing attempt: ", str(t))
            for test_case_file in os.listdir(tests):
                if str(t) != test_case_file.split('_')[-1].replace('Test.java', ''):
                    continue
                total_compile += 1
                total_tests += 1
                test_file = os.path.join(tests, test_case_file)

                # 1) Syntax check
                syntax_tmp = tempfile.mkdtemp()
                try:
                    syntax_cmd = self.javac_cmd(syntax_tmp, test_file)
                    # add lint flags
                    syntax_cmd.insert(1, '-Xlint:all')
                    proc = subprocess.run(syntax_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                    stderr = proc.stderr or ""

                    # Always count this file in syntax total
                    self.SYNTAX_TOTAL += 1

                    # Distinguish pure syntax errors vs semantic or other errors
                    syntax_pattern = re.compile(
                        r"(illegal start of expression|';' expected|unclosed string literal|unterminated string literal|unclosed comment|illegal character|identifier expected|expected '\}'|expected '\)'|expected '\]'|missing ';'|syntax error|无效的标记|缺少分号|未结束的字符串文字|语法错误|非法字符|缺少标识符|未闭合的括号|需要';'|需要\)|需要\{)",
                        re.IGNORECASE
                    )
                    if proc.returncode != 0:
                        if syntax_pattern.search(stderr):
                            # Pure syntax error
                            self.SYNTAX_ERROR += 1
                            syntax_errors += 1
                            if logs:
                                with open(logs['syntax'], 'a') as f:
                                    f.write(f"[SYNTAX_ERROR] {test_case_file}: {stderr.splitlines()[0] if stderr else 'syntax error'}\n")
                        else:
                            # Not a pure syntax error (treat as semantic or other)
                            if logs:
                                with open(logs['syntax'], 'a') as f:
                                    f.write(f"[SYNTAX_SEMANTIC] {test_case_file}: {stderr.splitlines()[0] if stderr else 'compile error'}\n")
                    else:
                        # No errors from javac syntax check
                        if logs:
                            with open(logs['syntax'], 'a') as f:
                                f.write(f"[SYNTAX_OK] {test_case_file}\n")
                finally:
                    if os.path.exists(syntax_tmp):
                        shutil.rmtree(syntax_tmp)

                # 2) Compile (call compile directly so we can log and skip run if fails)
                os.makedirs(compiled_test_dir, exist_ok=True)
                cmd = self.javac_cmd(compiled_test_dir, test_file)
                result = subprocess.run(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
                compiled_ok = (result.returncode == 0)
                if not compiled_ok:
                    self.COMPILE_ERROR += 1
                    compile_failed_list.append(test_case_file)
                    if logs:
                        with open(logs['compile'], 'a') as f:
                            f.write(f"[COMPILE_FAILED] {test_case_file}: {result.stderr.splitlines()[0] if result.stderr else 'compile error'}\n")
                        with open(logs['compile_failed'], 'a') as f:
                            f.write(test_case_file + "\n")
                    # 仍然写详细错误到单独文件
                    if os.path.basename(compiler_output) == 'compile_error':
                        compiler_output_file = f"{compiler_output}.txt"
                    else:
                        compiler_output_file = f"{compiler_output}-{os.path.basename(test_file)}.txt"
                    with open(compiler_output_file, "w") as f:
                        f.write(result.stdout)
                        f.write(result.stderr)
                    continue
                else:
                    if logs:
                        with open(logs['compile'], 'a') as f:
                            f.write(f"[COMPILE_OK] {test_case_file}\n")

                # 3) Run test (no recompilation) — 为每个测试设置独立 jacoco exec 文件
                test_basename = os.path.splitext(test_case_file)[0]
                per_test_exec = os.path.join(compiled_test_dir, f"jacoco_{test_basename}.exec")
                # remove old exec if exists
                try:
                    if os.path.exists(per_test_exec):
                        os.remove(per_test_exec)
                except Exception:
                    pass
                # 指定为当前运行使用的 jacoco destfile
                self.jacoco_destfile = per_test_exec
                exec_ok = self.run_test_only_with_reason(test_file, compiled_test_dir, test_output, logs)
                # 清理当前设置，恢复默认
                self.jacoco_destfile = None

                # 4) 记录调试信息：exec_ok 和 exec 文件状态
                if logs:
                    try:
                        with open(logs['execution_stats'], 'a') as f:
                            exists = os.path.exists(per_test_exec)
                            size = os.path.getsize(per_test_exec) if exists else 0
                            f.write(f"[PER_TEST_RUN] {test_case_file} exec_ok={exec_ok} exec_exists={exists} size={size}\n")
                    except Exception:
                        pass

                # 5) 如果 jacoco exec 文件存在并非空，则为该测试生成单用例覆盖报告并解析最后一个 counter
                try:
                    exists = os.path.exists(per_test_exec)
                    size = os.path.getsize(per_test_exec) if exists else 0
                except Exception:
                    exists = False
                    size = 0

                if exists and size > 0:
                    per_test_report_dir = os.path.join(report_dir, "per_test_reports", test_basename)
                    res_single = self.report(compiled_test_dir, per_test_report_dir, jacoco_exec_override=per_test_exec)
                    # 解析生成的 jacoco.xml（如果存在）
                    per_line_cov = per_line_total = per_branch_cov = per_branch_total = None
                    try:
                        jacoco_xml_path = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
                        if os.path.exists(jacoco_xml_path):
                            import xml.etree.ElementTree as ET
                            tree = ET.parse(jacoco_xml_path)
                            root = tree.getroot()
                            counters = root.findall('.//counter')
                            line_counters = [c for c in counters if c.attrib.get('type') == 'LINE']
                            branch_counters = [c for c in counters if c.attrib.get('type') == 'BRANCH']
                            if line_counters:
                                last_line = line_counters[-1]
                                per_line_cov = int(last_line.attrib.get('covered', 0))
                                per_line_missed = int(last_line.attrib.get('missed', 0))
                                per_line_total = per_line_cov + per_line_missed
                            if branch_counters:
                                last_branch = branch_counters[-1]
                                per_branch_cov = int(last_branch.attrib.get('covered', 0))
                                per_branch_missed = int(last_branch.attrib.get('missed', 0))
                                per_branch_total = per_branch_cov + per_branch_missed
                    except Exception:
                        pass

                    per_test_records.append({
                        'test_class': self.get_full_name(test_file),
                        'exec_file': per_test_exec,
                        'line_cov': per_line_cov,
                        'line_total': per_line_total,
                        'branch_cov': per_branch_cov,
                        'branch_total': per_branch_total
                    })
        # 2. 所有测试用例编译和执行后再统一生成 jacoco 覆盖率报告
        report_target = os.path.join(report_dir, "final")
        # 预先初始化覆盖率相关变量，避免后续打印时未定义
        line_cov = branch_cov = line_total = branch_total = line_rate = branch_rate = None
        m_line_cov = m_branch_cov = m_line_total = m_branch_total = m_line_rate = m_branch_rate = None
        modified_class_name = None

        # 合并所有单测产生的 jacoco exec（如果可用），生成全项目覆盖报告
        merged_exec = os.path.join(compiled_test_dir, "jacoco_merged.exec")
        exec_files = [r['exec_file'] for r in per_test_records if r.get('exec_file') and os.path.exists(r.get('exec_file'))]
        res = None
        if exec_files:
            # 使用 jacococli merge（需要在 config 中配置 JACOCO_CLI）
            if JACOCO_CLI and os.path.exists(JACOCO_CLI):
                merge_cmd = ["java", "-jar", JACOCO_CLI, "merge"] + exec_files + ["--destfile", merged_exec]
                try:
                    subprocess.run(merge_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, check=True)
                except Exception:
                    pass
                # 使用合并后的 exec 生成报告
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=merged_exec)
            else:
                # 如果无法 merge，则尝试使用第一个 exec 生成报告（降级处理）
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=exec_files[0])
        else:
            # 无单测 exec，尝试默认路径
            default_exec = os.path.join(compiled_test_dir, "jacoco.exec")
            if os.path.exists(default_exec):
                res = self.report(compiled_test_dir, report_target, jacoco_exec_override=default_exec)
            else:
                res = self.report(compiled_test_dir, report_target)

        # 解析并记录全项目覆盖率（与之前逻辑一致）
        if logs and res is not None:
            try:
                out = res.stdout.decode() if isinstance(res.stdout, bytes) else (res.stdout or "")
                err = res.stderr.decode() if isinstance(res.stderr, bytes) else (res.stderr or "")
                with open(logs['coverage'], 'a') as f:
                    f.write(f"===== Coverage report (all attempts) =====\n")
                    if out:
                        f.write("--- STDOUT ---\n")
                        f.write(out + "\n")
                    if err:
                        f.write("--- STDERR ---\n")
                        f.write(err + "\n")
            except Exception:
                pass

        jacoco_xml_path = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
        if os.path.exists(jacoco_xml_path):
            import xml.etree.ElementTree as ET
            tree = ET.parse(jacoco_xml_path)
            root = tree.getroot()
            # 全项目覆盖率（最后一个counter）
            counters = root.findall('.//counter')
            line_counters = [c for c in counters if c.attrib.get('type') == 'LINE']
            branch_counters = [c for c in counters if c.attrib.get('type') == 'BRANCH']
            if line_counters:
                last_line_counter = line_counters[-1]
                line_cov = int(last_line_counter.attrib.get('covered', 0))
                line_missed = int(last_line_counter.attrib.get('missed', 0))
                line_total = line_cov + line_missed
                line_rate = round(100 * line_cov / line_total, 2) if line_total else 0.0
            if branch_counters:
                last_branch_counter = branch_counters[-1]
                branch_cov = int(last_branch_counter.attrib.get('covered', 0))
                branch_missed = int(last_branch_counter.attrib.get('missed', 0))
                branch_total = branch_cov + branch_missed
                branch_rate = round(100 * branch_cov / branch_total, 2) if branch_total else 0.0

            # Modified class覆盖率（defects4j项目）
            # 识别所有 test_cases 下的测试用例前缀（如 CSVRecord_1_1Test.java -> CSVRecord）
            test_case_files = os.listdir(os.path.join(tests_dir, "test_cases"))
            modified_classes = set()
            for fname in test_case_files:
                if fname.endswith("Test.java"):
                    prefix = fname.split('_')[0]
                    modified_classes.add(prefix)
            # 只统计第一个（如有多个可扩展）
            if modified_classes:
                modified_class_name = list(modified_classes)[0]
                # 在 jacoco.xml 中查找 class name=modified_class_name
                for class_elem in root.findall('.//class'):
                    if class_elem.attrib.get('name','').endswith(modified_class_name):
                        # 取该class下的counter
                        m_line_counter = None
                        m_branch_counter = None
                        for c in class_elem.findall('counter'):
                            if c.attrib.get('type') == 'LINE':
                                m_line_counter = c
                            if c.attrib.get('type') == 'BRANCH':
                                m_branch_counter = c
                        if m_line_counter is not None:
                            m_line_cov = int(m_line_counter.attrib.get('covered', 0))
                            m_line_missed = int(m_line_counter.attrib.get('missed', 0))
                            m_line_total = m_line_cov + m_line_missed
                            m_line_rate = round(100 * m_line_cov / m_line_total, 2) if m_line_total else 0.0
                        if m_branch_counter is not None:
                            m_branch_cov = int(m_branch_counter.attrib.get('covered', 0))
                            m_branch_missed = int(m_branch_counter.attrib.get('missed', 0))
                            m_branch_total = m_branch_cov + m_branch_missed
                            m_branch_rate = round(100 * m_branch_cov / m_branch_total, 2) if m_branch_total else 0.0
                        break

        # 计算每个测试用例对全项目覆盖的贡献，并写入 testcase_coverage_detail.csv（追加）
        try:
            global_csv_parent_dir = os.path.abspath(os.path.join(self.target_path, ".."))
            os.makedirs(global_csv_parent_dir, exist_ok=True)
            detail_csv = os.path.join(global_csv_parent_dir, "testcase_coverage_detail.csv")
            # 列说明：per_line_* = 单个用例的覆盖计数/总数/覆盖率；proj_line_* = 全项目的覆盖计数/总数/覆盖率
            header = [
                'project', 'test_class',
                'per_line_cov', 'per_line_total', 'per_line_rate',
                'per_branch_cov', 'per_branch_total', 'per_branch_rate',
                'proj_line_cov', 'proj_line_total', 'proj_line_rate',
                'proj_branch_cov', 'proj_branch_total', 'proj_branch_rate',
                'line_contrib_pct', 'branch_contrib_pct'
            ]
            file_exists = os.path.exists(detail_csv)
            with open(detail_csv, 'a', newline='', encoding='utf-8') as csvf:
                writer = csv.writer(csvf)
                if not file_exists:
                    writer.writerow(header)
                project_name = os.path.basename(self.target_path.rstrip('/'))
                for rec in per_test_records:
                    per_line_cov = rec.get('line_cov') or 0
                    per_line_total = rec.get('line_total') or 0
                    per_line_rate = round(100 * per_line_cov / per_line_total, 2) if per_line_total else 0.0
                    per_branch_cov = rec.get('branch_cov') or 0
                    per_branch_total = rec.get('branch_total') or 0
                    per_branch_rate = round(100 * per_branch_cov / per_branch_total, 2) if per_branch_total else 0.0
                    # 贡献度按说明： 单个用例覆盖数 ÷ 全项目总覆盖数 × 100%
                    line_contrib = round(100 * per_line_cov / line_cov, 2) if line_cov and per_line_cov else 0.0
                    branch_contrib = round(100 * per_branch_cov / branch_cov, 2) if branch_cov and per_branch_cov else 0.0
                    writer.writerow([
                        project_name,
                        rec.get('test_class', ''),
                        per_line_cov,
                        per_line_total,
                        per_line_rate,
                        per_branch_cov,
                        per_branch_total,
                        per_branch_rate,
                        line_cov if line_cov is not None else '',
                        line_total if line_total is not None else '',
                        line_rate if line_rate is not None else '',
                        branch_cov if branch_cov is not None else '',
                        branch_total if branch_total is not None else '',
                        branch_rate if branch_rate is not None else '',
                        line_contrib,
                        branch_contrib
                    ])
            if logs:
                with open(logs['execution_stats'], 'a') as f:
                    f.write(f"[TESTCASE_COVERAGE_DETAIL] saved to {detail_csv}\n")
        except Exception:
            pass

        # append execution stats for all attempts
        with open(logs['execution_stats'], 'a') as f:
            f.write(f"[ALL ATTEMPTS]\n")
            f.write(f"  total_tests={total_tests}\n")
            f.write(f"  syntax_errors={syntax_errors}\n")
            f.write(f"  compile_errors={self.COMPILE_ERROR}\n")
            f.write(f"  test_run_errors={self.TEST_RUN_ERROR}\n")
            if line_rate is not None:
                f.write(f"  - 全项目 行覆盖率: {line_rate}%（覆盖行数: {line_cov} / 总行数: {line_total}）\n")
            if branch_rate is not None:
                f.write(f"  - 全项目 分支覆盖率: {branch_rate}%（覆盖分支数: {branch_cov} / 总分支数: {branch_total}）\n")
            if modified_class_name:
                f.write(f"  - 针对 Modified Class: {modified_class_name}\n")
                if m_line_rate is not None:
                    f.write(f"    - 行覆盖率: {m_line_rate}%（覆盖行数: {m_line_cov} / 总行数: {m_line_total}）\n")
                if m_branch_rate is not None:
                    f.write(f"    - 分支覆盖率: {m_branch_rate}%（覆盖分支数: {m_branch_cov} / 总分支数: {m_branch_total}）\n")

            # # 写入 CSV 汇总：project_coverage_summary.csv
            # try:
            #     run_time_seconds = round((datetime.now() - start_time).total_seconds(), 2)
            #     global_csv_parent_dir = os.path.abspath(os.path.join(self.target_path, ".."))  # 项目根目录的上级目录                    
            #     os.makedirs(global_csv_parent_dir, exist_ok=True)  # 确保目录存在，避免报错
            #     csv_fname = os.path.join(global_csv_parent_dir, "project_coverage_summary_global.csv") 
            #     headers = [
            #         'project', 'modified_class', 'line_cov', 'line_total', 'line_rate',
            #         'branch_cov', 'branch_total', 'branch_rate',
            #         'm_line_cov', 'm_line_total', 'm_line_rate',
            #         'm_branch_cov', 'm_branch_total', 'm_branch_rate', 'run_time'
            #     ]
            #     project_name = os.path.basename(self.target_path.rstrip('/'))
            #     file_exists = os.path.exists(csv_fname)
            #     with open(csv_fname, 'a', newline='', encoding='utf-8') as csvf:
            #         writer = csv.writer(csvf)
            #         if not file_exists:
            #             writer.writerow(headers)
            #         writer.writerow([
            #             project_name,
            #             modified_class_name or "",
            #             line_cov if line_cov is not None else "",
            #             line_total if line_total is not None else "",
            #             line_rate if line_rate is not None else "",
            #             branch_cov if branch_cov is not None else "",
            #             branch_total if branch_total is not None else "",
            #             branch_rate if branch_rate is not None else "",
            #             m_line_cov if m_line_cov is not None else "",
            #             m_line_total if m_line_total is not None else "",
            #             m_line_rate if m_line_rate is not None else "",
            #             m_branch_cov if m_branch_cov is not None else "",
            #             m_branch_total if m_branch_total is not None else "",
            #             m_branch_rate if m_branch_rate is not None else "",
            #             run_time_seconds
            #         ])
            #     if logs:
            #         with open(logs['execution_stats'], 'a') as f:
            #             f.write(f"[CSV_SUMMARY] saved to {csv_fname}\n")
            # except Exception as e:
            #     print("Failed to write CSV summary:", e)

        # ---- 新增：按用户要求写入测试执行摘要 CSV，包含 Attempts/Aborted/Syntax/Compile/Run/Passed 及对应比率 ----
        try:
            run_time_seconds = round((datetime.now() - start_time).total_seconds(), 2)
            # Attempts: 总任务数（应生成的测试用例数量）
            Attempts = total_tests
            # Aborted: Attempts - COMPILE TOTAL COUNT (total_compile)
            Aborted = Attempts - total_compile
            if Aborted < 0:
                Aborted = 0
            # SyntaxError: self.SYNTAX_ERROR
            SyntaxError = self.SYNTAX_ERROR
            # Compute denom for rates: Attempts - Aborted
            denom = Attempts - Aborted if (Attempts - Aborted) > 0 else None
            SyntaxRate = None
            CompileRate = None
            if denom:
                SyntaxRate = 1.0 - (SyntaxError / denom)
            # CompileError
            CompileError = self.COMPILE_ERROR
            if denom:
                CompileRate = 1.0 - (CompileError / denom)
            # RuntimeError
            RuntimeError = self.TEST_RUN_ERROR
            run_denom = Attempts - Aborted - CompileError
            RunRate = None
            if run_denom and run_denom > 0:
                RunRate = 1.0 - (RuntimeError / run_denom)
            # Passed = Attempts - Aborted - CompileError - RuntimeError
            Passed = Attempts - Aborted - CompileError - RuntimeError
            if Passed < 0:
                Passed = 0
            PassRate = None
            if denom and denom > 0:
                PassRate = Passed / denom

            summary_headers = [
                'project','modified_class','Attempts','Aborted','SyntaxError','SyntaxRate',
                'CompileError','CompileRate','RuntimeError','RunRate','Passed','PassRate',
                'line_cov','line_total','line_rate','branch_cov','branch_total','branch_rate',
                'm_line_cov','m_line_total','m_line_rate','m_branch_cov','m_branch_total','m_branch_rate','run_time'
            ]
            summary_fname = os.path.join(global_csv_parent_dir, 'project_test_summary.csv')
            file_exists = os.path.exists(summary_fname)
            with open(summary_fname, 'a', newline='', encoding='utf-8') as sf:
                w = csv.writer(sf)
                if not file_exists:
                    w.writerow(summary_headers)
                w.writerow([
                    project_name,
                    modified_class_name or "",
                    Attempts,
                    Aborted,
                    SyntaxError,
                    round(SyntaxRate, 4) if SyntaxRate is not None else '',
                    CompileError,
                    round(CompileRate, 4) if CompileRate is not None else '',
                    RuntimeError,
                    round(RunRate, 4) if RunRate is not None else '',
                    Passed,
                    round(PassRate, 4) if PassRate is not None else '',
                    line_cov if line_cov is not None else '',
                    line_total if line_total is not None else '',
                    line_rate if line_rate is not None else '',
                    branch_cov if branch_cov is not None else '',
                    branch_total if branch_total is not None else '',
                    branch_rate if branch_rate is not None else '',
                    m_line_cov if m_line_cov is not None else '',
                    m_line_total if m_line_total is not None else '',
                    m_line_rate if m_line_rate is not None else '',
                    m_branch_cov if m_branch_cov is not None else '',
                    m_branch_total if m_branch_total is not None else '',
                    m_branch_rate if m_branch_rate is not None else '',
                    run_time_seconds
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
        print("-" * 50)  # 分隔线，提升可读性
        print("COVERAGE STATISTICS (ALL ATTEMPTS):")
        # 1. 打印全项目覆盖率
        if line_rate is not None:
            print(f"  - 全项目 行覆盖率: {line_rate}%（覆盖行数: {line_cov} / 总行数: {line_total}）")
        if branch_rate is not None:
            print(f"  - 全项目 分支覆盖率: {branch_rate}%（覆盖分支数: {branch_cov} / 总分支数: {branch_total}）")
        # 2. 打印 Modified Class 覆盖率
        if modified_class_name:
            print(f"  - 针对 Modified Class: {modified_class_name}")
            if m_line_rate is not None:
                print(f"    - 行覆盖率: {m_line_rate}%（覆盖行数: {m_line_cov} / 总行数: {m_line_total}）")
            if m_branch_rate is not None:
                print(f"    - 分支覆盖率: {m_branch_rate}%（覆盖分支数: {m_branch_cov} / 总分支数: {m_branch_total}）")
        # 3. 兜底：无覆盖率数据的提示
        if line_rate is None and branch_rate is None and modified_class_name is None:
            print("  - 未获取到有效覆盖率数据（jacoco.xml 不存在或解析失败）")
        print("-" * 50)
        return total_compile, total_test_run

    def run_test_only_with_reason(self, test_file, compiled_test_dir, test_output, logs=None):
        """
        Run a compiled test (without compiling), and log failure reason if any.
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
                    # 提取核心异常信息
                    core_exception = "未提取到明确异常信息"
                    if stderr_content:
                        junit_exception_pattern = re.compile(r"=>\s*([\w\.]+Exception):\s*(.*?)(?=\n\s+at|$)", re.DOTALL)
                        normal_exception_pattern = re.compile(r"([\w\.]+Exception):\s*(.*?)(?=\n\s+at|$)", re.DOTALL)
                        junit_match = junit_exception_pattern.search(stderr_content)
                        normal_match = normal_exception_pattern.search(stderr_content)
                        if junit_match:
                            exception_class = junit_match.group(1)
                            exception_desc = junit_match.group(2).strip()
                            core_exception = f"{exception_class}：{exception_desc}"
                        elif normal_match:
                            exception_class = normal_match.group(1)
                            exception_desc = normal_match.group(2).strip()
                            core_exception = f"{exception_class}：{exception_desc}"

                    with open(logs['exec'], 'a') as f:
                        f.write(f"[EXEC_FAILED] {test_file}:\n")
                        f.write(f"[EXEC_RETURN_CODE] 返回码：{result.returncode}\n")
                        f.write(f"[EXEC_CORE_ERROR] 核心异常：{core_exception}\n")
                        f.write(f"[EXEC_FULL_ERROR_LOG] 完整失败日志（含JUnit详情、错误栈）：\n")
                        if stderr_content:
                            stderr_lines = stderr_content.splitlines()
                            for i, line in enumerate(stderr_lines):
                                if i > 50:
                                    f.write(f"    [...日志过长，后续内容截断，完整信息请查看：{test_output_file}]\n")
                                    break
                                f.write(f"    {line}\n")
                        else:
                            f.write(f"    无标准错误输出（stderr为空）\n")

                        f.write(f"[EXEC_STDOUT] 标准输出补充（辅助排查）：\n")
                        if stdout_content:
                            stdout_lines = stdout_content.splitlines()
                            for i, line in enumerate(stdout_lines):
                                if i > 50:
                                    f.write(f"    [...后续内容截断，完整信息请查看：{test_output_file}]\n")
                                    break
                                f.write(f"    {line}\n")
                        else:
                            f.write(f"    无标准输出（stdout为空）\n")

                        f.write(f"[EXEC_DETAIL_FILE] 完整错误文件（未截断）：{os.path.abspath(test_output_file)}\n")
                        f.write("=" * 100 + "\n")

                return False
            else:
                if logs:
                    with open(logs['exec'], 'a') as f:
                        f.write(f"[EXEC_OK] {test_file}\n")
        except subprocess.TimeoutExpired:
            if logs:
                with open(logs['exec'], 'a') as f:
                    f.write(f"[EXEC_FAILED] {test_file}: TIMEOUT\n")
            return False
        except Exception as core_exception:
            if logs:
                with open(logs['exec'], 'a') as f:
                    f.write(f"[EXEC_CORE_ERROR] 核心异常：{core_exception}\n")
            return False
        return True

    def run_single_test(self, test_file, compiled_test_dir, compiler_output, test_output):
        """
        Run a test case.
        :return: Whether it is successful or no.
        """
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
            # print(Fore.RED + "TIME OUT!", Style.RESET_ALL)
            return False
        return True

    @staticmethod
    def export_runtime_output(result, test_output_file):
        with open(test_output_file, "w") as f:
            f.write(result.stdout)
            error_msg = result.stderr
            error_msg = re.sub(r'log4j:WARN.*\n?', '', error_msg)
            if error_msg != '':
                f.write(error_msg)

    def compile(self, test_file, compiled_test_dir, compiler_output):
        """
        Compile a test case.
        :param test_file:
        :param compiled_test_dir: the directory to store compiled tests
        :param compiler_output:
        """
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
        """
        Return the all build directories of target repository
        """
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
        """
        If the path has a pom.xml file and target/classes compiled, a module.
        """
        if not os.path.isdir(project_path):
            return False
        if 'pom.xml' in os.listdir(project_path) and 'target' in os.listdir(project_path):
            return True
        return False

    def get_submodule(self, project_path):
        """
        Get all modules in given project.
        :return: module list
        """
        return [d for d in os.listdir(project_path) if self.is_module(os.path.join(project_path, d))]

    def has_submodule(self, project_path):
        """
        Is a project composed by submodules, e.g., gson
        """
        for dir in os.listdir(project_path):
            if self.is_module(os.path.join(project_path, dir)):
                return True
        return False

    def javac_cmd(self, compiled_test_dir, test_file):
        classpath = f"{JUNIT_JAR}:{MOCKITO_JAR}:{LOG4J_JAR}:{self.dependencies}:{self.build_dir}:."
        classpath_file = os.path.join(compiled_test_dir, 'classpath.txt')
        self.export_classpath(classpath_file, classpath)
        return ["javac", "-d", compiled_test_dir, f"@{classpath_file}", test_file]

    def java_cmd(self, compiled_test_dir, test_file):
        full_test_name = self.get_full_name(test_file)
        classpath = f"{COBERTURA_DIR}/cobertura-2.1.1.jar:{compiled_test_dir}/instrumented:{compiled_test_dir}:" \
                    f"{JUNIT_JAR}:{MOCKITO_JAR}:{LOG4J_JAR}:{self.dependencies}:{self.build_dir}:."
        classpath_file = os.path.join(compiled_test_dir, 'classpath.txt')
        self.export_classpath(classpath_file, classpath)
        if self.coverage_tool == "cobertura":
            return ["java", f"@{classpath_file}",
                    f"-Dnet.sourceforge.cobertura.datafile={compiled_test_dir}/cobertura.ser",
                    "org.junit.platform.console.ConsoleLauncher", "--disable-banner", "--disable-ansi-colors",
                    "--fail-if-no-tests", "--details=none", "--select-class", full_test_name]
        else:  # self.coverage_tool == "jacoco"
            # 支持为每次运行指定不同的 jacoco exec 文件（用于单个用例覆盖采集）
            jacoco_dest = self.jacoco_destfile if self.jacoco_destfile else os.path.join(compiled_test_dir, 'jacoco.exec')
            # 使用 append=true 可以在需要时合并多个 exec（这里仍然推荐为单测使用独立文件）
            javaagent = f"-javaagent:{JACOCO_AGENT}=destfile={jacoco_dest},append=true"
            return ["java", javaagent,
                    f"@{classpath_file}",
                    "org.junit.platform.console.ConsoleLauncher", "--disable-banner", "--disable-ansi-colors",
                    "--fail-if-no-tests", "--details=none", "--select-class", full_test_name]

    @staticmethod
    def export_classpath(classpath_file, classpath):
        with open(classpath_file, 'w') as f:
            classpath = "-cp " + classpath
            f.write(classpath)
        return

    def get_full_name(self, test_file):
        package = self.get_package(test_file)
        test_case = os.path.splitext(os.path.basename(test_file))[0]
        if package != '':
            return f"{package}.{test_case}"
        else:
            return test_case

    def instrument(self, instrument_dir, datafile_dir):
        """
        Use cobertura scripts to instrument compiled class.
        Generate 'instrumented' directory.
        """
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
        result = subprocess.run(["bash", os.path.join(COBERTURA_DIR, "cobertura-instrument.sh"),
                                 "--basedir", self.target_path,
                                 "--destination", f"{instrument_dir}/instrumented",
                                 "--datafile", f"{datafile_dir}/cobertura.ser",
                                 target_classes], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    def report(self, datafile_dir, report_dir, jacoco_exec_override=None):
        """
        Generate coverage report by given coverage tool.
        """
        os.makedirs(report_dir, exist_ok=True)
        result = None
        if self.coverage_tool == "cobertura":
            result = subprocess.run(["bash", os.path.join(COBERTURA_DIR, "cobertura-report.sh"),
                                        "--format", REPORT_FORMAT, "--datafile", f"{datafile_dir}/cobertura.ser",
                                        "--destination",
                                        report_dir], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        else:
            jacoco_exec_path = jacoco_exec_override if jacoco_exec_override else os.path.join(datafile_dir, "jacoco.exec")
            if os.path.exists(jacoco_exec_path) and os.path.getsize(jacoco_exec_path) > 0:
                # 构建 mvn jacoco:report 命令（自动生成 target/site/jacoco）
                mvn_jacoco_cmd = [
                    "mvn", "jacoco:report",
                    "-Djacoco.dataFile={}".format(jacoco_exec_path),  # 指定 jacoco.exec 路径
                    "-Dmaven.bundle.skip=true",  # 跳过不必要的 bundle 操作，提升效率
                    "-f", os.path.join(self.target_path, "pom.xml")  # 指定项目 pom.xml 路径
                ]
        
                # 执行 mvn 命令，生成标准 Jacoco 报告（target/site/jacoco）
                # 切换到项目根目录执行，确保 Maven 能正确识别项目结构
                result = subprocess.run(
                    mvn_jacoco_cmd,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.PIPE,
                    cwd=self.target_path,
                    text=True
                )
        
                # 验证报告是否生成成功（检查 jacoco.xml 是否存在）
                jacoco_xml_path = os.path.join(self.target_path, "target", "site", "jacoco", "jacoco.xml")
                if os.path.exists(jacoco_xml_path):
                    print(f"✅ 标准 Jacoco 报告目录生成成功：{os.path.dirname(jacoco_xml_path)}")
                else:
                    print(f"⚠️  标准 Jacoco 报告目录生成失败，可查看 Maven 执行日志排查问题")
            else:
                print(f"⚠️  jacoco.exec 文件无效或不存在，无法生成标准 Jacoco 报告：{jacoco_exec_path}")
                # fallback: do nothing, but result stays None
        return result

    def make_dependency(self):
        """
        Generate runtime dependencies of a given project
        """
        mvn_dependency_dir = 'target/dependency'
        deps = []
        if not self.has_made():
            # Run mvn command to generate dependencies
            # print("Making dependency for project", self.target_path)
            subprocess.run(
                f"mvn dependency:copy-dependencies -DoutputDirectory={mvn_dependency_dir} -f {self.target_path}/pom.xml",
                shell=True,
                stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            subprocess.run(f"mvn install -DskipTests -f {self.target_path}/pom.xml", shell=True,
                           stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

        dep_jars = glob.glob(self.target_path + "/**/*.jar", recursive=True)
        deps.extend(dep_jars)
        deps = list(set(deps))
        return ':'.join(deps)

    def has_made(self):
        """
        If the project has made before
        """
        for dirpath, dirnames, filenames in os.walk(self.target_path):
            if 'pom.xml' in filenames and 'target' in dirnames:
                target = os.path.join(dirpath, 'target')
                if 'dependency' in os.listdir(target):
                    return True
        return False

    def copy_tests(self, target_dir):
        """
        Copy test cases of given project to target path for running.
        :param target_dir: path to target directory used to store test cases
        """
        tests = glob.glob(self.test_path + "/**/*Test.java", recursive=True)
        target_project = os.path.basename(self.target_path.rstrip('/'))
        _ = [os.makedirs(os.path.join(target_dir, dir_name), exist_ok=True) for dir_name in
             ("test_cases", "compiler_output", "test_output", "report")]
        print("Copying tests to ", target_project, '...')
        for tc in tests:
            # tc should be 'pathto/project/testcase', but be robust to unexpected paths.
            tc_norm = os.path.normpath(tc)
            parts = tc_norm.split(os.sep)

            # Try to find an ancestor directory that contains the '%' separator used by our result dirs
            tc_project = None
            for part in reversed(parts[:-1]):  # skip the file itself
                if '%' in part:
                    tokens = part.split('%')
                    if len(tokens) >= 2 and tokens[1]:
                        tc_project = tokens[1]
                        break

            # Fallback: if we couldn't find a '%'-style directory, try to locate the project name in the path
            if not tc_project:
                if target_project in parts:
                    tc_project = target_project

            if not tc_project:
                print(f"Skipping test with unexpected path format: {tc}")
                continue

            if tc_project != target_project or not os.path.exists(self.target_path):
                continue

            os.system(f"cp {tc} {os.path.join(target_dir, 'test_cases')}")
