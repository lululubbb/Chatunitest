#!/usr/bin/env python3
"""Compute tree similarities (top-down, bottom-up, combined) on AST CSV outputs.

Usage:
  python3 measure_similarity.py /path/to/tests_dir

Reads AST CSV at tests_dir/AST/<proj>_AST.csv and writes
  tests_dir/Similarity/<proj>_Sims.csv
  tests_dir/Similarity/<proj>_bigSims.csv

Grouping: only compare test cases belonging to the same method group, identified
by the first numeric token in the test file name (e.g., _1_ in Foo_1_2).
"""
import os
import sys
import csv
import html
import re
from collections import defaultdict
from typing import List, Tuple, Dict

class Node:
    _id_counter = 0
    def __init__(self, label):
        self.label = label
        self.children = []
        self.parent = None
        self.id = Node._id_counter
        Node._id_counter += 1
        # filled later
        self.subtree_nodes = None
        self.subtree_size = None
        self.signature = None

def build_tree_from_element(elem) -> Node:
    node = Node(elem.tag)
    for child in list(elem):
        child_node = build_tree_from_element(child)
        child_node.parent = node
        node.children.append(child_node)
    return node

def compute_subtree_info(root: Node):
    # post-order
    nodes = []
    def dfs(n):
        for c in n.children:
            dfs(c)
        nodes.append(n)
    dfs(root)
    for n in nodes:
        # collect nodes in subtree
        s = set([n.id])
        for c in n.children:
            s |= c.subtree_nodes
        n.subtree_nodes = s
        n.subtree_size = len(s)
        # signature: label + '(' + ','.join(child.signatures) + ')'
        child_sigs = [c.signature for c in n.children]
        n.signature = n.label + '(' + ','.join(child_sigs) + ')'

# Top-down maximum ordered common subtree: recursively compute size of best match rooted at (a,b)
def topdown_size(a: Node, b: Node, memo: Dict[Tuple[int,int], int]) -> int:
    key = (a.id, b.id)
    if key in memo:
        return memo[key]
    if a.label != b.label:
        memo[key] = 0
        return 0
    m = len(a.children)
    n = len(b.children)
    # dp where weight of matching ai with bj is topdown_size(ai,bj)
    dp = [[0]*(n+1) for _ in range(m+1)]
    for i in range(m-1, -1, -1):
        for j in range(n-1, -1, -1):
            # option skip a[i] or skip b[j]
            v1 = dp[i+1][j]
            v2 = dp[i][j+1]
            match = topdown_size(a.children[i], b.children[j], memo)
            v3 = dp[i+1][j+1] + match if match>0 else dp[i+1][j+1]
            dp[i][j] = max(v1, v2, v3)
    res = 1 + dp[0][0]
    memo[key] = res
    return res

# reconstruct matched node ids for topdown
def topdown_match(a: Node, b: Node, memo: Dict[Tuple[int,int], int]) -> Tuple[set,set]:
    matched_a = set()
    matched_b = set()
    if a.label != b.label:
        return matched_a, matched_b
    # include roots
    matched_a.add(a.id)
    matched_b.add(b.id)
    m = len(a.children)
    n = len(b.children)
    # compute dp table of sizes
    dp = [[0]*(n+1) for _ in range(m+1)]
    for i in range(m-1, -1, -1):
        for j in range(n-1, -1, -1):
            v1 = dp[i+1][j]
            v2 = dp[i][j+1]
            match = topdown_size(a.children[i], b.children[j], memo)
            v3 = dp[i+1][j+1] + match if match>0 else dp[i+1][j+1]
            dp[i][j] = max(v1, v2, v3)
    # backtrack
    i = 0; j = 0
    while i < m and j < n:
        if dp[i][j] == dp[i+1][j]:
            i += 1
        elif dp[i][j] == dp[i][j+1]:
            j += 1
        else:
            # matched children
            if topdown_size(a.children[i], b.children[j], memo) > 0:
                sub_a, sub_b = topdown_match(a.children[i], b.children[j], memo)
                matched_a |= sub_a
                matched_b |= sub_b
                i += 1
                j += 1
            else:
                i += 1
    return matched_a, matched_b

