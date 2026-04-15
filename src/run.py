import os.path
import time
import json
from tools import *
from database import *
from parse_data import parse_data
from export_data import export_data
from scope_test import start_generation
from parse_xml import result_analysis
from task import Task
import subprocess
import sys
import glob
import os


def clear_dataset():
    """
    Clear the dataset folder.
    :return: None
    """
    # Delete the dataset folder
    if os.path.exists(dataset_dir):
        shutil.rmtree(dataset_dir)

# 定义进度记录文件
defect_name = os.path.basename(os.path.normpath(project_dir))
progress_file = f"/home/chenlu/ChatUniTest_GPT3.5/results_batch/{defect_name}/progress.json"

def save_progress(progress_file, step, status):
    """保存进度：独立函数，避免路径依赖"""
    progress_dir = os.path.dirname(progress_file)
    if not os.path.exists(progress_dir):
        os.makedirs(progress_dir)
    progress = {}
    if os.path.exists(progress_file):
        with open(progress_file, 'r') as f:
            progress = json.load(f)
    progress[step] = status
    with open(progress_file, 'w') as f:
        json.dump(progress, f, indent=2)





def run():
    """
    Generate the test cases with one-click.
    :return: None
    """
    print("\n📌 第一步：数据库初始化与调试")
    # Delete history data
    print("\n📌 第二步：清空历史数据")
    drop_table()

    # Create the table
    create_table()

    project_name = os.path.basename(os.path.normpath(project_dir))
    
    # --- 强制执行解析和入库，确保数据库有数据 ---
    print(f"📌 开始解析项目: {project_name}")
    info_path = Task.parse(project_dir)
    parse_data(info_path) 
    
    print("📌 开始导出数据...")
    export_data()

    # --- 构造查询语句 ---
    sql_query = f"SELECT id FROM method WHERE project_name='{project_name}';"

    print("📌 开始生成测试用例...")
    start_generation(sql_query, multiprocess=True, repair=True, confirmed=True)

    print("📌 开始分析结果...")
    result_analysis()

    # 生成后：自动运行 bug_revealing 与 similarity（针对当前 project）
    try:
        # project_dir 在 config.py 中定义，指向当前处理的 defects4j 单个项目（可能带 _b 后缀）
        proj = os.path.abspath(project_dir)
        # 找到该项目下最新的 tests%* 目录（如果存在）
        tests_dirs = sorted(glob.glob(os.path.join(proj, 'tests%*')))
        tests_dir = tests_dirs[-1] if tests_dirs else None

        # 1) 运行 bug_revealing（使用 src/run_bug_revealing.py，传入项目路径以批量处理）
        rb = [sys.executable, os.path.join(os.path.dirname(__file__), 'run_bug_revealing.py'), proj]
        print(f"📌 运行 bug_revealing: {' '.join(rb)}")
        subprocess.run(rb, check=False)

        # 2) 运行 code_to_ast + measure_similarity（对最新 tests_dir）
        if tests_dir and os.path.isdir(tests_dir):
            code_to_ast = [sys.executable, os.path.join(os.path.dirname(__file__), 'scripts', 'code_to_ast.py'), tests_dir]
            print(f"📌 运行 code_to_ast: {' '.join(code_to_ast)}")
            subprocess.run(code_to_ast, check=False)

            measure_sim = [sys.executable, os.path.join(os.path.dirname(__file__), 'scripts', 'measure_similarity.py'), tests_dir]
            print(f"📌 运行 measure_similarity: {' '.join(measure_sim)}")
            subprocess.run(measure_sim, check=False)
        else:
            print(f"⚠️ 未找到 tests 目录，跳过 similarity 计算: {proj}")
    except Exception as e:
        print(f"⚠️ 自动运行 bug_revealing/similarity 失败: {e}")


if __name__ == '__main__':
    print("Make sure the config.ini is correctly configured.")
    seconds = 1
    while seconds > 0:
        print(seconds)
        time.sleep(1)  # Pause for 1 second
        seconds -= 1
    run()
