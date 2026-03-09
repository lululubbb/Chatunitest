import math
import shutil
from config import *
import os
import json
import psutil
import re
import tiktoken
import datetime
import configparser

enc = tiktoken.get_encoding("cl100k_base")
encoding = tiktoken.encoding_for_model("gpt-3.5-turbo")


# def get_messages_tokens(messages):
#     """
#     Get the tokens of messages.
#     :param messages: The messages.
#     :return: The tokens.
#     """
#     cnt = 0
#     for message in messages:
#         cnt += count_tokens(message["content"])
#     return cnt

def get_messages_tokens(messages):
    """
    估算消息列表的Token数（兼容OpenAI API的Token计算规则）
    :param messages: 格式为 [{"role": "...", "content": "..."}] 的消息列表
    :return: 估算的Token总数
    """
    # 基础格式校验
    if not isinstance(messages, list):
        return 0
    
    try:
        import tiktoken
        # 使用cl100k_base编码（适配gpt-3.5-turbo、gpt-4、llama3等模型）
        enc = tiktoken.get_encoding("cl100k_base")
        total = 0
        for msg in messages:
            # 跳过格式异常的消息
            if not isinstance(msg, dict) or "content" not in msg:
                continue
            # 计算单条消息的Token数（包含role的固定开销，更贴近真实API计算）
            # 注：OpenAI API实际计算会包含role字段的固定Token，这里可按需补充
            total += len(enc.encode(msg["content"]))
        return total
    except (ImportError, ModuleNotFoundError):
        # 降级方案：1 Token ≈ 4 个字符（经验值）
        total = 0
        for msg in messages:
            if not isinstance(msg, dict) or "content" not in msg:
                continue
            total += len(msg["content"]) // 4
        return total

def count_tokens(strings):
    tokens = encoding.encode(strings)
    cnt = len(tokens)
    return cnt


def find_processes_created_by(pid):
    """
    Find the process's and all subprocesses' pid
    """
    parent_process = psutil.Process(pid)
    child_processes = parent_process.children(recursive=True)
    pids = [process.pid for process in child_processes]
    return pids.append(pid)


def remove_imports(code):
    # Define the regular expression pattern
    pattern = r"^import.*;$\n"

    # Use re.sub to remove lines matching the pattern
    output_str = re.sub(pattern, "", code, flags=re.MULTILINE)

    return output_str


def get_latest_file(file_dir, rounds=None, suffix=None):
    """
    Get the latest file
    :param file_dir:
    :return:
    """
    all_files = os.listdir(file_dir)
    file_number = len([x for x in all_files if x.endswith(".json")])
    if not suffix:
        for file in all_files:
            if file.startswith(str(file_number) + "_"):
                return os.path.join(file_dir, file)
    else:
        if not rounds:
            rounds = math.floor(file_number / 3)
        for file in all_files:
            if file.endswith(suffix + "_" + str(rounds) + ".json"):
                return os.path.join(file_dir, file)
    return ""


def get_dataset_path(method_id, project_name, class_name, method_name, direction):
    """
    Get the dataset path
    :return:
    """
    if direction == "raw":
        return os.path.join(dataset_dir, "raw_data",
                            method_id + "%" + project_name + "%" + class_name + "%" + method_name + "%raw.json")
    return os.path.join(dataset_dir, "direction_" + str(direction),
                        method_id + "%" + project_name + "%" + class_name + "%" + method_name + "%d" + str(
                            direction) + ".json")

# 没有用到
def get_project_class_info(method_id, project_name, class_name, method_name):
    file_name = get_dataset_path(method_id, project_name, class_name, method_name, "raw")
    if os.path.exists(file_name):
        with open(file_name, "w") as f:
            raw_data = json.load(f)
        return raw_data["package"], raw_data["imports"]
    return None, None


def parse_file_name(filename):
    m_id, project_name, class_name, method_name, direction_and_test_num = filename.split('%')
    direction, test_num = direction_and_test_num.split('_')
    return m_id, project_name, class_name, method_name, direction, test_num.split('.')[0]


def parse_file_name(directory):
    dir_name = os.path.basename(directory)
    m_id, project_name, class_name, method_name, invalid = dir_name.split('%')
    return m_id, project_name, class_name, method_name


def get_raw_data(method_id, project_name, class_name, method_name):
    with open(get_dataset_path(method_id, project_name, class_name, method_name, "raw"), "r") as f:
        raw_data = json.load(f)
    return raw_data


def get_project_abspath():
    return os.path.abspath(project_dir)


def remove_single_test_output_dirs(project_path):
    prefix = "test_"

    # Get a list of all directories in the current directory with the prefix
    directories = [d for d in os.listdir(project_path) if os.path.isdir(d) and d.startswith(prefix)]

    # Iterate through the directories and delete them if they are not empty
    for d in directories:
        try:
            shutil.rmtree(d)
            print(f"Directory {d} deleted successfully.")
        except Exception as e:
            print(f"Error deleting directory {d}: {e}")

