#!/usr/bin/env python3
"""
score_dataset.py
================
面向微调数据集的多维度评分脚本。

适用场景：
  已收集的测试用例和待测代码并不在 tests%时间戳/ 这样的目录下，
  而是直接放在 src/ 和 tests/ 同级目录，例如：

    dataset/
      Csv_2/
        src/          ← 源码（Maven 项目，含 pom.xml）
        tests/        ← 测试用例（*Test.java，直接放平或按包放）
        buggy/        ← (可选) buggy 版本，用于 bug_revealing
        fixed/        ← (可选) fixed 版本，用于 bug_revealing

  或者也支持标准的 Defects4J 风格（_b / _f 后缀），即：

    defect4j_projects/
      Csv_2_b/
        src/
        tests/        ← 测试用例放在这里（本脚本会自动识别）

  脚本为每个 sample 计算以下四个维度并输出综合评分 CSV：
    1. compile_score      (0 or 1)
    2. exec_score         (0 or 1)
    3. coverage_score     (行覆盖率 + 分支覆盖率的加权平均)
    4. bug_revealing_score (0 or 1，需提供 buggy/fixed 版本)
    5. redundancy_score   (基于 AST 相似度，越低越好即越不冗余)

用法：
  # 单个 sample 目录（src/ 和 tests/ 同级）
  python3 score_dataset.py --sample /path/to/Csv_2

  # 批量：根目录下有多个 sample 目录
  python3 score_dataset.py --root /path/to/dataset

  # 指定 buggy/fixed 路径（用于 bug_revealing）
  python3 score_dataset.py --sample /path/to/Csv_2 \\
      --buggy /path/to/Csv_2_b --fixed /path/to/Csv_2_f

  # 跳过 bug_revealing（没有 buggy/fixed 版本）
  python3 score_dataset.py --sample /path/to/Csv_2 --skip-bug-revealing

  # 输出结果到指定目录
  python3 score_dataset.py --root /path/to/dataset --outdir /path/to/scores

依赖：
  - 与当前项目 src/ 目录同级运行（使用 test_runner.TestRunner）
  - pip install javalang（相似度计算需要）
  - Java 11, Maven 已安装

输出文件（放在每个 sample 的 tests/ 目录下）：
  tests/
    {proj}_status.csv              ← 编译/执行状态
    {proj}_coveragedetail.csv      ← 覆盖率详情
    {proj}_bugrevealing.csv        ← bug_revealing（可选）
    Similarity/{proj}_bigSims.csv  ← 相似度
    {proj}_final_scores.csv        ← 综合评分
    logs/
      diagnosis.log                ← 诊断日志
"""

import os
import sys
import re
import csv
import glob
import shutil
import subprocess
import argparse
import tempfile
from datetime import datetime
from typing import Optional, Dict, List

# ── 将 src/ 加入 sys.path，以便 import test_runner 等 ──────────────────────
HERE = os.path.dirname(os.path.abspath(__file__))
SRC_DIR = HERE if os.path.exists(os.path.join(HERE, 'test_runner.py')) else \
          os.path.join(HERE, 'src')
if SRC_DIR not in sys.path:
    sys.path.insert(0, SRC_DIR)

SCRIPTS_DIR = os.path.join(SRC_DIR, 'scripts')

# ── 脚本路径 ────────────────────────────────────────────────────────────────
_BUG_REVEALING_SCRIPT   = os.path.join(SCRIPTS_DIR, 'bug_revealing.py')
_CODE_TO_AST_SCRIPT     = os.path.join(SCRIPTS_DIR, 'code_to_ast.py')
_MEASURE_SIM_SCRIPT     = os.path.join(SCRIPTS_DIR, 'measure_similarity.py')
_AGGREGATE_SCRIPT       = os.path.join(SRC_DIR, 'aggregate_scores.py')

for _s in [_BUG_REVEALING_SCRIPT, _CODE_TO_AST_SCRIPT, _MEASURE_SIM_SCRIPT]:
    if not os.path.exists(_s):
        print(f'[ERROR] Required script not found: {_s}')
        sys.exit(1)


