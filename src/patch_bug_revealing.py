#!/usr/bin/env python3
"""
Patch bug_revealing.py to:
1. Add `target_class` field to per-method CSV (*_bugrevealing.csv)
   New header: project_name, target_class, test_class, test_method,
               buggy_status, fixed_status, bug_revealing, buggy_rc, fixed_rc, notes

2. Add class-level aggregation CSV (*_bugrevealing_class_level.csv)
   Fields: project_name, target_class, test_class, total_methods,
           bug_revealing_methods, bug_revealing_rate, has_bug_revealing, br_score

Usage:
    python3 patch_bug_revealing.py src/scripts/bug_revealing.py
"""

import sys
import os
import shutil
import re


def patch(path):
    with open(path, 'r', encoding='utf-8') as f:
        src = f.read()

    original = src
    report = []

    # ──────────────────────────────────────────────────────────────────────────
    # PATCH 1: target_class 推断函数 + 调用，注入在 out_rows = [] 前
    # ──────────────────────────────────────────────────────────────────────────
    INJECT_CODE = r'''
    # ── 推断 target_class（modified class 简单类名）──────────────────────────
    def _resolve_target_class_from_buggy(project_root, tests_top=None):
        """返回 target_class 简单类名，失败返回空字符串"""
        meta = os.path.join(project_root, 'modified_classes.src')
        if os.path.exists(meta):
            try:
                with open(meta) as _f:
                    _line = _f.readline().strip()
                if _line:
                    return _line.split('.')[-1]
            except Exception:
                pass
        prop = os.path.join(project_root, 'defects4j.build.properties')
        if os.path.exists(prop):
            try:
                with open(prop) as _f:
                    for _l in _f:
                        if 'd4j.classes.modified' in _l and '=' in _l:
                            _val = _l.split('=', 1)[1].strip()
                            _first = _val.split(',')[0].strip()
                            if _first:
                                return _first.split('.')[-1]
            except Exception:
                pass
        tc_dir = None
        if tests_top:
            _cand = os.path.join(tests_top, 'test_cases')
            tc_dir = _cand if os.path.isdir(_cand) else (tests_top if os.path.isdir(tests_top) else None)
        if tc_dir:
            for _fname in os.listdir(tc_dir):
                if _fname.endswith('Test.java'):
                    _m = re.match(r'^(.+?)_\d+_\d+Test\.java$', _fname)
                    if _m:
                        return _m.group(1)
                    return _fname.split('_')[0]
        return ''

    _target_class = _resolve_target_class_from_buggy(buggy, top_tests_dir)
'''

    ANCHOR = "    out_rows = []\n    total_files = len(test_files)"
    if "_resolve_target_class_from_buggy" not in src:
        if ANCHOR in src:
            src = src.replace(ANCHOR, INJECT_CODE + "\n    out_rows = []\n    total_files = len(test_files)")
            report.append("✅ PATCH 1: target_class inference injected")
        else:
            report.append("⚠️  PATCH 1 SKIPPED: anchor 'out_rows = []' not found — check indentation")
    else:
        report.append("ℹ️  PATCH 1 already applied")

    # ──────────────────────────────────────────────────────────────────────────
    # PATCH 2a: per-method path append (method-level loop)
    # old: [proj_prefix, full, m, bstat, fstat, ...]
    # new: [proj_prefix, _target_class, full, m, bstat, fstat, ...]
    # ──────────────────────────────────────────────────────────────────────────
    OLD_2A = ("out_rows.append([proj_prefix, full, m, bstat, fstat, "
              "'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])")
    NEW_2A = ("out_rows.append([proj_prefix, _target_class, full, m, bstat, fstat, "
              "'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])")
    if OLD_2A in src:
        src = src.replace(OLD_2A, NEW_2A)
        report.append("✅ PATCH 2a: per-method append updated (method-level path)")
    elif NEW_2A in src:
        report.append("ℹ️  PATCH 2a already applied")
    else:
        report.append("⚠️  PATCH 2a SKIPPED: method-level append anchor not found")

    # ──────────────────────────────────────────────────────────────────────────
    # PATCH 2b: class-fallback path append
    # old: [proj_name, full, '', bstat, fstat, ...]
    # new: [proj_name, _target_class, full, '', bstat, fstat, ...]
    # ──────────────────────────────────────────────────────────────────────────
    OLD_2B = ("out_rows.append([proj_name, full, '', bstat, fstat, "
              "'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])")
    NEW_2B = ("out_rows.append([proj_name, _target_class, full, '', bstat, fstat, "
              "'true' if is_bug_revealing else 'false', str(b_rc), str(f_rc), notes])")
    if OLD_2B in src:
        src = src.replace(OLD_2B, NEW_2B)
        report.append("✅ PATCH 2b: per-method append updated (class-fallback path)")
    elif NEW_2B in src:
        report.append("ℹ️  PATCH 2b already applied")
    else:
        report.append("⚠️  PATCH 2b SKIPPED: class-fallback append anchor not found")

    # ──────────────────────────────────────────────────────────────────────────
    # PATCH 3: notes 回写索引 [8] → [9] (因为 target_class 插入到第2列)
    # 只替换尚未更改的
    # ──────────────────────────────────────────────────────────────────────────
    if "out_rows[-1][8] = notes" in src:
        src = src.replace("out_rows[-1][8] = notes", "out_rows[-1][9] = notes")
        report.append("✅ PATCH 3: notes index updated [8]→[9]")
    else:
        report.append("ℹ️  PATCH 3 already applied or not needed")

    # ──────────────────────────────────────────────────────────────────────────
    # PATCH 4: CSV writerow 表头 + 类级汇总 CSV
    # ──────────────────────────────────────────────────────────────────────────
    # 匹配原始 writerow 行（带 target_class 之前的旧表头）
    OLD_HEADER = ("writer.writerow(['project_name','test_class','test_method','buggy_status',"
                  "'fixed_status','bug_revealing','buggy_rc','fixed_rc','notes'])")
    NEW_HEADER = ("writer.writerow(['project_name','target_class','test_class','test_method',\n"
                  "                         'buggy_status','fixed_status','bug_revealing','buggy_rc','fixed_rc','notes'])")

    CLASS_LEVEL_BLOCK = '''

    # ── 类级汇总 *_bugrevealing_class_level.csv ─────────────────────────────
    # Fields: project_name, target_class, test_class,
    #         total_methods, bug_revealing_methods,
    #         bug_revealing_rate, has_bug_revealing, br_score
    # br_score = bug_revealing_rate (0~1)，触发率越高得分越高，供综合评分使用
    try:
        class_level_out = os.path.splitext(args.out)[0] + '_class_level.csv'
        # out_rows 列序: 0=project_name, 1=target_class, 2=test_class,
        #                3=test_method, 4=buggy_status, 5=fixed_status,
        #                6=bug_revealing, 7=buggy_rc, 8=fixed_rc, 9=notes
        class_stats = {}   # key=(project_name, target_class, test_class)
        for _row in out_rows:
            if len(_row) < 7:
                continue
            _key = (_row[0], _row[1], _row[2])
            _br  = str(_row[6]).strip().lower() == 'true'
            if _key not in class_stats:
                class_stats[_key] = {'total': 0, 'revealing': 0}
            class_stats[_key]['total'] += 1
            if _br:
                class_stats[_key]['revealing'] += 1
        with open(class_level_out, 'w', newline='', encoding='utf-8') as _clf:
            _cl_writer = csv.writer(_clf)
            _cl_writer.writerow([
                'project_name', 'target_class', 'test_class',
                'total_methods', 'bug_revealing_methods',
                'bug_revealing_rate', 'has_bug_revealing', 'br_score',
            ])
            for (_proj_n, _tgt, _ts), _s in class_stats.items():
                _tot  = _s['total']
                _rev  = _s['revealing']
                _rate = round(_rev / _tot, 6) if _tot else 0.0
                _has  = 'true' if _rev > 0 else 'false'
                _br_score = _rate   # br_score 直接等于触发率
                _cl_writer.writerow([
                    _proj_n, _tgt, _ts,
                    _tot, _rev, _rate, _has, _br_score,
                ])
        print(f'Class-level summary: {os.path.abspath(class_level_out)}')
        if logfh:
            try:
                logfh.write(f'Class-level CSV: {os.path.abspath(class_level_out)}\\n')
            except Exception:
                pass
    except Exception as _cl_err:
        print(f'Warning: failed to write class-level CSV: {_cl_err}')'''

    if OLD_HEADER in src:
        src = src.replace(OLD_HEADER, NEW_HEADER)
        # 在 "for r in out_rows:\n            writer.writerow(r)" 之后插入类级汇总
        WRITE_LOOP_END = "        for r in out_rows:\n            writer.writerow(r)"
        if WRITE_LOOP_END in src and CLASS_LEVEL_BLOCK.strip()[:30] not in src:
            src = src.replace(WRITE_LOOP_END, WRITE_LOOP_END + CLASS_LEVEL_BLOCK)
            report.append("✅ PATCH 4: CSV header updated + class-level CSV block added")
        else:
            report.append("✅ PATCH 4a: CSV header updated (class-level block may already exist)")
    elif "'target_class','test_class','test_method'" in src:
        report.append("ℹ️  PATCH 4 already applied")
    else:
        report.append("⚠️  PATCH 4 SKIPPED: CSV header anchor not found")

    # ──────────────────────────────────────────────────────────────────────────
    # Write back
    # ──────────────────────────────────────────────────────────────────────────
    print("\n".join(report))
    if src != original:
        backup = path + '.bak'
        shutil.copy2(path, backup)
        print(f"\n  Backup saved: {backup}")
        with open(path, 'w', encoding='utf-8') as f:
            f.write(src)
        print(f"✅ Patched file written: {path}")
    else:
        print("\nℹ️  No changes written (all patches already applied or all skipped)")


if __name__ == '__main__':
    if len(sys.argv) < 2:
        print(f"Usage: python3 {sys.argv[0]} <path/to/bug_revealing.py>")
        sys.exit(1)
    patch(sys.argv[1])
