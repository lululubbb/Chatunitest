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
    # ========== 第一步：初始化路径和进度文件 ==========
    project_name = os.path.basename(os.path.normpath(project_dir))
    defect_result_dir = f"/home/chenlu/ChatUniTest_GPT3.5/results_batch/{project_name}"
    progress_file = f"{defect_result_dir}/progress.json"
    
    # ========== 核心修复：无论文件是否存在，都初始化progress为空字典 ==========
    progress = {}  # 先定义默认值，避免未定义报错
    if os.path.exists(progress_file):
        with open(progress_file, 'r') as f:
            progress = json.load(f)  # 文件存在时覆盖默认值

    # ========== 第二步：原有核心逻辑 + 断点续跑 ==========
    # 1. 解析项目（断点续跑：未完成才执行）
    if progress.get("parse") != "success":
        print("📌 开始解析项目...")
        info_path = Task.parse(project_dir)
        parse_data(info_path)  # 解析后入库
        save_progress(progress_file, "parse", "success")
        print("✅ 解析步骤完成")
    else:
        print("⚠️ 解析步骤已完成，跳过！")
        info_path = None

    # 2. 导出数据（断点续跑：未完成才执行）
    if progress.get("export") != "success":  # 现在progress一定有定义
        print("📌 开始导出数据...")
        export_data()
        save_progress(progress_file, "export", "success")
        print("✅ 导出步骤完成")
    else:
        print("⚠️ 导出步骤已完成，跳过！")

  

    # Parse project
    # info_path = Task.parse(project_dir)

    # Parse data
    # parse_data(info_path)

    # clear last dataset
    # clear_dataset()

    # Export data for multi-process
    # export_data()

    # project_name = os.path.basename(os.path.normpath(project_dir))

    # Modify SQL query to test the designated classes.
    sql_query = """
        SELECT id FROM method WHERE project_name='{}';
    """.format(project_name)

    # Start the whole process
    # start_generation(sql_query, multiprocess=False, repair=True, confirmed=False)
    # start_generation(sql_query, multiprocess=True, repair=True, confirmed=False)
    # 4. 生成测试用例（断点续跑：未完成才执行）
    if progress.get("generate") != "success":
        print("📌 开始生成测试用例...")
        start_generation(sql_query, multiprocess=True, repair=True, confirmed=True)
        save_progress(progress_file, "generate", "success")
        print("✅ 生成测试用例完成")
    else:
        print("⚠️ 生成测试用例步骤已完成，跳过！")

    # Export the result
    print("📌 开始分析结果...")
    result_analysis()
    print("✅ 结果分析完成")


if __name__ == '__main__':
    print("Make sure the config.ini is correctly configured.")
    seconds = 1
    while seconds > 0:
        print(seconds)
        time.sleep(1)  # Pause for 1 second
        seconds -= 1
    run()