# 没有用到
def get_date_string(directory_name):
    try:
        # 原有逻辑：按%分割取第二个元素
        date_str = directory_name.split('%')[1]
        return date_str
    except IndexError:
        # 容错：无%时，返回目录名本身或当前时间，避免崩溃
        print(f"⚠️ 警告：目录名 {directory_name} 无%分隔符，无法提取日期！")
        # 方案1：返回目录名（保证排序不崩溃）
        return directory_name


def find_result_in_projects():
    """
    Find the new directory.
    :return: The new directory.
    """
    all_results = [x for x in os.listdir(project_dir) if '%' in x]
    all_results = sorted(all_results, key=get_date_string)
    return os.path.join(result_dir, all_results[-1])


def find_newest_result():
    """
    Find the newest directory.
    :return: The new directory.
    """
    config = configparser.ConfigParser()
    config.read("/home/chenlu/ChatUniTest_GPT3.5/config/config.ini")
    try:
        result_dir = config.get("DEFAULT", "result_dir")
    except configparser.NoSectionError:
        # 兜底：直接读取无分段的配置
        result_dir = config.get("", "result_dir")

    # 3. 检查结果目录是否存在
    if not os.path.exists(result_dir):
        raise Exception(f"结果目录不存在！路径：{result_dir}")

    # 4. 获取所有子目录
    all_results = [d for d in os.listdir(result_dir) if os.path.isdir(os.path.join(result_dir, d))]
    
    # 5. 空列表检查
    if not all_results:
        raise Exception(f"结果目录 {result_dir} 下无任何子目录！")
    
    # 6. 排序（兼容无%的目录名）
    all_results = sorted(all_results, key=get_date_string)
    newest_result = os.path.join(result_dir, all_results[-1])
    
    return newest_result

# 没有用到
def get_finished_project():
    projects = []
    all_directory = os.listdir(result_dir)
    for directory in all_directory:
        if directory.startswith("scope_test"):
            sub_dir = os.path.join(result_dir, directory)
            child_dir = ""
            for dir in os.listdir(sub_dir):
                if os.path.isdir(os.path.join(sub_dir, dir)):
                    child_dir = dir
                    break
            m_id, project_name, class_name, method_name = parse_file_name(child_dir)
            if project_name not in projects:
                projects.append(project_name)
    return projects

# 没有用到
def get_openai_content(content):
    """
    Get the content for OpenAI
    :param content:
    :return:
    """
    if not isinstance(content, dict):
        return ""
    return content["choices"][0]['message']["content"]

# 没有用到
def get_openai_message(content):
    """
    Get the content for OpenAI
    :param content:
    :return:
    """
    if not isinstance(content, dict):
        return ""
    return content["choices"][0]['message']


def check_java_version():
    java_home = os.environ.get('JAVA_HOME')
    if 'jdk-17' in java_home:
        return 17
    elif 'jdk-11' in java_home:
        return 11


def repair_package(code, package_info):
    """
    Repair package declaration in test.
    """
    first_line = code.split('import')[0]
    if package_info == '' or package_info in first_line:
        return code
    code = package_info + "\n" + code
    return code


# TODO: imports can be optimized
def repair_imports(code, imports):
    """
    Repair imports in test.
    """
    import_list = imports.split('\n')
    first_line, _code = code.split('\n', 1)
    for _import in reversed(import_list):
        if _import not in code:
            _code = _import + "\n" + _code
    return first_line + '\n' + _code

# 没有用到
def add_timeout(test_case, timeout=8000):
    """
    Add timeout to test cases. Only for Junit 5
    """
    # check junit version
    junit4 = 'import org.junit.Test'
    junit5 = 'import org.junit.jupiter.api.Test'
    if junit4 in test_case:  # Junit 4
        test_case = test_case.replace('@Test(', f'@Test(timeout = {timeout}, ')
        return test_case.replace('@Test\n', f'@Test(timeout = {timeout})\n')
    elif junit5 in test_case:  # Junit 5
        timeout_import = 'import org.junit.jupiter.api.Timeout;'
        test_case = repair_imports(test_case, timeout_import)
        return test_case.replace('@Test\n', f'@Test\n    @Timeout({timeout})\n')
    else:
        print("Can not know which junit version!")
        return test_case


def export_method_test_case(output, class_name, m_id, test_num, method_test_case):
    """
    Export test case to file.
    output : pathto/project/testcase.java
    """
    method_test_case = add_timeout(method_test_case)
    f = os.path.join(output, class_name + "_" + str(m_id) + '_' + str(test_num) + "Test.java")
    if not os.path.exists(output):
        os.makedirs(output)
    with open(f, "w") as output_file:
        output_file.write(method_test_case)


def change_class_name(test_case, class_name, m_id, test_num):
    """
    Change the class name in the test_case by given m_id.
    """
    old_name = class_name + 'Test'
    new_name = class_name + '_' + str(m_id) + '_' + str(test_num) + 'Test'
    return test_case.replace(old_name, new_name, 1)

# 没有用到
def get_current_time():
    """
    Get current time
    :return:
    """
    current_time = datetime.datetime.now()
    formatted_time = current_time.strftime("%H:%M:%S")
    return formatted_time