# Bottom-up matching: greedy by signature and descending subtree size, avoid overlapping
def bottomup_match(root1: Node, root2: Node) -> Tuple[set,set,int]:
    sig_map1 = defaultdict(list)
    sig_map2 = defaultdict(list)
    all_nodes1 = []
    all_nodes2 = []
    def collect(n, lst, sigmap):
        lst.append(n)
        sigmap[n.signature].append(n)
        for c in n.children:
            collect(c, lst, sigmap)
    collect(root1, all_nodes1, sig_map1)
    collect(root2, all_nodes2, sig_map2)
    used1 = set()
    used2 = set()
    matched_nodes_count = 0
    matched_ids1 = set()
    matched_ids2 = set()
    # process signatures that exist in both
    for sig in sig_map1.keys():
        if sig not in sig_map2:
            continue
        list1 = sorted(sig_map1[sig], key=lambda x: x.subtree_size, reverse=True)
        list2 = sorted(sig_map2[sig], key=lambda x: x.subtree_size, reverse=True)
        i = 0; j = 0
        while i < len(list1) and j < len(list2):
            n1 = list1[i]
            n2 = list2[j]
            # ensure these subtrees don't overlap with already used nodes
            if n1.subtree_nodes & used1:
                i += 1; continue
            if n2.subtree_nodes & used2:
                j += 1; continue
            # match them
            matched_nodes_count += n1.subtree_size
            matched_ids1 |= n1.subtree_nodes
            matched_ids2 |= n2.subtree_nodes
            used1 |= n1.subtree_nodes
            used2 |= n2.subtree_nodes
            i += 1; j += 1
    return matched_ids1, matched_ids2, matched_nodes_count

# helper to parse AST CSV and build trees
def read_ast_csv(ast_csv_path: str) -> Dict[str,str]:
    # returns mapping test_filename -> ast_xml_string
    # collect possibly multiple AST fragments per test file
    res = defaultdict(list)
    if not os.path.exists(ast_csv_path):
        return res
    with open(ast_csv_path, 'r', encoding='utf-8') as f:
        reader = csv.reader(f)
        header = next(reader, None)
        for row in reader:
            if len(row) < 2:
                continue
            # first column like TestFile.method
            name = row[0].strip().strip('"')
            # test file name is portion before first '.'
            if '.' in name:
                filepart = name.split('.',1)[0]
            else:
                filepart = name
            ast_escaped = row[1]
            # unescape entities
            ast_xml = html.unescape(ast_escaped)
            # if AST stored wrapped in quotes and extra quotes, strip
            ast_xml = ast_xml.strip()
            res[filepart].append(ast_xml)
    # convert lists to concatenated XML fragments (caller will wrap if needed)
    return res

# parse XML string to Node tree
import xml.etree.ElementTree as ET

def xml_to_tree(xml_str: str) -> Node:
    try:
        root_elem = ET.fromstring(xml_str)
    except Exception:
        # try wrap
        try:
            root_elem = ET.fromstring('<ROOT>' + xml_str + '</ROOT>')
        except Exception:
            return None
    return build_tree_from_element(root_elem)

def group_by_method(test_names: List[str]) -> Dict[str, List[str]]:
    groups = defaultdict(list)
    for tn in test_names:
        m = re.search(r'_(\d+)_', tn)  # 匹配_数字_的格式，确保提取的是第一个数字段
        if m:
            key = m.group(1)
        else:
            # 兼容无下划线的情况
            m2 = re.search(r'(\d+)', tn)
            key = m2.group(1) if m2 else 'default'
        groups[key].append(tn)
    
    return groups

