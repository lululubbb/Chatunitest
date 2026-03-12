#!/usr/bin/env python3
"""
aggregate_scores.py — 综合评分脚本（v2）
==========================================
根据每个测试类在各维度的评分，计算综合得分并输出到 CSV。

权重配置：
  compile       = 0.15
  exec          = 0.15
  coverage      = 0.30
  bug_revealing = 0.20
  redundancy    = 0.20  （score = 1 - redundancy_score，即越不冗余越好）
  合计           = 1.00

数据来源（全部在 tests%*/ 目录下）：
  {proj}_{tc}_status.csv             → compile_score, exec_score
  {proj}_{tc}_coveragedetail.csv     → coverage_score
  {proj}_{tc}_bugrevealing*.csv      → bug_revealing_score
  Similarity/{proj}_{tc}_bigSims.csv → redundancy_score

输出：
  tests%*/{proj}_{tc}_final_scores.csv

用法：
  python3 aggregate_scores.py --project /path/to/defect4j_projects/Csv_1_b
  python3 aggregate_scores.py --root /path/to/defect4j_projects
  python3 aggregate_scores.py --root /path/to/defect4j_projects --projects Csv_1 Csv_2
  python3 aggregate_scores.py --root /path/to/defect4j_projects --outdir /path/to/results
"""

import os
import sys
import csv
import glob
import re
import argparse
from typing import Optional, Dict

# ── 权重 ──────────────────────────────────────────────────────────────────────
WEIGHT_COMPILE      = 0.15
WEIGHT_EXEC         = 0.15
WEIGHT_COVERAGE     = 0.30
WEIGHT_BUGREVEALING = 0.20
WEIGHT_REDUNDANCY   = 0.20
assert abs(WEIGHT_COMPILE + WEIGHT_EXEC + WEIGHT_COVERAGE +
           WEIGHT_BUGREVEALING + WEIGHT_REDUNDANCY - 1.0) < 1e-9


def safe_float(v, default=None):
    try:
        return float(v)
    except Exception:
        return default


def find_newest_tests_dir(project_path: str) -> Optional[str]:
    candidates = [
        os.path.join(project_path, e)
        for e in os.listdir(project_path)
        if os.path.isdir(os.path.join(project_path, e)) and e.startswith('tests')
    ]
    if not candidates:
        return None
    candidates.sort(key=os.path.getmtime, reverse=True)
    return candidates[0]


def find_file_glob(directory: str, pattern: str) -> Optional[str]:
    matches = sorted(glob.glob(os.path.join(directory, pattern)))
    return matches[0] if matches else None


# ── 读取各维度数据 ────────────────────────────────────────────────────────────

