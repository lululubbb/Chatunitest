#!/usr/bin/env python3
"""辅助脚本：解析 JUnit ConsoleLauncher 输出与 JaCoCo XML 报告

用法示例：
  python3 stats_utils.py parse_failed /tmp/project_Test_out.log
  python3 stats_utils.py parse_jacoco /path/to/jacoco.xml

输出：
  parse_failed -> 整数 (失败测试数)
  parse_jacoco -> 四个整数，依次为: LINE_COVERED LINE_MISSED BRANCH_COVERED BRANCH_MISSED
"""

import argparse
import re
import sys
import os
import json
import xml.etree.ElementTree as ET


def parse_failed_file(path):
    try:
        text = open(path, 'r', errors='ignore').read()
    except Exception:
        print(0)
        return

    # 1) 匹配 [ 1 tests failed ] 之类的模式
    m = re.search(r"\[\s*(\d+)\s+tests failed\s*\]", text, re.I)
    if m:
        print(int(m.group(1)))
        return

    # 2) 匹配 "Tests run: 2, Failures: 1, Errors: 0" 这类
    m = re.search(r"Tests run:.*?Failures:\s*(\d+)", text)
    if m:
        failures = int(m.group(1))
        m2 = re.search(r"Errors:\s*(\d+)", text)
        errors = int(m2.group(1)) if m2 else 0
        print(failures + errors)
        return

    # 3) 匹配 "There were 1 failures" 等
    m = re.search(r"There (?:was|were)\s+(\d+)\s+failures?", text, re.I)
    if m:
        print(int(m.group(1)))
        return

    # 4) 统计 FAIL / FAILED 标记作为保守估计（可选）
    m = re.search(r"FAILED\b", text)
    if m:
        # 无法确定数量，返回1作为保守估计
        print(1)
        return

    print(0)


def parse_jacoco_xml(path):
    try:
        tree = ET.parse(path)
        root = tree.getroot()
    except Exception:
        # 输出四个0以便脚本兜底
        print("0 0 0 0")
        return

    line_cov = line_missed = branch_cov = branch_missed = 0
    # JaCoCo 生成的 counter 元素形如: <counter type="LINE" missed="10" covered="90"/>
    for c in root.findall('.//counter'):
        t = c.get('type')
        if t == 'LINE':
            line_cov = int(c.get('covered', '0'))
            line_missed = int(c.get('missed', '0'))
        elif t == 'BRANCH':
            branch_cov = int(c.get('covered', '0'))
            branch_missed = int(c.get('missed', '0'))

    print(f"{line_cov} {line_missed} {branch_cov} {branch_missed}")


def parse_jacoco_summary(path, to_json=False):
    """输出：
    line_covered line_missed line_total line_rate branch_covered branch_missed branch_total branch_rate
    同时可选地把汇总写入同目录下的 coverage_summary.json
    """
    try:
        tree = ET.parse(path)
        root = tree.getroot()
    except Exception:
        print("0 0 0 0 0.00 0 0 0 0.00")
        return

    line_cov = line_missed = branch_cov = branch_missed = 0
    for c in root.findall('.//counter'):
        t = c.get('type')
        if t == 'LINE':
            line_cov = int(c.get('covered', '0'))
            line_missed = int(c.get('missed', '0'))
        elif t == 'BRANCH':
            branch_cov = int(c.get('covered', '0'))
            branch_missed = int(c.get('missed', '0'))

    line_total = line_cov + line_missed
    branch_total = branch_cov + branch_missed
    line_rate = 0.0
    branch_rate = 0.0
    if line_total > 0:
        line_rate = (line_cov / line_total) * 100
    if branch_total > 0:
        branch_rate = (branch_cov / branch_total) * 100

    if to_json:
        import json
        outp = {
            "line_covered": line_cov,
            "line_missed": line_missed,
            "line_total": line_total,
            "line_rate": f"{line_rate:.2f}",
            "branch_covered": branch_cov,
            "branch_missed": branch_missed,
            "branch_total": branch_total,
            "branch_rate": f"{branch_rate:.2f}",
        }
        json_path = os.path.splitext(path)[0] + "_summary.json"
        try:
            with open(json_path, 'w') as jf:
                json.dump(outp, jf)
        except Exception:
            pass

    print(f"{line_cov} {line_missed} {line_total} {line_rate:.2f} {branch_cov} {branch_missed} {branch_total} {branch_rate:.2f}")