def extract_target_class_from_test_names(test_names: List[str]) -> str:
    """
    Extract target_class name from test file names.
    Test file name format: ClassName_methodId_testNum or ClassName_methodId
    Example: ExtendedBufferedReader_7_3Test -> ExtendedBufferedReader
    """
    if not test_names:
        return ''
    # Get first test name and extract class name (everything before first _digit_)
    first_name = test_names[0]
    # Remove 'Test' suffix if present
    if first_name.endswith('Test'):
        first_name = first_name[:-4]
    # Split by underscore and take the part before numeric tokens
    parts = first_name.split('_')
    if parts:
        # Take everything before the first numeric part
        target_class = ''
        for part in parts:
            if part.isdigit():
                break
            if target_class:
                target_class += '_' + part
            else:
                target_class = part
        return target_class
    return ''

def process_tests_dir(tests_dir: str):
    tests_dir = os.path.abspath(tests_dir)
    # allow either test_cases dir or top tests dir
    if os.path.basename(tests_dir) == 'test_cases':
        top_tests_dir = os.path.dirname(tests_dir)
        tc_dir = tests_dir
    elif os.path.basename(tests_dir).startswith('tests'):
        top_tests_dir = tests_dir
        if os.path.isdir(os.path.join(tests_dir,'test_cases')):
            tc_dir = os.path.join(tests_dir,'test_cases')
        else:
            tc_dir = tests_dir
    else:
        top_tests_dir = os.path.dirname(tests_dir)
        if os.path.isdir(os.path.join(top_tests_dir,'test_cases')):
            tc_dir = os.path.join(top_tests_dir,'test_cases')
        else:
            tc_dir = tests_dir
    project_dir = os.path.dirname(top_tests_dir)
    proj_prefix = os.path.basename(project_dir)
    # normalized short project prefix (drop trailing _b or _f)
    proj_short = re.sub(r'(_b|_f)$', '', proj_prefix)
    ast_dir = os.path.join(top_tests_dir, 'AST')
    # prefer normalized short prefix, but accept legacy prefixed filename
    ast_csv_short = os.path.join(ast_dir, f'{proj_short}_AST.csv')
    ast_csv_legacy = os.path.join(ast_dir, f'{proj_prefix}_AST.csv')
    if os.path.exists(ast_csv_short):
        ast_csv = ast_csv_short
    elif os.path.exists(ast_csv_legacy):
        ast_csv = ast_csv_legacy
    else:
        print('AST CSV not found (tried):', ast_csv_short, 'and', ast_csv_legacy)
        return
    mapping = read_ast_csv(ast_csv)
    if not mapping:
        print('No ASTs parsed from', ast_csv)
        return
    # build trees
    trees = {}
    for fname, xml_list in mapping.items():
        # combine multiple AST fragments for the same test file into one XML root
        combined = None
        if isinstance(xml_list, list):
            combined_xml = '<ROOT>' + ''.join(xml_list) + '</ROOT>'
        else:
            combined_xml = '<ROOT>' + xml_list + '</ROOT>'
        node = xml_to_tree(combined_xml)
        if node is None:
            continue
        # compute subtree info
        compute_subtree_info(node)
        trees[fname] = node
    
    # Extract target_class from test names
    target_class = extract_target_class_from_test_names(list(trees.keys()))
    
    # group by method numeric token
    groups = group_by_method(list(trees.keys()))
    # prepare output dir
    sim_dir = os.path.join(top_tests_dir, 'Similarity')
    os.makedirs(sim_dir, exist_ok=True)
    sims_csv = os.path.join(sim_dir, f'{proj_short}_Sims.csv')
    # Include target_class in bigSims filename if available
    if target_class:
        big_csv = os.path.join(sim_dir, f'{proj_short}_target_{target_class}_bigSims.csv')
        bigsum_csv = os.path.join(sim_dir, f'{proj_short}_target_{target_class}_bigSimssum.csv')
    else:
        big_csv = os.path.join(sim_dir, f'{proj_short}_bigSims.csv')
        bigsum_csv = os.path.join(sim_dir, f'{proj_short}_bigSimssum.csv')
    # write header for sims (add project column)
    with open(sims_csv, 'w', newline='', encoding='utf-8') as sf:
        w = csv.writer(sf)
        w.writerow(['project','target_class','test_case_1','test_case_2','topdown_subtree_size','topdown_similarity','bottomup_subtree_size','bottomup_similarity','combined_subtree_size','combined_similarity','redundancy_score'])
        # will also collect per-test best
        best_map = {}
        for key, members in groups.items():
            # compare all pairs
            for i in range(len(members)):
                for j in range(i+1, len(members)):
                    a_name = members[i]
                    b_name = members[j]
                    a = trees.get(a_name)
                    b = trees.get(b_name)
                    if a is None or b is None:
                        continue
                    size1 = a.subtree_size
                    size2 = b.subtree_size
                    # topdown
                    memo = {}
                    td_size = topdown_size(a, b, memo) - 1  # subtract root counted twice? Actually topdown_size returns nodes per tree including root: we want matched_nodes_per_tree=k: topdown_size returns 1+sum matching children => nodes in root subtree matched including root
                    # But earlier we used topdown_size to return number of nodes in matched subtree per tree
                    td_nodes = td_size
                    if td_nodes < 0:
                        td_nodes = 0
                    td_sim = (2.0 * td_nodes) / (size1 + size2) if (size1 + size2) > 0 else 0.0
                    # topdown matched ids
                    td_ids_a, td_ids_b = topdown_match(a, b, memo)
                    # bottomup
                    bu_ids_a, bu_ids_b, bu_nodes = bottomup_match(a, b)
                    bu_nodes = bu_nodes
                    bu_sim = (2.0 * bu_nodes) / (size1 + size2) if (size1 + size2) > 0 else 0.0
                    # combined: union of matched nodes per tree
                    union_a = set(td_ids_a) | set(bu_ids_a)
                    union_b = set(td_ids_b) | set(bu_ids_b)
                    # use average per-tree matched nodes (should be equal) => compute nodes_per_tree = (len(union_a) + len(union_b))/2
                    nodes_per_tree = int(round((len(union_a) + len(union_b)) / 2.0))
                    comb_sim = (2.0 * nodes_per_tree) / (size1 + size2) if (size1 + size2) > 0 else 0.0
                    redundancy = 1.0 - comb_sim
                    w.writerow([proj_short, target_class, a_name, b_name, td_nodes, f'{td_sim:.6f}', bu_nodes, f'{bu_sim:.6f}', nodes_per_tree, f'{comb_sim:.6f}', f'{redundancy:.6f}'])
                    # update best
                    for src, dst, val in [(a_name, b_name, comb_sim), (b_name, a_name, comb_sim)]:
                        prev = best_map.get(src)
                        if prev is None or val > prev[2]:
                            best_map[src] = (src, dst, nodes_per_tree, comb_sim)
    # write bigSims
    with open(big_csv, 'w', newline='', encoding='utf-8') as bf:
        w = csv.writer(bf)
        w.writerow(['project','target_class','test_case_1','test_case_2','combined_subtree_size','combined_similarity','redundancy_score'])
        for tc, rec in best_map.items():
            # rec is (src, dst, nodes_per_tree, comb_sim)
            redundancy = 1.0 - rec[3]
            w.writerow([proj_short, target_class, rec[0], rec[1], rec[2], f'{rec[3]:.6f}', f'{redundancy:.6f}'])
    # compute bigSimssum: sum-of-squares and mean-of-squares over best combined similarities
    vals = [rec[3] for rec in best_map.values()]
    n = len(vals)
    sumsq = sum([v*v for v in vals]) if n>0 else 0.0
    meansq = (sumsq / n) if n>0 else 0.0
    with open(bigsum_csv, 'w', newline='', encoding='utf-8') as bs:
        w = csv.writer(bs)
        w.writerow(['project','n_tests','sum_of_squares','mean_of_squares'])
        w.writerow([proj_short, n, f'{sumsq:.6f}', f'{meansq:.6f}'])
    print('Wrote similarity CSVs to', sim_dir)

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print('Usage: measure_similarity.py /path/to/tests_dir')
        sys.exit(1)
    process_tests_dir(sys.argv[1])