# ─────────────────────────────────────────────────────────────────────────────
# 目录结构适配：将 src+tests 布局规范化为 TestRunner 期望的结构
# ─────────────────────────────────────────────────────────────────────────────

def normalize_sample_dir(sample_dir: str) -> dict:
    """
    识别 sample_dir 的目录布局，返回：
      {
        'project_root': str,   ← Maven 项目根（含 pom.xml 和 target/classes）
        'tests_dir':    str,   ← tests%* 风格目录（含 test_cases/ 子目录）
        'proj_short':   str,   ← 项目简称，如 Csv_2
        'buggy':        str|None,
        'fixed':        str|None,
      }

    支持以下布局（自动识别）：
      布局 A（标准 Defects4J）:
        sample_dir/           ← 即 Csv_2_b/
          src/                ← pom.xml 在此处或 sample_dir/
          tests%xxx/
            test_cases/

      布局 B（新收集数据集）:
        sample_dir/
          src/                ← Maven 项目（含 pom.xml）
          tests/              ← 测试用例（*Test.java）
          buggy/              ← (可选)
          fixed/              ← (可选)

      布局 C（极简）:
        sample_dir/
          pom.xml             ← Maven 项目根就是 sample_dir
          tests/              ← 测试用例
    """
    sample_dir = os.path.abspath(sample_dir)
    basename = os.path.basename(sample_dir)

    # 提取 proj_short
    proj_short = re.sub(r'(_b|_f)$', '', basename)

    result = {
        'project_root': None,
        'tests_dir':    None,
        'proj_short':   proj_short,
        'buggy':        None,
        'fixed':        None,
    }

    # ── 布局 A：标准 Defects4J（已有 tests%xxx/）────────────────────────────
    existing_tests = sorted(glob.glob(os.path.join(sample_dir, 'tests%*')))
    if existing_tests:
        result['project_root'] = sample_dir
        result['tests_dir']    = existing_tests[-1]
        # 自动找 fixed/buggy 对
        if basename.endswith('_b'):
            fixed_path = os.path.join(os.path.dirname(sample_dir),
                                      basename.replace('_b', '_f'))
            result['buggy'] = sample_dir
            result['fixed'] = fixed_path if os.path.isdir(fixed_path) else None
        elif basename.endswith('_f'):
            buggy_path = os.path.join(os.path.dirname(sample_dir),
                                      basename.replace('_f', '_b'))
            result['fixed'] = sample_dir
            result['buggy'] = buggy_path if os.path.isdir(buggy_path) else None
        return result

    # ── 布局 B：src/ + tests/ 同级 ───────────────────────────────────────────
    src_subdir   = os.path.join(sample_dir, 'src')
    tests_subdir = os.path.join(sample_dir, 'tests')
    buggy_subdir = os.path.join(sample_dir, 'buggy')
    fixed_subdir = os.path.join(sample_dir, 'fixed')

    if os.path.isdir(src_subdir) and os.path.isdir(tests_subdir):
        # src/ 下可能就是 Maven 项目根，或者 src/ 就是源码而 pom.xml 在 sample_dir
        if os.path.exists(os.path.join(src_subdir, 'pom.xml')):
            project_root = src_subdir
        elif os.path.exists(os.path.join(sample_dir, 'pom.xml')):
            project_root = sample_dir
        else:
            project_root = src_subdir  # 最后兜底

        # 创建规范的 tests%时间戳/ 目录，并把 tests/ 下的 .java 文件放进 test_cases/
        ts = datetime.now().strftime('%y%m%d%H%M%S')
        tests_dir_path = os.path.join(sample_dir, f'tests%{ts}')
        test_cases_path = os.path.join(tests_dir_path, 'test_cases')
        os.makedirs(test_cases_path, exist_ok=True)

        # 复制所有 *Test.java 到 test_cases/
        copied = 0
        for root_d, _, files in os.walk(tests_subdir):
            for f in files:
                if f.endswith('Test.java') or f.endswith('test.java'):
                    src_f = os.path.join(root_d, f)
                    dst_f = os.path.join(test_cases_path, f)
                    if not os.path.exists(dst_f):
                        shutil.copy2(src_f, dst_f)
                        copied += 1
        print(f'  [LAYOUT_B] Copied {copied} test files to {test_cases_path}')

        result['project_root'] = project_root
        result['tests_dir']    = tests_dir_path
        result['buggy']        = buggy_subdir if os.path.isdir(buggy_subdir) else None
        result['fixed']        = fixed_subdir if os.path.isdir(fixed_subdir) else None
        return result

    # ── 布局 C：pom.xml 直接在 sample_dir ───────────────────────────────────
    tests_subdir_c = os.path.join(sample_dir, 'tests')
    if os.path.exists(os.path.join(sample_dir, 'pom.xml')) and os.path.isdir(tests_subdir_c):
        ts = datetime.now().strftime('%y%m%d%H%M%S')
        tests_dir_path = os.path.join(sample_dir, f'tests%{ts}')
        test_cases_path = os.path.join(tests_dir_path, 'test_cases')
        os.makedirs(test_cases_path, exist_ok=True)
        copied = 0
        for root_d, _, files in os.walk(tests_subdir_c):
            for f in files:
                if f.endswith('Test.java'):
                    shutil.copy2(os.path.join(root_d, f),
                                 os.path.join(test_cases_path, f))
                    copied += 1
        print(f'  [LAYOUT_C] Copied {copied} test files to {test_cases_path}')
        result['project_root'] = sample_dir
        result['tests_dir']    = tests_dir_path
        return result

    # 无法识别
    raise ValueError(
        f'Cannot determine layout for sample_dir={sample_dir}. '
        f'Expected: tests%*/, or src/+tests/, or pom.xml+tests/.'
    )