def read_status_csv(tests_dir: str, proj_short: str) -> Dict[str, dict]:
    """→ {test_class: {compile_score, exec_score}}"""
    result = {}
    fpath = find_file_glob(tests_dir, f'{proj_short}_*_status.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = row.get('test_class', '').strip()
                if not tc:
                    continue
                result[tc] = {
                    'compile_score': safe_float(row.get('compile_score'), 0.0),
                    'exec_score':    safe_float(row.get('exec_score'), 0.0),
                }
    except Exception as e:
        print(f'  [WARN] read_status_csv: {e}')
    return result


def read_coverage_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    """→ {test_class: coverage_score}"""
    result = {}
    fpath = find_file_glob(tests_dir, f'{proj_short}_*_coveragedetail.csv')
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = row.get('test_class', '').strip()
                if not tc:
                    continue
                exec_status = row.get('exec_status', 'ok').strip()
                # 非正常执行（超时/失败/编译失败），覆盖率贡献为 0
                cs = 0.0 if exec_status != 'ok' else safe_float(row.get('coverage_score'), 0.0)
                if cs is not None:
                    result[tc] = cs
    except Exception as e:
        print(f'  [WARN] read_coverage_csv: {e}')
    return result


def read_bugrevealing_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    """→ {test_class: 1.0 if bug_revealing else 0.0}"""
    result = {}
    # 优先 class_level 汇总
    fpath = (find_file_glob(tests_dir, f'{proj_short}_*_bugrevealing_class_level.csv') or
             find_file_glob(tests_dir, f'{proj_short}_*_bugrevealing*.csv'))
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = (row.get('test_class') or row.get('full_class_name') or
                      row.get('test_case') or '').strip()
                if not tc:
                    continue
                br_val = str(row.get('bug_revealing',
                                     row.get('is_bug_revealing', '0'))).lower()
                score = 1.0 if br_val in ('true', '1', 'yes') else 0.0
                result[tc] = max(result.get(tc, 0.0), score)
    except Exception as e:
        print(f'  [WARN] read_bugrevealing_csv: {e}')
    return result


def read_bigsims_csv(tests_dir: str, proj_short: str) -> Dict[str, float]:
    """→ {test_class: redundancy_score}  (0~1, 越低越好)"""
    result = {}
    sim_dir = os.path.join(tests_dir, 'Similarity')
    if not os.path.isdir(sim_dir):
        return result
    fpath = (find_file_glob(sim_dir, f'{proj_short}_*_bigSims.csv') or
             find_file_glob(sim_dir, f'{proj_short}_target_*_bigSims.csv'))
    if not fpath:
        return result
    try:
        with open(fpath, newline='', encoding='utf-8') as f:
            for row in csv.DictReader(f):
                tc = row.get('test_case_1', '').strip()
                if not tc:
                    continue
                rs = safe_float(row.get('redundancy_score'))
                if rs is not None:
                    result[tc] = rs
    except Exception as e:
        print(f'  [WARN] read_bigsims_csv: {e}')
    return result


# ── 综合评分计算 ──────────────────────────────────────────────────────────────

def compute_final_scores(status_map, coverage_map, bugrevealing_map, redundancy_map) -> list:
    all_tests = set(status_map) | set(coverage_map) | set(bugrevealing_map) | set(redundancy_map)
    rows = []

    for tc in sorted(all_tests):
        st             = status_map.get(tc, {})
        compile_score  = st.get('compile_score')
        exec_score     = st.get('exec_score')
        coverage_score = coverage_map.get(tc)
        br_score       = bugrevealing_map.get(tc)
        rs             = redundancy_map.get(tc)
        red_contrib    = (1.0 - rs) if rs is not None else None  # 越不冗余越好

        weighted_sum = 0.0
        total_weight = 0.0

        def add(score, weight):
            nonlocal weighted_sum, total_weight
            if score is not None:
                weighted_sum += score * weight
                total_weight += weight

        add(compile_score,  WEIGHT_COMPILE)
        add(exec_score,     WEIGHT_EXEC)
        add(coverage_score, WEIGHT_COVERAGE)
        add(br_score,       WEIGHT_BUGREVEALING)
        add(red_contrib,    WEIGHT_REDUNDANCY)

        final_score      = (weighted_sum / total_weight) if total_weight > 0 else 0.0
        valid_weight_pct = round(100.0 * total_weight, 2)

        rows.append({
            'test_class':          tc,
            'compile_score':       round(compile_score,  6) if compile_score  is not None else '',
            'exec_score':          round(exec_score,     6) if exec_score      is not None else '',
            'coverage_score':      round(coverage_score, 6) if coverage_score  is not None else '',
            'bug_revealing_score': round(br_score,       6) if br_score        is not None else '',
            'redundancy_score':    round(rs,             6) if rs              is not None else '',
            'redundancy_contrib':  round(red_contrib,    6) if red_contrib     is not None else '',
            'final_score':         round(final_score,    6),
            'valid_weight_pct':    valid_weight_pct,
        })
    return rows


# ── 处理单个项目 ──────────────────────────────────────────────────────────────

def process_project(project_path: str, outdir: Optional[str] = None) -> bool:
    project_path = os.path.abspath(project_path)
    proj_prefix  = os.path.basename(project_path)
    proj_short   = re.sub(r'(_b|_f)$', '', proj_prefix)

    tests_dir = find_newest_tests_dir(project_path)
    if not tests_dir:
        print(f'  [SKIP] No tests dir found under {project_path}')
        return False
    print(f'  tests_dir = {tests_dir}')

    status_map       = read_status_csv(tests_dir, proj_short)
    coverage_map     = read_coverage_csv(tests_dir, proj_short)
    bugrevealing_map = read_bugrevealing_csv(tests_dir, proj_short)
    redundancy_map   = read_bigsims_csv(tests_dir, proj_short)

    if not status_map:
        print(f'  [WARN] No status data → skipping {proj_prefix}')
        return False

    print(f'  status={len(status_map)} coverage={len(coverage_map)} '
          f'bugrevealing={len(bugrevealing_map)} redundancy={len(redundancy_map)}')

    rows = compute_final_scores(status_map, coverage_map, bugrevealing_map, redundancy_map)

    # 从 status 文件名提取 target_class
    target_class = ''
    sc_file = find_file_glob(tests_dir, f'{proj_short}_*_status.csv')
    if sc_file:
        m = re.match(rf'^{re.escape(proj_short)}_(.+)_status\.csv$',
                     os.path.basename(sc_file))
        if m:
            target_class = m.group(1)

    out_fname = f'{proj_short}_{target_class}_final_scores.csv' if target_class \
                else f'{proj_short}_final_scores.csv'
    out_dir = outdir if outdir else tests_dir
    os.makedirs(out_dir, exist_ok=True)
    out_path = os.path.join(out_dir, out_fname)

    fieldnames = [
        'test_class',
        'compile_score', 'exec_score', 'coverage_score',
        'bug_revealing_score', 'redundancy_score', 'redundancy_contrib',
        'final_score', 'valid_weight_pct',
    ]
    with open(out_path, 'w', newline='', encoding='utf-8') as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)

    print(f'  ✅ {len(rows)} rows → {out_path}')
    return True


# ── 主入口 ────────────────────────────────────────────────────────────────────

def extract_project_number(p):
    m = re.search(r'_(\d+)_', os.path.basename(p))
    return int(m.group(1)) if m else 0


def main():
    parser = argparse.ArgumentParser()
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('--project', help='Single project dir, e.g. /path/to/Csv_1_b')
    group.add_argument('--root',    help='Root dir containing *_b projects')
    parser.add_argument('--projects', nargs='+',
                        help='Filter by project names (with --root), e.g. Csv_1 Csv_2')
    parser.add_argument('--outdir', default=None,
                        help='Override output dir (default: tests%*/ of each project)')
    args = parser.parse_args()

    if args.project:
        projects = [os.path.abspath(args.project)]
    else:
        root = os.path.abspath(args.root)
        if args.projects:
            projects = []
            for pname in args.projects:
                candidate = os.path.join(root, pname if pname.endswith(('_b','_f')) else pname+'_b')
                if os.path.isdir(candidate):
                    projects.append(candidate)
        else:
            projects = sorted(glob.glob(os.path.join(root, '*_b')),
                              key=extract_project_number)

    if not projects:
        print('No projects found.')
        sys.exit(1)

    ok = sum(1 for p in projects
             if [print(f'\n=== {os.path.basename(p)} ==='), process_project(p, args.outdir)][1])
    print(f'\nDone: {ok}/{len(projects)} projects processed.')


if __name__ == '__main__':
    main()
