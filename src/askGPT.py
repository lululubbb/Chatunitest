import copy
import os.path
import time
import openai
import requests
from tools import *
import random
import concurrent.futures
import javalang
import jinja2
from colorama import Fore, Style, init
from task import Task
import json
from openai import OpenAI

from llm_stats_tracker import LLMStatsTracker

init()
env = jinja2.Environment(loader=jinja2.FileSystemLoader('../prompt'))


def ask_chatgpt(messages, save_path):
    """
    Send messages to GPT, save response, and return (success, llm_elapsed_seconds).

    Returns
    -------
    success : bool
    llm_elapsed : float   wall-clock seconds of the LLM API call only
    """
    if get_messages_tokens(messages) > MAX_PROMPT_TOKENS:
        llm_elapsed = 0.0
        completion = {
            "choices": [
                {
                    "message": {
                        "role": "assistant",
                        "content": "Max prompt token exceeded, request aborted."
                    },
                    "finish_reason": "length"
                }
            ],
            "usage": {
                "prompt_tokens": get_messages_tokens(messages),
                "completion_tokens": 0,
                "total_tokens": get_messages_tokens(messages),
                "llm_elapsed_time_seconds": llm_elapsed
            }
        }
        with open(save_path, "w", encoding="utf-8") as f:
            json.dump(completion, f, indent=2, ensure_ascii=False)
        return False, llm_elapsed

    client = OpenAI(
        api_key=random.choice(api_keys),
        base_url="https://yibuapi.com/v1"
        
        # base_url = "https://dashscope.aliyuncs.com/compatible-mode/v1"

        # base_url="https://api.chatanywhere.tech/v1"
    )

    max_try = 5
    # ── measure wall-clock of LLM call only ────────────────────────────────
    t0 = time.time()
    while max_try:
        try:
            completion = client.chat.completions.create(
                messages=messages,
                model=model,
                temperature=temperature,
                top_p=top_p,
                frequency_penalty=frequency_penalty,
                presence_penalty=presence_penalty,
            )
            llm_elapsed = round(time.time() - t0, 4)

            completion_dict = completion.model_dump()
            if "usage" not in completion_dict:
                completion_dict["usage"] = {}
            # store elapsed so downstream readers can access it
            completion_dict["usage"]["llm_elapsed_time_seconds"] = llm_elapsed

            with open(save_path, "w") as f:
                json.dump(completion_dict, f, indent=2)
            return True, llm_elapsed

        except Exception as e:
            print(Fore.RED + str(e), Style.RESET_ALL)
            if "This model's maximum context length is 4097 tokens." in str(e):
                break
            time.sleep(10)
            if "Rate limit reached" in str(e):
                time.sleep(random.randint(60, 120))
        max_try -= 1

    llm_elapsed = round(time.time() - t0, 4)
    completion_fail = {
        "choices": [
            {
                "message": {
                    "role": "assistant",
                    "content": "GPT request failed after 5 retries."
                },
                "finish_reason": "error"
            }
        ],
        "usage": {
            "prompt_tokens": 0,
            "completion_tokens": 0,
            "total_tokens": 0,
            "llm_elapsed_time_seconds": llm_elapsed
        }
    }
    with open(save_path, "w", encoding="utf-8") as f:
        json.dump(completion_fail, f, indent=2, ensure_ascii=False)
    return False, llm_elapsed