# ─────────────────────────────────────────────────────────────────────────────
# 步骤1：编译 + 执行 + 覆盖率（调用 TestRunner.start_all_test）
# ─────────────────────────────────────────────────────────────────────────────

def run_coverage(project_root: str, tests_dir: str) -> bool:
    """
    调用 TestRunner.start_all_test() 对 tests_dir/test_cases/ 下的测试用例
    进行编译、执行和覆盖率统计。
    输出：
      tests_dir/{proj}_status.csv
      tests_dir/{proj}_coveragedetail.csv
      tests_dir/logs/
    """
    try:
        from test_runner import TestRunner
    except ImportError:
        print('[ERROR] Cannot import TestRunner. Make sure src/ is in sys.path.')
        return False

    try:
        runner = TestRunner(tests_dir, project_root)
        runner.start_all_test()
        return True
    except Exception as e:
        print(f'  [WARN] run_coverage failed: {e}')
        import traceback
        traceback.print_exc()
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 步骤2：bug_revealing（调用 scripts/bug_revealing.py）
# ─────────────────────────────────────────────────────────────────────────────

def run_bug_revealing(buggy: str, fixed: str, tests_dir: str,
                      proj_short: str, timeout: int = 300) -> bool:
    """调用 bug_revealing.py，结果写入 tests_dir/{proj}_bugrevealing.csv"""
    if not buggy or not os.path.isdir(buggy):
        print(f'  [SKIP] bug_revealing: buggy dir not found: {buggy}')
        return False
    if not fixed or not os.path.isdir(fixed):
        print(f'  [SKIP] bug_revealing: fixed dir not found: {fixed}')
        return False

    cmd = [
        sys.executable, _BUG_REVEALING_SCRIPT,
        '--buggy', buggy,
        '--fixed', fixed,
        '--tests', tests_dir,
        '--timeout', str(timeout),
    ]
    print(f'  [BUG_REVEALING] {" ".join(cmd)}')
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=timeout * 5)
        if proc.stdout:
            print(proc.stdout[-2000:])
        if proc.returncode != 0 and proc.stderr:
            print(f'  [WARN] bug_revealing stderr:\n{proc.stderr[-1000:]}')
        return proc.returncode == 0
    except subprocess.TimeoutExpired:
        print(f'  [WARN] bug_revealing timeout')
        return False
    except Exception as e:
        print(f'  [WARN] bug_revealing error: {e}')
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 步骤3：相似度（code_to_ast → measure_similarity）
# ─────────────────────────────────────────────────────────────────────────────

