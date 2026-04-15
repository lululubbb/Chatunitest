#!/usr/bin/env python3
"""
score_dataset.py
================
面向微调数据集的多维度评分脚本。

权重说明：
  完整模式（5维）:
    compile=0.15, exec=0.15, coverage=0.30, bug_revealing=0.20, redundancy=0.20

  跳过 bug_revealing（4维，--skip-bug-revealing）:
    权重重新归一化到 compile=0.1875, exec=0.1875, coverage=0.375, redundancy=0.25
    (即把 0.20 按比例分配给剩余4维，保证 final_score 仍在 0~1 且跨 sample 可比)

  跳过 redundancy（4维，--skip-similarity）:
    类似地重新归一化

  两者都跳过（3维）:
    compile=0.25, exec=0.25, coverage=0.50

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
  # 布局B，有 buggy/ 和 fixed/ 子目录 → 自动执行 bug_revealing（但注意：
    # buggy/ 里必须是完整 Maven 项目，不是只有 .java 文件）
    python3 score_dataset.py --sample /data/dataset/Csv_2

    # 布局B，没有 buggy/ 和 fixed/ → 自动跳过 bug_revealing，权重自动归一化到4维
    python3 score_dataset.py --sample /data/dataset/Csv_2

    # 强制跳过，即使 buggy/ 存在也不执行
    python3 score_dataset.py --sample /data/dataset/Csv_2 --skip-bug-revealing

  # 批量：根目录下有多个 sample 目录
  python3 score_dataset.py --root /path/to/dataset

  # 指定 buggy/fixed 路径（用于 bug_revealing）
  python3 score_dataset.py --sample /path/to/Csv_2 \\
      --buggy /path/to/Csv_2_b --fixed /path/to/Csv_2_f

  # 输出结果到指定目录
  python3 score_dataset.py --root /path/to/dataset --outdir /path/to/scores

  # 跳过 bug_revealing（没有 buggy/fixed 版本）
  python3 score_dataset.py --sample /path/to/Csv_2 --skip-bug-revealing

  # 批量评分
  python3 score_dataset.py --root /data/dataset --skip-bug-revealing --outdir /data/scores

依赖：
  - 与当前项目 src/ 目录同级运行（使用 test_runner.TestRunner）
  - pip install javalang（相似度计算需要）
  - Java 11, Maven 已安装

输出文件（放在每个 sample 的 tests/ 目录下）：
  tests/
    {proj}_status.csv              ← 编译/执行状态
    {proj}_coveragedetail.csv      ← 覆盖率详情
    {proj}_bugrevealing.csv        ← bug_revealing（可选）
    Similarity/
      {proj}_bigSims.csv           ← 相似度
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
from datetime import datetime
from typing import Optional, Dict, List, Tuple

# ── sys.path 设置 ────────────────────────────────────────────────────────────
HERE = os.path.dirname(os.path.abspath(__file__))
SRC_DIR = HERE if os.path.exists(os.path.join(HERE, 'test_runner.py')) else \
          os.path.join(HERE, 'src')
if SRC_DIR not in sys.path:
    sys.path.insert(0, SRC_DIR)

SCRIPTS_DIR = os.path.join(SRC_DIR, 'scripts')

_BUG_REVEALING_SCRIPT = os.path.join(SCRIPTS_DIR, 'bug_revealing.py')
_CODE_TO_AST_SCRIPT   = os.path.join(SCRIPTS_DIR, 'code_to_ast.py')
_MEASURE_SIM_SCRIPT   = os.path.join(SCRIPTS_DIR, 'measure_similarity.py')

for _s in [_BUG_REVEALING_SCRIPT, _CODE_TO_AST_SCRIPT, _MEASURE_SIM_SCRIPT]:
    if not os.path.exists(_s):
        print(f'[ERROR] Required script not found: {_s}')
        sys.exit(1)


# ─────────────────────────────────────────────────────────────────────────────
# 权重归一化：根据可用维度动态计算各维权重
# ─────────────────────────────────────────────────────────────────────────────

# 基础权重（5维完整）
_BASE_WEIGHTS = {
    'compile':       0.15,
    'exec':          0.15,
    'coverage':      0.30,
    'bug_revealing': 0.20,
    'redundancy':    0.20,
}

def compute_normalized_weights(
    skip_bug_revealing: bool = False,
    skip_similarity:    bool = False,
) -> Dict[str, float]:
    """
    根据跳过的维度，把对应权重按比例重新分配给剩余维度，
    确保权重之和始终为 1.0，final_score 在所有 sample 间可比。

    例：skip_bug_revealing=True
      剩余: compile=0.15, exec=0.15, coverage=0.30, redundancy=0.20
      总:   0.80
      归一化: compile=0.1875, exec=0.1875, coverage=0.375, redundancy=0.25
    """
    active = dict(_BASE_WEIGHTS)
    if skip_bug_revealing:
        active.pop('bug_revealing')
    if skip_similarity:
        active.pop('redundancy')

    total = sum(active.values())
    return {k: round(v / total, 8) for k, v in active.items()}


def weights_to_str(weights: Dict[str, float]) -> str:
    return '  '.join(f'{k}={v:.4f}' for k, v in weights.items())


# ─────────────────────────────────────────────────────────────────────────────
# 目录结构适配
# ─────────────────────────────────────────────────────────────────────────────

def normalize_sample_dir(sample_dir: str) -> dict:
    """
    识别 sample_dir 的目录布局，返回规范化后的路径信息。

    支持三种布局：
      A: sample_dir/ 下已有 tests%*/ → 标准 Defects4J，直接使用
      B: sample_dir/src/ + sample_dir/tests/ → 新数据集
      C: sample_dir/pom.xml + sample_dir/tests/ → 极简布局
    """
    sample_dir = os.path.abspath(sample_dir)
    basename = os.path.basename(sample_dir)
    proj_short = re.sub(r'(_b|_f)$', '', basename)

    result = {
        'project_root': None,
        'tests_dir':    None,
        'proj_short':   proj_short,
        'buggy':        None,
        'fixed':        None,
    }

    # ── 布局 A：已有 tests%*/ ─────────────────────────────────────────────────
    existing_tests = sorted(glob.glob(os.path.join(sample_dir, 'tests%*')))
    if existing_tests:
        result['project_root'] = sample_dir
        result['tests_dir']    = existing_tests[-1]
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
    src_sub   = os.path.join(sample_dir, 'src')
    tests_sub = os.path.join(sample_dir, 'tests')
    if os.path.isdir(src_sub) and os.path.isdir(tests_sub):
        if os.path.exists(os.path.join(src_sub, 'pom.xml')):
            project_root = src_sub
        elif os.path.exists(os.path.join(sample_dir, 'pom.xml')):
            project_root = sample_dir
        else:
            project_root = src_sub
        tests_dir_path = _make_tests_dir(sample_dir, tests_sub, proj_short)
        result['project_root'] = project_root
        result['tests_dir']    = tests_dir_path
        result['buggy'] = _find_subdir(sample_dir, 'buggy')
        result['fixed'] = _find_subdir(sample_dir, 'fixed')
        return result

    # ── 布局 C：pom.xml 直接在 sample_dir ───────────────────────────────────
    if (os.path.exists(os.path.join(sample_dir, 'pom.xml')) and
            os.path.isdir(os.path.join(sample_dir, 'tests'))):
        tests_dir_path = _make_tests_dir(sample_dir,
                                         os.path.join(sample_dir, 'tests'),
                                         proj_short)
        result['project_root'] = sample_dir
        result['tests_dir']    = tests_dir_path
        return result

    raise ValueError(
        f'Cannot determine layout for {sample_dir}. '
        f'Expected: tests%*/, or src/+tests/, or pom.xml+tests/.'
    )


def _make_tests_dir(sample_dir: str, tests_src: str, proj_short: str) -> str:
    """创建 tests%时间戳/test_cases/，把 tests_src 下的 *Test.java 复制进去"""
    ts = datetime.now().strftime('%y%m%d%H%M%S')
    tests_dir = os.path.join(sample_dir, f'tests%{ts}')
    test_cases = os.path.join(tests_dir, 'test_cases')
    os.makedirs(test_cases, exist_ok=True)
    copied = 0
    for root_d, _, files in os.walk(tests_src):
        for f in files:
            if f.endswith('Test.java') or f.endswith('test.java'):
                dst = os.path.join(test_cases, f)
                if not os.path.exists(dst):
                    shutil.copy2(os.path.join(root_d, f), dst)
                    copied += 1
    print(f'  [LAYOUT] Copied {copied} test files → {test_cases}')
    return tests_dir


def _find_subdir(parent: str, name: str) -> Optional[str]:
    p = os.path.join(parent, name)
    return p if os.path.isdir(p) else None


# ─────────────────────────────────────────────────────────────────────────────
# 步骤1：编译 + 执行 + 覆盖率
# ─────────────────────────────────────────────────────────────────────────────

def run_coverage(project_root: str, tests_dir: str) -> bool:
    try:
        from test_runner import TestRunner
    except ImportError:
        print('[ERROR] Cannot import TestRunner')
        return False
    try:
        runner = TestRunner(tests_dir, project_root)
        runner.start_all_test()
        return True
    except Exception as e:
        print(f'  [WARN] run_coverage: {e}')
        import traceback; traceback.print_exc()
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 步骤2：bug_revealing
# ─────────────────────────────────────────────────────────────────────────────

def run_bug_revealing(buggy: str, fixed: str, tests_dir: str,
                      timeout: int = 300) -> bool:
    if not buggy or not os.path.isdir(buggy):
        print(f'  [SKIP] bug_revealing: buggy dir not found: {buggy}')
        return False
    if not fixed or not os.path.isdir(fixed):
        print(f'  [SKIP] bug_revealing: fixed dir not found: {fixed}')
        return False
    cmd = [sys.executable, _BUG_REVEALING_SCRIPT,
           '--buggy', buggy, '--fixed', fixed, '--tests', tests_dir,
           '--timeout', str(timeout)]
    print(f'  [BUG_REVEALING] {" ".join(cmd)}')
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True,
                              timeout=timeout * 5)
        if proc.stdout:
            print(proc.stdout[-2000:])
        if proc.returncode != 0 and proc.stderr:
            print(f'  [WARN] bug_revealing stderr:\n{proc.stderr[-1000:]}')
        return proc.returncode == 0
    except subprocess.TimeoutExpired:
        print('  [WARN] bug_revealing timeout')
        return False
    except Exception as e:
        print(f'  [WARN] bug_revealing: {e}')
        return False


# ─────────────────────────────────────────────────────────────────────────────
# 步骤3：相似度
# ─────────────────────────────────────────────────────────────────────────────

def run_similarity(tests_dir: str) -> bool:
    for script, label in [(_CODE_TO_AST_SCRIPT, 'code_to_ast'),
                           (_MEASURE_SIM_SCRIPT, 'measure_similarity')]:
        cmd = [sys.executable, script, tests_dir]
        print(f'  [{label.upper()}] {" ".join(cmd)}')
        try:
            r = subprocess.run(cmd, capture_output=True, text=True, timeout=300)
            if r.returncode != 0:
                print(f'  [WARN] {label} failed (rc={r.returncode}): {r.stderr[:500]}')
        except Exception as e:
            print(f'  [WARN] {label}: {e}')
            return False
    return True


# ─────────────────────────────────────────────────────────────────────────────
# 步骤4：综合评分（内建，不依赖 aggregate_scores.py，权重可控）
# ─────────────────────────────────────────────────────────────────────────────

def safe_float(v, default=None):
    try:
        return float(v)
    except Exception:
        return default


def _find_file(directory: str, pattern: str) -> Optional[str]:
    matches = sorted(glob.glob(os.path.join(directory, pattern)))
    return matches[0] if matches else None


def read_status_csv(tests_dir: str, proj_short: str) -> Dict[str, dict]:
    result = {}
    fpath = _find_file(tests_dir, f'{proj_short}_*_status.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = (row.get('test_class') or '').strip()
                if not tc:
                    continue
                result[tc] = {
                    'compile_score': safe_float(row.get('compile_score')),
                    'exec_score':    safe_float(row.get('exec_score')),
                }
    except Exception as e:
        print(f'  [WARN] read_status_csv: {e}')
    return result


def read_coverage_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    result = {}
    fpath = _find_file(tests_dir, f'{proj_short}_*_coveragedetail*.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = (row.get('test_class') or '').strip()
                if not tc:
                    continue
                v = safe_float(row.get('coverage_score'))
                if v is not None:
                    result[tc] = v
    except Exception as e:
        print(f'  [WARN] read_coverage_csv: {e}')
    return result


def read_bugrevealing_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    result = {}
    fpath = _find_file(tests_dir, f'{proj_short}*bugrevealing*.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = (row.get('test_class') or '').strip()
                if not tc:
                    continue
                br = str(row.get('bug_revealing', '0')).lower()
                result[tc] = max(result.get(tc, 0.0),
                                 1.0 if br in ('true', '1', 'yes') else 0.0)
    except Exception as e:
        print(f'  [WARN] read_bugrevealing_csv: {e}')
    return result


def read_similarity_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    result = {}
    sim_dir = os.path.join(tests_dir, 'Similarity')
    if not os.path.isdir(sim_dir):
        return result
    fpath = _find_file(sim_dir, f'{proj_short}_*_bigSims.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = (row.get('test_case_1') or '').strip()
                if not tc:
                    continue
                rs = safe_float(row.get('redundancy_score'))
                if rs is not None:
                    result[tc] = rs
    except Exception as e:
        print(f'  [WARN] read_similarity_csv: {e}')
    return result


def compute_scores(
    status_map:       Dict[str, dict],
    coverage_map:     Dict[str, float],
    bugrevealing_map: Dict[str, float],
    redundancy_map:   Dict[str, float],
    weights:          Dict[str, float],
) -> List[dict]:
    """
    根据传入的 weights 字典（已归一化）计算综合评分。
    weights 只包含可用维度，缺失维度不参与计算。
    """
    all_tc = (set(status_map) | set(coverage_map) |
              set(bugrevealing_map) | set(redundancy_map))
    rows = []

    for tc in sorted(all_tc):
        st = status_map.get(tc, {})
        cs = st.get('compile_score')
        es = st.get('exec_score')
        cv = coverage_map.get(tc)
        br = bugrevealing_map.get(tc) if bugrevealing_map else None
        rs = redundancy_map.get(tc)
        red_contrib = (1.0 - rs) if rs is not None else None

        wsum = 0.0
        for dim, score in [('compile',       cs),
                            ('exec',          es),
                            ('coverage',      cv),
                            ('bug_revealing', br),
                            ('redundancy',    red_contrib)]:
            w = weights.get(dim, 0.0)
            if score is not None and w > 0:
                wsum += score * w

        # 处理某条记录中个别维度数据缺失（比如这个 test_class 没有 coverage 数据）
        # 此时用当前记录实际可用维度的权重之和来归一化
        actual_w = sum(
            weights.get(dim, 0.0)
            for dim, score in [('compile',       cs),
                                ('exec',          es),
                                ('coverage',      cv),
                                ('bug_revealing', br),
                                ('redundancy',    red_contrib)]
            if score is not None and weights.get(dim, 0.0) > 0
        )
        final = round(wsum / actual_w, 6) if actual_w > 0 else 0.0

        rows.append({
            'test_class':          tc,
            'compile_score':       round(cs,          6) if cs  is not None else '',
            'exec_score':          round(es,          6) if es  is not None else '',
            'coverage_score':      round(cv,          6) if cv  is not None else '',
            'bug_revealing_score': round(br,          6) if br  is not None else '',
            'redundancy_score':    round(rs,          6) if rs  is not None else '',
            'redundancy_contrib':  round(red_contrib, 6) if red_contrib is not None else '',
            'final_score':         final,
            'dims_used':           ','.join(
                d for d, s in [('compile', cs), ('exec', es), ('coverage', cv),
                                ('bug_revealing', br), ('redundancy', red_contrib)]
                if s is not None and weights.get(
                    'redundancy' if d == 'redundancy' else d, 0.0) > 0
            ),
        })
    return rows


def run_aggregate_internal(
    project_root:      str,
    tests_dir:         str,
    proj_short:        str,
    weights:           Dict[str, float],
    outdir:            Optional[str] = None,
) -> bool:
    """内建综合评分：直接读 CSV 文件计算，不依赖外部 aggregate_scores.py"""
    status_map       = read_status_csv(tests_dir, proj_short)
    coverage_map     = read_coverage_csv(tests_dir, proj_short)
    bugrevealing_map = read_bugrevealing_csv(tests_dir, proj_short) if 'bug_revealing' in weights else {}
    redundancy_map   = read_similarity_csv(tests_dir, proj_short)   if 'redundancy'    in weights else {}

    if not status_map:
        print(f'  [WARN] No status data found, cannot compute final scores')
        return False

    print(f'  status={len(status_map)}  coverage={len(coverage_map)}  '
          f'bug_revealing={len(bugrevealing_map)}  redundancy={len(redundancy_map)}')
    print(f'  weights: {weights_to_str(weights)}')

    rows = compute_scores(status_map, coverage_map, bugrevealing_map,
                          redundancy_map, weights)

    # 确定 target_class
    target_class = ''
    sc = _find_file(tests_dir, f'{proj_short}_*_status.csv')
    if sc:
        m = re.match(rf'^{re.escape(proj_short)}_(.+)_status\.csv$',
                     os.path.basename(sc))
        if m:
            target_class = m.group(1)

    fname = (f'{proj_short}_{target_class}_final_scores.csv' if target_class
             else f'{proj_short}_final_scores.csv')
    out_path = os.path.join(outdir or tests_dir, fname)
    os.makedirs(os.path.dirname(out_path), exist_ok=True)

    fieldnames = ['test_class', 'compile_score', 'exec_score', 'coverage_score',
                  'bug_revealing_score', 'redundancy_score', 'redundancy_contrib',
                  'final_score', 'dims_used']

    # 写入时追加权重配置注释行（作为 CSV 第一行注释，以 # 开头）
    with open(out_path, 'w', newline='', encoding='utf-8') as f:
        f.write(f'# weights: {weights_to_str(weights)}\n')
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)

    print(f'  ✅ {len(rows)} rows → {out_path}')
    return True


# ─────────────────────────────────────────────────────────────────────────────
# 确保 Maven 编译
# ─────────────────────────────────────────────────────────────────────────────

def ensure_compiled(project_root: str):
    target_classes = os.path.join(project_root, 'target', 'classes')
    if os.path.isdir(target_classes):
        return
    pom = os.path.join(project_root, 'pom.xml')
    if not os.path.exists(pom):
        print(f'  [WARN] No pom.xml at {project_root}')
        return
    print(f'  [COMPILE] mvn compile @ {project_root} ...')
    try:
        proc = subprocess.run(
            ['mvn', 'compile', '-DskipTests', '-q', '-f', pom],
            capture_output=True, text=True, timeout=300, cwd=project_root)
        if proc.returncode == 0:
            print('  [COMPILE] ✅ OK')
        else:
            print(f'  [COMPILE] ⚠️  failed:\n{proc.stderr[-500:]}')
    except Exception as e:
        print(f'  [COMPILE] ERROR: {e}')


# ─────────────────────────────────────────────────────────────────────────────
# 单个 sample 完整流程
# ─────────────────────────────────────────────────────────────────────────────

def score_sample(
    sample_dir:         str,
    buggy_override:     Optional[str] = None,
    fixed_override:     Optional[str] = None,
    skip_bug_revealing: bool = False,
    skip_similarity:    bool = False,
    outdir:             Optional[str] = None,
    timeout:            int = 300,
) -> dict:
    print(f'\n{"="*60}')
    print(f'[SAMPLE] {sample_dir}')
    print(f'{"="*60}')

    result = {
        'sample': sample_dir, 'proj_short': '',
        'coverage_ok': False, 'bug_revealing_ok': False,
        'similarity_ok': False, 'aggregate_ok': False, 'error': '',
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

    # 计算本次使用的权重（归一化）
    weights = compute_normalized_weights(
        skip_bug_revealing=skip_bug_revealing or not (buggy and fixed),
        skip_similarity=skip_similarity,
    )
    print(f'  active weights: {weights_to_str(weights)}')

    # ── 确保 Maven 编译 ───────────────────────────────────────────────────────
    ensure_compiled(project_root)

    # ── 步骤1：覆盖率 ─────────────────────────────────────────────────────────
    print('\n[STEP 1] Coverage & Compilation')
    ok = run_coverage(project_root, tests_dir)
    result['coverage_ok'] = ok

    # ── 步骤2：bug_revealing ─────────────────────────────────────────────────
    if not skip_bug_revealing and buggy and fixed:
        print('\n[STEP 2] Bug Revealing')
        ok = run_bug_revealing(buggy, fixed, tests_dir, timeout)
        result['bug_revealing_ok'] = ok
    else:
        print(f'\n[STEP 2] Bug Revealing  [SKIPPED]  '
              f'(skip_flag={skip_bug_revealing}, buggy={bool(buggy)}, fixed={bool(fixed)})')

    # ── 步骤3：相似度 ─────────────────────────────────────────────────────────
    if not skip_similarity:
        print('\n[STEP 3] Similarity (AST)')
        ok = run_similarity(tests_dir)
        result['similarity_ok'] = ok
    else:
        print('\n[STEP 3] Similarity  [SKIPPED]')

    # ── 步骤4：综合评分（内建，权重已归一化）─────────────────────────────────
    print('\n[STEP 4] Aggregate Scores (normalized weights)')
    ok = run_aggregate_internal(project_root, tests_dir, proj_short, weights, outdir)
    result['aggregate_ok'] = ok

    return result


# ─────────────────────────────────────────────────────────────────────────────
# 批量模式
# ─────────────────────────────────────────────────────────────────────────────

def find_samples(root_dir: str) -> List[str]:
    samples = []
    for entry in sorted(os.listdir(root_dir)):
        d = os.path.join(root_dir, entry)
        if not os.path.isdir(d) or entry.endswith('_f'):
            continue
        if (glob.glob(os.path.join(d, 'tests%*')) or
                (os.path.isdir(os.path.join(d, 'src')) and
                 os.path.isdir(os.path.join(d, 'tests'))) or
                (os.path.exists(os.path.join(d, 'pom.xml')) and
                 os.path.isdir(os.path.join(d, 'tests')))):
            samples.append(d)
    return samples


def write_summary(results: list, outdir: str,
                  weights: Dict[str, float]):
    """合并所有 final_scores.csv 为一个汇总文件，并记录权重配置"""
    all_rows, fieldnames = [], None
    for r in results:
        sample_dir = r.get('sample', '')
        candidates = (
            glob.glob(os.path.join(sample_dir, 'tests%*', '*final_scores.csv')) +
            glob.glob(os.path.join(outdir, f'{r.get("proj_short","*")}*final_scores.csv'))
        )
        for fpath in candidates:
            try:
                with open(fpath, newline='', encoding='utf-8') as f:
                    # 跳过 # 注释行
                    lines = [l for l in f.readlines() if not l.startswith('#')]
                import io
                reader = csv.DictReader(io.StringIO(''.join(lines)))
                if fieldnames is None:
                    fieldnames = ['sample', 'proj_short'] + (reader.fieldnames or [])
                for row in reader:
                    row['sample']     = sample_dir
                    row['proj_short'] = r.get('proj_short', '')
                    all_rows.append(row)
            except Exception:
                pass

    if not all_rows:
        print('[SUMMARY] No data to summarize')
        return

    summary_path = os.path.join(outdir, 'dataset_scores_summary.csv')
    with open(summary_path, 'w', newline='', encoding='utf-8') as f:
        f.write(f'# weights used: {weights_to_str(weights)}\n')
        writer = csv.DictWriter(f, fieldnames=fieldnames or list(all_rows[0].keys()),
                                extrasaction='ignore')
        writer.writeheader()
        writer.writerows(all_rows)
    print(f'\n✅ Summary ({len(all_rows)} rows) → {summary_path}')
    print(f'   weights: {weights_to_str(weights)}')


# ─────────────────────────────────────────────────────────────────────────────
# CLI
# ─────────────────────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(
        description='Multi-dimension scorer for dataset test cases',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__,
    )
    grp = parser.add_mutually_exclusive_group(required=True)
    grp.add_argument('--sample', help='单个 sample 目录')
    grp.add_argument('--root',   help='根目录，批量处理所有子目录')

    parser.add_argument('--buggy',  default=None, help='buggy 项目路径（bug_revealing 用）')
    parser.add_argument('--fixed',  default=None, help='fixed 项目路径（bug_revealing 用）')
    parser.add_argument('--skip-bug-revealing', action='store_true',
                        help='跳过 bug_revealing（权重自动归一化到其余维度）')
    parser.add_argument('--skip-similarity', action='store_true',
                        help='跳过相似度计算（权重自动归一化到其余维度）')
    parser.add_argument('--outdir', default=None, help='综合评分输出目录')
    parser.add_argument('--timeout', type=int, default=300, help='bug_revealing 超时秒数')
    args = parser.parse_args()

    outdir = os.path.abspath(args.outdir) if args.outdir else None
    if outdir:
        os.makedirs(outdir, exist_ok=True)

    # 预先计算权重（用于打印和汇总文件头）
    weights = compute_normalized_weights(
        skip_bug_revealing=args.skip_bug_revealing,
        skip_similarity=args.skip_similarity,
    )
    print(f'\n[CONFIG] Active weights: {weights_to_str(weights)}')

    # ── 单个 sample ──────────────────────────────────────────────────────────
    if args.sample:
        r = score_sample(
            sample_dir=args.sample,
            buggy_override=args.buggy,
            fixed_override=args.fixed,
            skip_bug_revealing=args.skip_bug_revealing,
            skip_similarity=args.skip_similarity,
            outdir=outdir,
            timeout=args.timeout,
        )
        print(f'\n[DONE] {r}')
        return

    # ── 批量 ─────────────────────────────────────────────────────────────────
    samples = find_samples(args.root)
    if not samples:
        print(f'No samples found under {args.root}')
        sys.exit(1)
    print(f'Found {len(samples)} samples')

    results = []
    for s in samples:
        r = score_sample(
            sample_dir=s,
            skip_bug_revealing=args.skip_bug_revealing,
            skip_similarity=args.skip_similarity,
            outdir=outdir,
            timeout=args.timeout,
        )
        results.append(r)

    summary_outdir = outdir or args.root
    os.makedirs(summary_outdir, exist_ok=True)
    write_summary(results, summary_outdir, weights)

    print('\n=== Batch Summary ===')
    for r in results:
        st = '✅' if r['aggregate_ok'] else ('⚠️ ' if r['coverage_ok'] else '❌')
        print(f'  {st} {r["proj_short"]:20s}'
              f'  cov={r["coverage_ok"]}  br={r["bug_revealing_ok"]}'
              f'  sim={r["similarity_ok"]}  agg={r["aggregate_ok"]}'
              + (f'  ERR={r["error"]}' if r.get("error") else ''))


if __name__ == '__main__':
    main()