def ask_llama(messages, save_path):
    """
    Call local Llama3 via Ollama.

    Returns
    -------
    success : bool
    llm_elapsed : float   wall-clock seconds of the LLM API call only
    """
    payload = {
        "model": "llama3:8b",
        "messages": messages,
        "stream": False,
        "temperature": 0.5, 
        "top_p": 1.0   
    }
    # ── measure wall-clock of LLM call only ────────────────────────────────
    t0 = time.time()
    max_try = 5
    while max_try:
        try:
            resp = requests.post(
                OLLAMA_URL,
                json=payload,
                timeout=OLLAMA_TIMEOUT
            )
            resp.raise_for_status()
            result = resp.json()

            llm_elapsed = round(time.time() - t0, 4)
            completion = {
                "choices": [
                    {
                        "message": {
                            "role": "assistant",
                            "content": result["choices"][0]["message"]["content"]
                        },
                        "finish_reason": result["choices"][0].get("finish_reason", "stop")
                    }
                ],
                "usage": {
                    "prompt_tokens": get_messages_tokens(messages),
                    "completion_tokens": len(result["choices"][0]["message"]["content"]) // 4,
                    "total_tokens": (get_messages_tokens(messages) +
                                     len(result["choices"][0]["message"]["content"]) // 4),
                    "llm_elapsed_time_seconds": llm_elapsed
                }
            }
            with open(save_path, "w", encoding="utf-8") as f:
                json.dump(completion, f, indent=2, ensure_ascii=False)
            return True, llm_elapsed

        except requests.exceptions.ConnectionError:
            print(Fore.RED + f"Ollama connection failed: {OLLAMA_URL}", Style.RESET_ALL)
            time.sleep(10)
        except requests.exceptions.Timeout:
            print(Fore.RED + f"Ollama timeout ({OLLAMA_TIMEOUT}s)", Style.RESET_ALL)
            time.sleep(10)
        except requests.exceptions.HTTPError as e:
            print(Fore.RED + f"Ollama HTTP error: {e}", Style.RESET_ALL)
            break
        except Exception as e:
            print(Fore.RED + f"Ollama error: {e}", Style.RESET_ALL)
            time.sleep(5)
        max_try -= 1

    llm_elapsed = round(time.time() - t0, 4)
    completion_fail = {
        "choices": [{"message": {"role": "assistant", "content": "LLM request failed"}}],
        "usage": {
            "prompt_tokens": 0,
            "completion_tokens": 0,
            "total_tokens": 0,
            "llm_elapsed_time_seconds": llm_elapsed
        }
    }
    with open(save_path, "w", encoding="utf-8") as f:
        json.dump(completion_fail, f, indent=2, ensure_ascii=False)
    print(Fore.RED + "Ollama failed after 5 retries", Style.RESET_ALL)
    return False, llm_elapsed


def generate_prompt(template_name, context: dict):
    """
    Generate prompt via jinja2 engine
    :param template_name: the name of the prompt template
    :param context: the context to render the template
    :return:
    """
    # Load template
    template = env.get_template(template_name)
    return template.render(context)


def load_context_file(context_file):
    if isinstance(context_file, str):
        with open(context_file, "r") as f:
            return json.load(f)
    return context_file


def generate_messages(template_name, context_file):
    """
    This function generates messages before asking GPT, using user and system templates.
    :param template_name: The template name of the user template.
    :param context_file: The context JSON file or dict to render the template.
    :return: A list of generated messages.
    """
    context = load_context_file(context_file)
    messages = []
    system_name = f"{template_name.split('.')[0]}_system.jinja2"
    system_path = os.path.join("../prompt", system_name)
    if os.path.exists(system_path):
        system_message = generate_prompt(system_name, {})
        messages.append({"role": "system", "content": system_message})

    user_message = generate_prompt(template_name, context)
    messages.append({"role": "user", "content": user_message})

    return messages


def complete_code(code):
    """
    To complete the code
    :param code:
    :return:
    """

    # We delete the last incomplete test.
    code = code.split("@Test\n")
    code = "@Test\n".join(code[:-1]) + '}'
    return code


def process_error_message(error_message, allowed_tokens):
    """
    Process the error message
    :param error_message:
    :param allowed_tokens:
    :return:
    """
    if allowed_tokens <= 0:
        return ""
    while count_tokens(error_message) > allowed_tokens:
        if len(error_message) > 50:
            error_message = error_message[:-50]
        else:
            break
    return error_message


def if_code_is_valid(code) -> bool:
    """
    Check if the code is valid
    :param code:
    :return: True or False
    """
    if "{" not in code or "}" not in code:
        return False
    try:
        javalang.parse.parse(code)
        return True
    except Exception:
        return False


def syntactic_check(code):
    """
    Syntactic repair
    :param code:
    :return: has_syntactic_error, code
    """
    if is_syntactic_correct(code):
        return False, code
    stop_point = [";", "}", "{", " "]
    for idx in range(len(code) - 1, -1, -1):
        if code[idx] in stop_point:
            code = code[:idx + 1]
            break
    left_bracket = code.count("{")
    right_bracket = code.count("}")
    for _ in range(left_bracket - right_bracket):
        code += "}\n"
    if is_syntactic_correct(code):
        return True, code
    matches = list(re.finditer(r"(?<=\})[^\}]+(?=@)", code))
    if matches:
        code = code[:matches[-1].start() + 1]
        left_count = code.count("{")
        right_count = code.count("}")
        for _ in range(left_count - right_count):
            code += "\n}"
    if is_syntactic_correct(code):
        return True, code
    return True, ""


def is_syntactic_correct(code):
    """
    Check if the code is syntactically correct
    :param code:
    :return:
    """
    try:
        javalang.parse.parse(code)
        return True
    except Exception as e:
        return False


def extract_code(string):
    """
    Check if the string is valid code and extract it.
    :param string:
    :return: has_code, extracted_code, has_syntactic_error
    """
    # if the string is valid code, return True
    if is_syntactic_correct(string):
        return True, string, False

    has_code = False
    extracted_code = ""
    has_syntactic_error = False

    # Define regex pattern to match the code blocks
    pattern = r"```[java]*([\s\S]*?)```"

    # Find all matches in the text
    matches = re.findall(pattern, string)
    if matches:
        # Filter matches to only include ones that contain "@Test"
        filtered_matches = [match.strip() for match in matches if
                            "@Test" in match and "class" in match and "import" in match]
        if filtered_matches:
            for match in filtered_matches:
                has_syntactic_error, extracted_code = syntactic_check(match)
                if extracted_code != "":
                    has_code = True
                    break

    if not has_code:
        if "```java" in string:
            separate_string = string.split("```java")[1]
            if "@Test" in separate_string:
                has_syntactic_error, temp_code = syntactic_check(separate_string)
                if temp_code != "":
                    extracted_code = temp_code
                    has_code = True
        elif "```" in string:
            separate_strings = string.split("```")
            for separate_string in separate_strings:
                if "@Test" in separate_string:
                    has_syntactic_error, temp_code = syntactic_check(separate_string)
                    if temp_code != "":
                        extracted_code = temp_code
                        has_code = True
                        break
        else:
            allowed = ["import", "packages", "", "@"]
            code_lines = string.split("\n")
            start, anchor, end = -1, -1, -1
            allowed_lines = [False for _ in range(len(code_lines))]
            left_brace = {x: 0 for x in range(len(code_lines))}
            right_brace = {x: 0 for x in range(len(code_lines))}
            for i, line in enumerate(code_lines):
                left_brace[i] += line.count("{")
                right_brace[i] += line.count("}")
                striped_line = line.strip()

                for allow_start in allowed:
                    if striped_line.startswith(allow_start):
                        allowed_lines[i] = True
                        break

                if re.search(r'public class .*Test', line) and anchor == -1:
                    anchor = i

            if anchor != -1:
                start = anchor
                while start:
                    if allowed_lines[start]:
                        start -= 1

                end = anchor
                left_sum, right_sum = 0, 0
                while end < len(code_lines):
                    left_sum += left_brace[end]
                    right_sum += right_brace[end]
                    if left_sum == right_sum and left_sum >= 1 and right_sum >= 1:
                        break
                    end += 1

                temp_code = "\n".join(code_lines[start:end + 1])
                has_syntactic_error, temp_code = syntactic_check(temp_code)
                if temp_code != "":
                    extracted_code = temp_code
                    has_code = True

    extracted_code = extracted_code.strip()
    return has_code, extracted_code, has_syntactic_error


def extract_and_run(input_string, output_path, class_name, method_id, test_num, project_name, package):
    """
    Extract the code and run it
    :param project_name:
    :param test_num:
    :param method_id:
    :param class_name:
    :param input_string:
    :param output_path:
    :return:
    """
    result = {}
    # 1. Extract the code
    has_code, extracted_code, has_syntactic_error = extract_code(input_string)
    if not has_code:
        return False, True
    result["has_code"] = has_code
    result["source_code"] = extracted_code
    if package:
        result["source_code"] = repair_package(extracted_code, package)
    result["has_syntactic_error"] = has_syntactic_error
    # 2. Run the code
    temp_dir = os.path.join(os.path.dirname(output_path), "temp")
    if os.path.exists(temp_dir):
        shutil.rmtree(temp_dir)
    os.makedirs(temp_dir)

    export_method_test_case(os.path.abspath(temp_dir), class_name, method_id, test_num,
                            change_class_name(result["source_code"], class_name, method_id, test_num))

    # run test
    response_dir = os.path.abspath(os.path.dirname(output_path))
    target_dir = os.path.abspath(project_dir)
    Task.test(response_dir, target_dir)

    # 3. Read the result
    if "compile_error.txt" in os.listdir(temp_dir):
        with open(os.path.join(temp_dir, "compile_error.txt"), "r") as f:
            result["compile_error"] = f.read()

    if "runtime_error.txt" in os.listdir(temp_dir):
        with open(os.path.join(temp_dir, "runtime_error.txt"), "r") as f:
            result["runtime_error"] = f.read()
    if "coverage.html" in os.listdir(temp_dir):
        result["coverage_html"] = True
    if "coverage.xml" in os.listdir(temp_dir):
        result["coverage_xml"] = True

    test_passed = False
    if "coverage_xml" in result or "coverage_html" in result:
        test_passed = True

    # 4. Save the result
    with open(output_path, "w") as f:
        json.dump(result, f)

    return test_passed, False


def remain_prompt_tokens(messages):
    return MAX_PROMPT_TOKENS - get_messages_tokens(messages)


def whole_process(test_num, base_name, base_dir, repair, submits, total):
    """
    Generate and repair a test case for one method × one test-number slot.

    Token / time tracking
    ---------------------
    Uses LLMStatsTracker (matching refine-agent):
      • prompt_tokens / completion_tokens  — from API response["usage"]
      • llm_elapsed_seconds               — wall-clock of the HTTP call ONLY
        (compile / run / repair time is NOT included)

    The tracker summary is saved to  <save_dir>/llm_stats.json
    alongside the existing time_stats.json for backward compatibility.
    """
    process_start_time = time.time()
    progress = '[' + str(submits) + ' / ' + str(total) + ']'
    # Create subdirectories for each test
    save_dir = os.path.join(base_dir, str(test_num))
    if not os.path.exists(save_dir):
        os.makedirs(save_dir)
    run_temp_dir = os.path.join(save_dir, "runtemp")

    steps, rounds = 0, 0
    method_id, project_name, class_name, method_name = parse_file_name(base_name)

    # ── NEW: LLMStatsTracker (refine-agent compatible) ───────────────────────
    tracker = LLMStatsTracker()

    # ── LEGACY: keep time_stats for backward compatibility ───────────────────
    time_stats = {
        "process_start_time": process_start_time,
        "process_end_time": 0,
        "total_elapsed_time_seconds": 0,
        "total_llm_elapsed_seconds": 0.0,   # NEW field: LLM-only time
        "rounds_time_stats": {}
    }

    # 1. Get method data
    with open(get_dataset_path(method_id, project_name, class_name, method_name, "raw"), "r") as f:
        raw_data = json.load(f)

    package = raw_data["package"]
    imports = raw_data["imports"]

    # 2. Get data from direction_1 as well as direction_3
    with open(get_dataset_path(method_id, project_name, class_name, method_name, 1), "r") as f:
        context_d_1 = json.load(f)
    with open(get_dataset_path(method_id, project_name, class_name, method_name, 3), "r") as f:
        context_d_3 = json.load(f)

    def _remove_imports_context(strings):
        if imports:
            strings = strings.replace(imports, "")
        if package:
            strings = strings.replace(package, "")
        strings = strings.strip()
        return strings

    try:
        while rounds < max_rounds:
            round_start_time = time.time()

            # ── advance tracker round ─────────────────────────────────────────
            tracker.next_round()

            steps += 1
            rounds += 1
            print(progress, method_id, "test_" + str(test_num), "Asking gpt...", "rounds", rounds)
            gpt_file_name = os.path.join(save_dir, str(steps) + "_GPT_" + str(rounds) + ".json")

            # ── build messages ────────────────────────────────────────────────
            if rounds != 1:
                last_round_result = get_latest_file(save_dir)
                with open(last_round_result, "r") as f:
                    last_round_result = json.load(f)
                last_raw = get_latest_file(save_dir, suffix="raw")
                with open(last_raw, "r") as f:
                    last_raw = json.load(f)

                context = {
                    "class_name": context_d_1["class_name"],
                    "method_name": context_d_1["focal_method"],
                    "unit_test": last_raw["source_code"],
                    "method_code": context_d_1["information"]
                }
                messages = generate_messages(TEMPLATE_ERROR, context)
                allow_tokens = remain_prompt_tokens(messages)
                if allow_tokens < MIN_ERROR_TOKENS:
                    context["method_code"] = _remove_imports_context(context["method_code"])
                    messages = generate_messages(TEMPLATE_ERROR, context)
                    allow_tokens = remain_prompt_tokens(messages)
                if allow_tokens < MIN_ERROR_TOKENS:
                    context["method_code"] = context_d_3["full_fm"]
                    messages = generate_messages(TEMPLATE_ERROR, context)
                    allow_tokens = remain_prompt_tokens(messages)
                if allow_tokens < MIN_ERROR_TOKENS:
                    context["method_code"] = _remove_imports_context(context_d_3["full_fm"])
                    messages = generate_messages(TEMPLATE_ERROR, context)
                    allow_tokens = remain_prompt_tokens(messages)
                if allow_tokens >= MIN_ERROR_TOKENS:
                    if "compile_error" in last_round_result:
                        context["error_type"] = "compiling"
                        context["error_message"] = process_error_message(
                            last_round_result["compile_error"], allow_tokens)
                    if "runtime_error" in last_round_result:
                        context["error_type"] = "running"
                        context["error_message"] = process_error_message(
                            last_round_result["runtime_error"], allow_tokens)
                else:
                    print(progress, Fore.RED + method_id, "Tokens not enough, fatal error...",
                          Style.RESET_ALL)
                    break

                has_compile_error = ("compile_error" in last_round_result and
                                     last_round_result["compile_error"].strip() != "")
                has_runtime_error = ("runtime_error" in last_round_result and
                                     last_round_result["runtime_error"].strip() != "")
                if not has_compile_error and not has_runtime_error:
                    print(progress, Fore.RED + method_id, "Timeout error, fatal error...",
                          Style.RESET_ALL)
                    break
                messages = generate_messages(TEMPLATE_ERROR, context)
            else:
                if not context_d_3["c_deps"] and not context_d_3["m_deps"]:
                    context = copy.deepcopy(context_d_1)
                    messages = generate_messages(TEMPLATE_NO_DEPS, context)
                    if remain_prompt_tokens(messages) < 0:
                        context["information"] = _remove_imports_context(context["information"])
                        messages = generate_messages(TEMPLATE_NO_DEPS, context)
                        if remain_prompt_tokens(messages) < 0:
                            messages = []
                else:
                    context = copy.deepcopy(context_d_3)
                    messages = generate_messages(TEMPLATE_WITH_DEPS, context)
                    if remain_prompt_tokens(messages) < 0:
                        context["full_fm"] = _remove_imports_context(context["full_fm"])
                        messages = generate_messages(TEMPLATE_WITH_DEPS, context)
                        if remain_prompt_tokens(messages) < 0:
                            messages = []

                if not messages:
                    context = copy.deepcopy(context_d_1)
                    context["information"] = context_d_3["full_fm"]
                    messages = generate_messages(TEMPLATE_NO_DEPS, context)
                    if remain_prompt_tokens(messages) < 0:
                        context["information"] = _remove_imports_context(context["information"])
                        messages = generate_messages(TEMPLATE_NO_DEPS, context)
                        if remain_prompt_tokens(messages) < 0:
                            print(progress, Fore.RED + "Tokens not enough, fatal error...",
                                  Style.RESET_ALL)
                            break

            # ── LLM call — measure elapsed here (wall-clock of HTTP only) ────
            status, llm_elapsed = ask_chatgpt(messages, gpt_file_name)
            # status, llm_elapsed = ask_llama(messages, gpt_file_name)

            if not status:
                print(progress, Fore.RED + 'OpenAI Fail processing messages', Style.RESET_ALL)
                time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                    time.time() - round_start_time, 4)
                break

            with open(gpt_file_name, "r") as f:
                gpt_result = json.load(f)

            # ── record in tracker (refine-agent compatible) ──────────────────
            #    tokens come directly from API response["usage"]
            #    elapsed is the LLM-call wall-clock only
            tracker.record_from_response(gpt_result, elapsed=llm_elapsed,
                                         round_idx=rounds)

            # ── extract + run ────────────────────────────────────────────────
            steps += 1

            raw_file_name = os.path.join(save_dir, str(steps) + "_raw_" + str(rounds) + ".json")

            # extract the test and save the result in raw_file_name
            input_string = gpt_result["choices"][0]['message']["content"]
            test_passed, fatal_error = extract_and_run(input_string, raw_file_name, class_name, method_id, test_num,
                                                       project_name,
                                                       package)

            # ── annotate raw file with token / timing info ───────────────────
            if os.path.exists(raw_file_name):
                with open(raw_file_name, "r+", encoding="utf-8") as f:
                    raw_result = json.load(f)
                    raw_result["prompt_tokens"]            = gpt_result["usage"]["prompt_tokens"]
                    raw_result["completion_tokens"]        = gpt_result["usage"]["completion_tokens"]
                    raw_result["total_tokens"]             = gpt_result["usage"]["total_tokens"]
                    raw_result["llm_elapsed_time_seconds"] = llm_elapsed   # LLM-call time only
                    f.seek(0)
                    json.dump(raw_result, f, indent=2, ensure_ascii=False)
                    f.truncate()

            if test_passed:
                print(progress, Fore.GREEN + method_id, "test_" + str(test_num),
                      "steps", steps, "rounds", rounds, "test passed", Style.RESET_ALL)
                time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                    time.time() - round_start_time, 4)
                break

            if not os.path.exists(raw_file_name):
                print(progress, Fore.RED + method_id, "test_" + str(test_num),
                      "steps", steps, "rounds", rounds, "no code in raw result", Style.RESET_ALL)
                time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                    time.time() - round_start_time, 4)
                break

            with open(get_latest_file(save_dir), "r") as f:
                raw_result = json.load(f)

            # ── imports repair ────────────────────────────────────────────────
            steps += 1
            imports_file_name = os.path.join(save_dir,
                                             str(steps) + "_imports_" + str(rounds) + ".json")
            source_code = raw_result["source_code"]
            source_code = repair_imports(source_code, imports)
            test_passed, fatal_error = extract_and_run(
                source_code, imports_file_name, class_name,
                method_id, test_num, project_name, package)

            if os.path.exists(imports_file_name):
                with open(imports_file_name, "r+", encoding="utf-8") as f:
                    imports_result = json.load(f)
                    imports_result["prompt_tokens"]            = gpt_result["usage"]["prompt_tokens"]
                    imports_result["completion_tokens"]        = gpt_result["usage"]["completion_tokens"]
                    imports_result["total_tokens"]             = gpt_result["usage"]["total_tokens"]
                    imports_result["llm_elapsed_time_seconds"] = llm_elapsed
                    f.seek(0)
                    json.dump(imports_result, f, indent=2, ensure_ascii=False)
                    f.truncate()

            if test_passed:
                print(progress, Fore.GREEN + method_id, "test_" + str(test_num),
                      "steps", steps, "rounds", rounds, "test passed", Style.RESET_ALL)
                time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                    time.time() - round_start_time, 4)
                break
            if fatal_error:
                print(progress, Fore.RED + method_id, "test_" + str(test_num),
                      "steps", steps, "rounds", rounds, "fatal error", Style.RESET_ALL)
                time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                    time.time() - round_start_time, 4)
                break

            print(progress, Fore.YELLOW + method_id, "test_" + str(test_num),
                  "Test failed, fixing...", "rounds", rounds, Style.RESET_ALL)
            time_stats["rounds_time_stats"][f"round_{rounds}"] = round(
                time.time() - round_start_time, 4)

            if not repair:
                break

    except Exception as e:
        print(progress, Fore.RED + str(e), Style.RESET_ALL)

    finally:
        # 新增：计算单个测试用例总耗时，保存时间统计文件
        process_end_time = time.time()
        tracker_summary = tracker.summary()

        # ── finalise time_stats ───────────────────────────────────────────────
        time_stats["process_end_time"]           = process_end_time
        time_stats["total_elapsed_time_seconds"] = round(
            process_end_time - process_start_time, 4)
        time_stats["total_llm_elapsed_seconds"]  = round(
            tracker_summary["total_llm_elapsed_seconds"], 4)

        # ── save time_stats.json (backward compat) ────────────────────────────
        with open(os.path.join(save_dir, "time_stats.json"), "w", encoding="utf-8") as f:
            json.dump(time_stats, f, indent=2, ensure_ascii=False)

        # ── save llm_stats.json (refine-agent compatible) ────────────────────
        llm_stats_out = {
            # ── same top-level keys as refine-agent LLMStatsTracker.summary() ─
            "total_prompt_tokens":       tracker_summary["total_prompt_tokens"],
            "total_completion_tokens":   tracker_summary["total_completion_tokens"],
            "total_tokens":              tracker_summary["total_tokens"],
            "total_llm_elapsed_seconds": tracker_summary["total_llm_elapsed_seconds"],
            "num_llm_calls":             tracker_summary["num_llm_calls"],
            "rounds":                    tracker_summary["rounds"],
            # ── extra context ─────────────────────────────────────────────────
            "method_id":    method_id,
            "test_num":     test_num,
            "project_name": project_name,
            "class_name":   class_name,
            "method_name":  method_name,
        }
        with open(os.path.join(save_dir, "llm_stats.json"), "w", encoding="utf-8") as f:
            json.dump(llm_stats_out, f, indent=2, ensure_ascii=False)

        print(progress, Fore.BLUE + method_id, "test_" + str(test_num),
              f"process_total={time_stats['total_elapsed_time_seconds']}s | "
              f"llm_only={time_stats['total_llm_elapsed_seconds']}s | "
              f"tokens={tracker_summary['total_tokens']} "
              f"(prompt={tracker_summary['total_prompt_tokens']}, "
              f"compl={tracker_summary['total_completion_tokens']})",
              Style.RESET_ALL)

    if os.path.exists(run_temp_dir):
        shutil.rmtree(os.path.abspath(run_temp_dir))

    # return the tracker summary so start_whole_process can aggregate
    return tracker_summary