def run_similarity(tests_dir: str) -> bool:
    """调用 code_to_ast.py 和 measure_similarity.py"""
    # code_to_ast
    cmd1 = [sys.executable, _CODE_TO_AST_SCRIPT, tests_dir]
    print(f'  [CODE_TO_AST] {" ".join(cmd1)}')
    try:
        r1 = subprocess.run(cmd1, capture_output=True, text=True, timeout=120)
        if r1.returncode != 0:
            print(f'  [WARN] code_to_ast failed (rc={r1.returncode}): {r1.stderr[:500]}')
    except Exception as e:
        print(f'  [WARN] code_to_ast error: {e}')
        return False

    # measure_similarity
    cmd2 = [sys.executable, _MEASURE_SIM_SCRIPT, tests_dir]
    print(f'  [SIMILARITY] {" ".join(cmd2)}')
    try:
        r2 = subprocess.run(cmd2, capture_output=True, text=True, timeout=300)
        if r2.returncode != 0:
            print(f'  [WARN] measure_similarity failed (rc={r2.returncode}): {r2.stderr[:500]}')
        return r2.returncode == 0
    except Exception as e:
        print(f'  [WARN] measure_similarity error: {e}')
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 步骤4：综合评分（aggregate_scores.py）
# ─────────────────────────────────────────────────────────────────────────────

def run_aggregate(project_root: str, outdir: Optional[str] = None) -> bool:
    """调用 aggregate_scores.py 汇总四个维度"""
    if not os.path.exists(_AGGREGATE_SCRIPT):
        print(f'  [SKIP] aggregate_scores.py not found: {_AGGREGATE_SCRIPT}')
        return False

    cmd = [sys.executable, _AGGREGATE_SCRIPT, '--project', project_root]
    if outdir:
        cmd += ['--outdir', outdir]
    print(f'  [AGGREGATE] {" ".join(cmd)}')
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=120)
        if proc.stdout:
            print(proc.stdout[-1000:])
        if proc.returncode != 0:
            print(f'  [WARN] aggregate failed: {proc.stderr[:500]}')
        return proc.returncode == 0
    except Exception as e:
        print(f'  [WARN] aggregate error: {e}')
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 单个 sample 的完整流程
# ─────────────────────────────────────────────────────────────────────────────

def score_sample(
    sample_dir: str,
    buggy_override: Optional[str] = None,
    fixed_override: Optional[str] = None,
    skip_bug_revealing: bool = False,
    skip_similarity: bool = False,
    outdir: Optional[str] = None,
) -> dict:
    """
    对一个 sample 执行完整的四维评分流程，返回摘要 dict。
    """
    print(f'\n{"="*60}')
    print(f'[SAMPLE] {sample_dir}')
    print(f'{"="*60}')

    result = {
        'sample': sample_dir,
        'proj_short': '',
        'coverage_ok': False,
        'bug_revealing_ok': False,
        'similarity_ok': False,
        'aggregate_ok': False,
        'error': '',
    }

    try:
        info = normalize_sample_dir(sample_dir)
    except ValueError as e:
        result['error'] = str(e)
        print(f'  [ERROR] {e}')
        return result

    project_root = info['project_root']
    tests_dir    = info['tests_dir']
    proj_short   = info['proj_short']
    buggy        = buggy_override or info['buggy']
    fixed        = fixed_override or info['fixed']

    result['proj_short'] = proj_short
    print(f'  project_root = {project_root}')
    print(f'  tests_dir    = {tests_dir}')
    print(f'  buggy        = {buggy}')
    print(f'  fixed        = {fixed}')

    # ── 确保 Maven 项目已编译 ───────────────────────────────────────────────
    _ensure_compiled(project_root)

    # ── 步骤1：编译/执行/覆盖率 ──────────────────────────────────────────────
    print('\n[STEP 1] Coverage & Compilation')
    ok = run_coverage(project_root, tests_dir)
    result['coverage_ok'] = ok
    if not ok:
        print('  [WARN] Coverage step failed, but continuing...')

    # ── 步骤2：bug_revealing ─────────────────────────────────────────────────
    if not skip_bug_revealing:
        print('\n[STEP 2] Bug Revealing')
        ok = run_bug_revealing(buggy, fixed, tests_dir, proj_short)
        result['bug_revealing_ok'] = ok
    else:
        print('\n[STEP 2] Bug Revealing  [SKIPPED]')

    # ── 步骤3：相似度 ────────────────────────────────────────────────────────
    if not skip_similarity:
        print('\n[STEP 3] Similarity (AST)')
        ok = run_similarity(tests_dir)
        result['similarity_ok'] = ok
    else:
        print('\n[STEP 3] Similarity  [SKIPPED]')

    # ── 步骤4：综合评分 ──────────────────────────────────────────────────────
    print('\n[STEP 4] Aggregate Scores')
    ok = run_aggregate(project_root, outdir)
    result['aggregate_ok'] = ok

    # 打印最终评分文件位置
    score_files = glob.glob(os.path.join(tests_dir, '*final_scores.csv'))
    if outdir:
        score_files += glob.glob(os.path.join(outdir, f'{proj_short}*final_scores.csv'))
    if score_files:
        print(f'\n  ✅ Final scores: {score_files[-1]}')
    else:
        print('\n  [WARN] final_scores.csv not found')

    return result


