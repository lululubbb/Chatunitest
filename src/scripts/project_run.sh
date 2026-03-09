#!/bin/bash
# bash project_run.sh gson
# ===================== 参数校验 =====================
if [ $# -ne 1 ]; then
    echo -e "\033[31m❌ 用法错误！\033[0m"
    echo -e "👉 正确用法：$0 <project_name>"
    echo -e "👉 示例："
    echo -e "   $0 gson"
    echo -e "   $0 commons-csv"
    echo -e "   $0 commons-cli"
    exit 1
fi

PROJECT_NAME=$1

# ===================== 可配置公共路径 =====================
# 所有 Java 项目的统一父目录
PROJECT_BASE_DIR="/home/chenlu"

# ChatUniTest 核心路径
CONFIG_FILE="/home/chenlu/ChatUniTest_GPT3.5/config/config.ini"
CHATUNITEST_SRC="/home/chenlu/ChatUniTest_GPT3.5/src"

# ChatUniTest 结果根目录
RESULT_BASE="/home/chenlu/ChatUniTest_GPT3.5/results"
DATASET_BASE="/home/chenlu/ChatUniTest_GPT3.5/dataset"

# ===================== 动态生成项目相关路径 =====================
TARGET_PROJECT_DIR="${PROJECT_BASE_DIR}/${PROJECT_NAME}"
RESULT_ROOT="${RESULT_BASE}_${PROJECT_NAME}"
DATASET_DIR="${DATASET_BASE}_${PROJECT_NAME}"

# ===================== 核心逻辑（无需再改） =====================

# 1. 检查项目目录是否存在
if [ ! -d "$TARGET_PROJECT_DIR" ]; then
    echo -e "\033[31m❌ 错误：项目目录不存在 → $TARGET_PROJECT_DIR\033[0m"
    exit 1
fi

# 2. 创建结果 / 数据集目录
mkdir -p "$RESULT_ROOT"
mkdir -p "$DATASET_DIR"

# 3. 修改 ChatUniTest 配置
sed -i '' \
    -e "s|^project_dir = .*|project_dir = $TARGET_PROJECT_DIR|g" \
    -e "s|^result_dir = .*|result_dir = $RESULT_ROOT|g" \
    -e "s|^dataset_dir = .*|dataset_dir = $DATASET_DIR|g" \
    "$CONFIG_FILE"

# 4. 验证配置
echo -e "\033[32m✅ ChatUniTest 配置已更新：\033[0m"
grep "^project_dir = " "$CONFIG_FILE" | xargs
grep "^result_dir = " "$CONFIG_FILE" | xargs
grep "^dataset_dir = " "$CONFIG_FILE" | xargs

# 5. 日志文件
RUN_LOG="$RESULT_ROOT/${PROJECT_NAME}_run.log"

echo -e "\033[34m🚀 开始为 [$PROJECT_NAME] 生成测试用例...\033[0m"
echo -e "\033[34m📝 日志文件：$RUN_LOG\033[0m"

# 6. 执行 ChatUniTest
cd "$CHATUNITEST_SRC" || exit 1
python3 run.py 2>&1 | tee "$RUN_LOG"

RUN_EXIT_CODE=${PIPESTATUS[0]}

# 7. 运行结果判断
if [ $RUN_EXIT_CODE -eq 0 ]; then
    STATUS="成功"
    echo -e "\n\033[32m✅ [$PROJECT_NAME] 测试用例生成完成！\033[0m"
else
    STATUS="失败"
    echo -e "\n\033[31m❌ [$PROJECT_NAME] 测试用例生成失败！\033[0m"
fi

# 8. 生成汇总文件
SUMMARY="$RESULT_ROOT/${PROJECT_NAME}_summary.txt"
cat <<EOF > "$SUMMARY"
===== ChatUniTest 运行汇总 =====

项目名称：$PROJECT_NAME
项目目录：$TARGET_PROJECT_DIR
运行状态：$STATUS
运行时间：$(date +%Y-%m-%d\ %H:%M:%S)

结果目录：$RESULT_ROOT
数据集目录：$DATASET_DIR
完整日志：$RUN_LOG
配置文件：$CONFIG_FILE
EOF

echo -e "\033[32m📋 汇总文件已生成：$SUMMARY\033[0m"

# 9. 若失败，返回错误码
if [ $RUN_EXIT_CODE -ne 0 ]; then
    exit $RUN_EXIT_CODE
fi

echo -e "\033[32m🎉 [$PROJECT_NAME] 全流程完成！\033[0m"