def start_whole_process(source_dir, result_path, multiprocess=True, repair=True):
    """
    Orchestrate generation across all methods × test slots.

    Global stats
    ------------
    Aggregates LLMStatsTracker summaries from all tasks and writes:
      <result_path>/global_stats.json   — refine-agent compatible schema
    """
    global_start_time = time.time()

    # aggregate tracker-summary dicts from all tasks
    all_summaries = []
    futures = []

    file_paths = []
    for root, dirs, files in os.walk(source_dir):
        for file in files:
            if file.endswith(".json"):
                file_paths.append(os.path.join(root, file))

    submits = 0
    total = len(file_paths) * test_number

    if multiprocess:
        print("Multi process executing!")
        with concurrent.futures.ProcessPoolExecutor(max_workers=process_number) as executor:
            for file_path in file_paths:
                _, base_name = os.path.split(
                    file_path.replace("/dataset_batch/", "/results_batch/"))
                base_dir = os.path.join(result_path, base_name.split(".json")[0])
                for test_num in range(1, test_number + 1):
                    submits += 1
                    future = executor.submit(
                        whole_process, test_num, base_name, base_dir,
                        repair, submits, total)
                    futures.append(future)

            for future in concurrent.futures.as_completed(futures):
                try:
                    summary = future.result()
                    if summary:
                        all_summaries.append(summary)
                except Exception as e:
                    print(Fore.RED + f"Task error: {e}" + Style.RESET_ALL)
    else:
        print("Single process executing!")
        for file_path in file_paths:
            _, base_name = os.path.split(
                file_path.replace("/dataset_batch/", "/results_batch/"))
            base_dir = os.path.join(result_path, base_name.split(".json")[0])
            for test_num in range(1, test_number + 1):
                submits += 1
                summary = whole_process(test_num, base_name, base_dir,
                                        repair, submits, total)
                if summary:
                    all_summaries.append(summary)

    # ── aggregate global stats ────────────────────────────────────────────────
    global_end_time = time.time()
    global_wall_clock = round(global_end_time - global_start_time, 4)

    total_prompt_tokens     = sum(s["total_prompt_tokens"]       for s in all_summaries)
    total_completion_tokens = sum(s["total_completion_tokens"]   for s in all_summaries)
    total_tokens            = sum(s["total_tokens"]              for s in all_summaries)
    total_llm_elapsed       = sum(s["total_llm_elapsed_seconds"] for s in all_summaries)
    total_llm_calls         = sum(s["num_llm_calls"]             for s in all_summaries)

    global_stats = {
        # ── refine-agent compatible top-level keys ────────────────────────────
        "total_prompt_tokens":           total_prompt_tokens,
        "total_completion_tokens":       total_completion_tokens,
        "total_tokens":                  total_tokens,
        "total_llm_elapsed_seconds":     round(total_llm_elapsed, 4),
        "num_llm_calls":                 total_llm_calls,

        # ── timing ────────────────────────────────────────────────────────────
        "global_wall_clock_seconds":     global_wall_clock,
        "total_tasks":                   total,
        "completed_tasks":               submits,
        "avg_task_wall_clock_seconds":   round(global_wall_clock / submits, 4) if submits else 0,
        "avg_llm_elapsed_per_task_sec":  round(total_llm_elapsed  / submits, 4) if submits else 0,

        # ── per-task averages ─────────────────────────────────────────────────
        "avg_tokens_per_task":           round(total_tokens            / submits, 2) if submits else 0,
        "avg_prompt_tokens_per_task":    round(total_prompt_tokens     / submits, 2) if submits else 0,
        "avg_completion_tokens_per_task":round(total_completion_tokens / submits, 2) if submits else 0,
    }

    global_stats_path = os.path.join(result_path, "global_stats.json")
    with open(global_stats_path, "w", encoding="utf-8") as f:
        json.dump(global_stats, f, indent=2, ensure_ascii=False)

    print(Fore.MAGENTA + "\n===== Global Stats =====")
    print(f"Tasks:             {submits}/{total}")
    print(f"Wall-clock:        {global_wall_clock}s")
    print(f"LLM-only elapsed:  {round(total_llm_elapsed, 4)}s  "
          f"({total_llm_calls} calls)")
    print(f"Total tokens:      {total_tokens}  "
          f"(prompt={total_prompt_tokens}, completion={total_completion_tokens})")
    print(f"Avg LLM time/task: {global_stats['avg_llm_elapsed_per_task_sec']}s")
    print(f"Saved: {global_stats_path}")
    print("========================" + Style.RESET_ALL)