def _ensure_compiled(project_root: str):
    """确保 Maven 项目已编译，如果 target/classes 不存在则运行 mvn compile"""
    target_classes = os.path.join(project_root, 'target', 'classes')
    if os.path.isdir(target_classes):
        return  # 已编译，跳过
    pom = os.path.join(project_root, 'pom.xml')
    if not os.path.exists(pom):
        print(f'  [WARN] No pom.xml found at {project_root}, skipping compile')
        return
    print(f'  [COMPILE] Running mvn compile at {project_root} ...')
    cmd = ['mvn', 'compile', '-DskipTests', '-q', '-f', pom]
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True,
                              timeout=300, cwd=project_root)
        if proc.returncode == 0:
            print('  [COMPILE] ✅ mvn compile OK')
        else:
            print(f'  [COMPILE] ⚠️  mvn compile failed:\n{proc.stderr[-500:]}')
    except Exception as e:
        print(f'  [COMPILE] ERROR: {e}')


# ─────────────────────────────────────────────────────────────────────────────
# 批量模式：扫描 root 下的所有 sample 目录
# ─────────────────────────────────────────────────────────────────────────────

def find_samples(root_dir: str) -> List[str]:
    """
    在 root_dir 下发现所有可能的 sample 目录：
      - 含 src/ 和 tests/ 的子目录
      - 含 pom.xml 和 tests/ 的子目录
      - 含 tests%* 的 Defects4J 风格目录（后缀为 _b）
    跳过 _f 后缀（通常是 _b 的对应 fixed，不单独评分）
    """
    samples = []
    for entry in sorted(os.listdir(root_dir)):
        d = os.path.join(root_dir, entry)
        if not os.path.isdir(d):
            continue
        if entry.endswith('_f'):
            continue  # fixed 版跳过，由 _b 对应处理

        # 含 tests%*（Defects4J 风格）
        if glob.glob(os.path.join(d, 'tests%*')):
            samples.append(d)
            continue
        # 含 src/ + tests/
        if os.path.isdir(os.path.join(d, 'src')) and os.path.isdir(os.path.join(d, 'tests')):
            samples.append(d)
            continue
        # 含 pom.xml + tests/
        if (os.path.exists(os.path.join(d, 'pom.xml')) and
                os.path.isdir(os.path.join(d, 'tests'))):
            samples.append(d)
    return samples


# ─────────────────────────────────────────────────────────────────────────────
# 汇总 CSV
# ─────────────────────────────────────────────────────────────────────────────