def parse_class_summary(path, class_name, to_json=False):
    """查找 jacoco.xml 中与 class_name 匹配的 <class> 元素并返回行/分支覆盖数量/总量/百分比。

    class_name 支持以下形式：
      - 简单名: CSVFormat
      - 包+类: org.apache.commons.csv.CSVFormat
      - 斜杠形式: org/apache/commons/csv/CSVFormat
    """
    try:
        tree = ET.parse(path)
        root = tree.getroot()
    except Exception:
        print("0 0 0 0.00 0 0 0 0.00")
        return

    # 规范化输入
    simple = class_name.split('.')[-1]
    slash_form = class_name.replace('.', '/')

    # 收集所有可能匹配的 class 节点，以便优先级选择（避免首个匹配就是内部类）
    exact_match = None       # name == slash_form
    outer_match = None       # name endswith '/simple' and no '$'
    src_outer = None         # sourcefilename == simple.java and no '$'
    src_any = None           # first class with sourcefilename == simple.java
    any_match = None         # name endswith simple (包括内部类)

    for cls in root.findall('.//class'):
        name = cls.get('name') or ''
        src = cls.get('sourcefilename') or ''
        if name == slash_form:
            exact_match = cls
            break
        if name.endswith('/' + simple) and ('$' not in name):
            outer_match = cls
        if src == simple + '.java' and ('$' not in name):
            src_outer = cls
        if src == simple + '.java' and src_any is None:
            src_any = cls
        if name.endswith(simple) and any_match is None:
            any_match = cls

    candidate_cls = exact_match or outer_match or src_outer or src_any or any_match

    if candidate_cls is None:
        # 未找到 class，返回0
        print("0 0 0 0.00 0 0 0 0.00")
        return

    # 找到后提取 counter（优先使用类末尾出现的 counter 节点）
    line_cov = line_missed = branch_cov = branch_missed = 0
    # 有些 jacoco 输出会在 method 后再有 class-level counters；这里先尝试读取类级别的 counter 元素
    # 如果没有，再尝试从最后出现的 counter 中抓取 LINE/BRANCH
    for c in candidate_cls.findall('counter'):
        t = c.get('type')
        if t == 'LINE':
            line_cov = int(c.get('covered', '0'))
            line_missed = int(c.get('missed', '0'))
        elif t == 'BRANCH':
            branch_cov = int(c.get('covered', '0'))
            branch_missed = int(c.get('missed', '0'))
    # 如果没有直接的类级 LINE/BRANCH（例如 counters 仅在方法中），尝试取该 class 节点最后出现的 counter 列表
    if (line_cov == 0 and line_missed == 0) and (branch_cov == 0 and branch_missed == 0):
        # 查找 class 子树中最后出现的 counter 节点并用其类型值覆盖
        last_counters = candidate_cls.findall('.//counter')
        if last_counters:
            # 从后往前取第一个出现的 LINE/BRANCH
            for c in reversed(last_counters):
                t = c.get('type')
                if t == 'LINE' and (line_cov == 0 and line_missed == 0):
                    line_cov = int(c.get('covered', '0'))
                    line_missed = int(c.get('missed', '0'))
                if t == 'BRANCH' and (branch_cov == 0 and branch_missed == 0):
                    branch_cov = int(c.get('covered', '0'))
                    branch_missed = int(c.get('missed', '0'))
    found = {
        'line_cov': line_cov,
        'line_missed': line_missed,
        'branch_cov': branch_cov,
        'branch_missed': branch_missed,
    }

    if not found:
        # 未找到 class，返回0
        print("0 0 0 0.00 0 0 0 0.00")
        return

    line_total = found['line_cov'] + found['line_missed']
    branch_total = found['branch_cov'] + found['branch_missed']
    line_rate = (found['line_cov'] / line_total * 100) if line_total > 0 else 0.0
    branch_rate = (found['branch_cov'] / branch_total * 100) if branch_total > 0 else 0.0

    if to_json:
        outp = {
            "class": class_name,
            "line_covered": found['line_cov'],
            "line_missed": found['line_missed'],
            "line_total": line_total,
            "line_rate": f"{line_rate:.2f}",
            "branch_covered": found['branch_cov'],
            "branch_missed": found['branch_missed'],
            "branch_total": branch_total,
            "branch_rate": f"{branch_rate:.2f}",
        }
        json_path = os.path.splitext(path)[0] + "_" + simple + "_summary.json"
        try:
            with open(json_path, 'w') as jf:
                json.dump(outp, jf)
        except Exception:
            pass

    print(f"{found['line_cov']} {found['line_missed']} {line_total} {line_rate:.2f} {found['branch_cov']} {found['branch_missed']} {branch_total} {branch_rate:.2f}")