def write_summary(results: list, outdir: str):
    """将所有 sample 的 final_scores.csv 合并为一个汇总文件"""
    all_rows = []
    fieldnames = None
    for r in results:
        sample_dir = r.get('sample', '')
        # 搜索 final_scores.csv
        candidates = (
            glob.glob(os.path.join(sample_dir, 'tests%*', '*final_scores.csv')) +
            glob.glob(os.path.join(outdir, f'{r.get("proj_short","*")}*final_scores.csv'))
        )
        for fpath in candidates:
            try:
                with open(fpath, newline='', encoding='utf-8') as f:
                    reader = csv.DictReader(f)
                    if fieldnames is None:
                        fieldnames = ['sample', 'proj_short'] + (reader.fieldnames or [])
                    for row in reader:
                        row['sample'] = sample_dir
                        row['proj_short'] = r.get('proj_short', '')
                        all_rows.append(row)
            except Exception:
                pass

    if not all_rows:
        print('[SUMMARY] No final_scores data found to aggregate')
        return

    summary_path = os.path.join(outdir, 'dataset_scores_summary.csv')
    with open(summary_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames or list(all_rows[0].keys()),
                                extrasaction='ignore')
        writer.writeheader()
        writer.writerows(all_rows)
    print(f'\n✅ Summary written to {summary_path}  ({len(all_rows)} rows)')


# ─────────────────────────────────────────────────────────────────────────────
# CLI 入口
# ─────────────────────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(
        description='Multi-dimension scorer for collected test dataset',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__,
    )
    grp = parser.add_mutually_exclusive_group(required=True)
    grp.add_argument('--sample', help='单个 sample 目录（含 src/ 和 tests/ 或标准 D4J 结构）')
    grp.add_argument('--root',   help='根目录，自动发现下面所有 sample 并批量评分')

    parser.add_argument('--buggy',  default=None, help='覆盖 buggy 项目路径（用于 bug_revealing）')
    parser.add_argument('--fixed',  default=None, help='覆盖 fixed 项目路径（用于 bug_revealing）')
    parser.add_argument('--skip-bug-revealing', action='store_true',
                        help='不执行 bug_revealing（无 buggy/fixed 版本时使用）')
    parser.add_argument('--skip-similarity', action='store_true',
                        help='不执行相似度计算')
    parser.add_argument('--outdir', default=None,
                        help='综合评分输出目录（默认写到 tests_dir 下）')
    parser.add_argument('--timeout', type=int, default=300,
                        help='bug_revealing 单测超时秒数（默认 300）')
    args = parser.parse_args()

    outdir = os.path.abspath(args.outdir) if args.outdir else None
    if outdir:
        os.makedirs(outdir, exist_ok=True)

    # ── 单个 sample ──────────────────────────────────────────────────────────
    if args.sample:
        r = score_sample(
            sample_dir=args.sample,
            buggy_override=args.buggy,
            fixed_override=args.fixed,
            skip_bug_revealing=args.skip_bug_revealing,
            skip_similarity=args.skip_similarity,
            outdir=outdir,
        )
        print(f'\n[DONE] {r}')
        return

    # ── 批量 ─────────────────────────────────────────────────────────────────
    samples = find_samples(args.root)
    if not samples:
        print(f'No samples found under {args.root}')
        sys.exit(1)
    print(f'Found {len(samples)} samples under {args.root}')

    results = []
    for s in samples:
        r = score_sample(
            sample_dir=s,
            skip_bug_revealing=args.skip_bug_revealing,
            skip_similarity=args.skip_similarity,
            outdir=outdir,
        )
        results.append(r)

    # 写汇总
    summary_outdir = outdir or args.root
    os.makedirs(summary_outdir, exist_ok=True)
    write_summary(results, summary_outdir)

    # 打印简要结果
    print('\n=== Batch Summary ===')
    for r in results:
        status = '✅' if r['aggregate_ok'] else ('⚠️ ' if r['coverage_ok'] else '❌')
        print(f'  {status} {r["proj_short"]:20s}  '
              f'coverage={r["coverage_ok"]}  '
              f'br={r["bug_revealing_ok"]}  '
              f'sim={r["similarity_ok"]}  '
              f'agg={r["aggregate_ok"]}  '
              + (f'ERR={r["error"]}' if r.get("error") else ''))


if __name__ == '__main__':
    main()