def main():
    parser = argparse.ArgumentParser()
    sub = parser.add_subparsers(dest='cmd')

    p1 = sub.add_parser('parse_failed', help='解析 JUnit ConsoleLauncher 输出，返回失败测试数')
    p1.add_argument('file')

    p2 = sub.add_parser('parse_jacoco', help='解析 jacoco.xml，返回 covered/missed counts')
    p2.add_argument('file')

    p3 = sub.add_parser('parse_jacoco_summary', help='解析 jacoco.xml，返回覆盖数量/总量/百分比，附加可写 summary json')
    p3.add_argument('file')
    p3.add_argument('--json', action='store_true', help='同时写出 summary json 到同目录')

    p4 = sub.add_parser('parse_class_summary', help='解析 jacoco.xml，返回指定类的覆盖数量/总量/百分比，附加可写 summary json')
    p4.add_argument('file')
    p4.add_argument('class_name', help='类名或全限定名（支持 CSVFormat 或 org.apache.commons.csv.CSVFormat）')
    p4.add_argument('--json', action='store_true', help='同时写出 summary json 到同目录')

    args = parser.parse_args()

    if args.cmd == 'parse_failed':
        parse_failed_file(args.file)
    elif args.cmd == 'parse_jacoco':
        parse_jacoco_xml(args.file)
    elif args.cmd == 'parse_jacoco_summary':
        parse_jacoco_summary(args.file, to_json=args.json)
    elif args.cmd == 'parse_class_summary':
        parse_class_summary(args.file, args.class_name, to_json=args.json)
    elif args.cmd == 'find_class':
        # 输出匹配到的 class name（slash form），若未找到则输出空行
        def find_class(path, class_name):
            try:
                tree = ET.parse(path)
                root = tree.getroot()
            except Exception:
                print("")
                return
            simple = class_name.split('.')[-1]
            slash_form = class_name.replace('.', '/')
            exact_match = None
            outer_match = None
            src_outer = None
            src_any = None
            any_match = None
            for cls in root.findall('.//class'):
                name = cls.get('name') or ''
                src = cls.get('sourcefilename') or ''
                if name == slash_form:
                    exact_match = name
                    break
                if name.endswith('/' + simple) and ('$' not in name):
                    outer_match = name
                if src == simple + '.java' and ('$' not in name):
                    src_outer = name
                if src == simple + '.java' and src_any is None:
                    src_any = name
                if name.endswith(simple) and any_match is None:
                    any_match = name
            candidate = exact_match or outer_match or src_outer or src_any or any_match
            print(candidate or "")
        find_class(args.file, args.class_name)
    else:
        parser.print_help()


if __name__ == '__main__':
    main()
