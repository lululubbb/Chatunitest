#!/bin/bash

# 1. 完整 commons-csv 项目根目录（必填！）
TARGET_PROJECT_DIR="/home/chenlu/commons-csv"
# 2. ChatUniTest 相关核心路径（根据实际路径确认）
CONFIG_FILE="/home/chenlu/ChatUniTest_GPT3.5/config/config.ini"
CHATUNITEST_SRC="/home/chenlu/ChatUniTest_GPT3.5/src"
# 3. 结果存储根目录（自定义，会自动创建）
RESULT_ROOT="/home/chenlu/ChatUniTest_GPT3.5/results_commons_csv"
# 4. 数据集存储目录（自定义，会自动创建）
DATASET_DIR="/home/chenlu/ChatUniTest_GPT3.5/dataset_commons_csv"

# ===================== 无需修改的核心逻辑 =====================
# 1. 检查目标项目是否存在
if [ ! -d "$TARGET_PROJECT_DIR" ]; then
    echo -e "\033[31m❌ 错误：目标项目目录不存在 → $TARGET_PROJECT_DIR\033[0m"
    exit 1
fi

# 2. 创建结果/数据集目录（避免不存在）
mkdir -p $RESULT_ROOT
mkdir -p $DATASET_DIR

# 3. 修改 ChatUniTest 配置文件（指向完整 commons-csv 项目）
# 注：sed 的 -i 后加 '' 是兼容 macOS，Linux 可省略；| 是路径分隔符（避免项目路径含 / 导致替换失败）
sed -i '' \
    -e "s|^project_dir = .*|project_dir = $TARGET_PROJECT_DIR|g" \
    -e "s|^result_dir = .*|result_dir = $RESULT_ROOT|g" \
    -e "s|^dataset_dir = .*|dataset_dir = $DATASET_DIR|g" \
    $CONFIG_FILE

# 验证配置修改结果（控制台打印）
echo -e "\033[32m✅ 已更新 ChatUniTest 配置：\033[0m"
grep "^project_dir = " $CONFIG_FILE | xargs
grep "^result_dir = " $CONFIG_FILE | xargs
grep "^dataset_dir = " $CONFIG_FILE | xargs

# 4. 定义日志文件路径（存储所有控制台输出）
RUN_LOG="$RESULT_ROOT/commons_csv_run.log"
echo -e "\033[34m🚀 开始为 commons-csv 生成测试用例...\033[0m"
echo -e "\033[34m📝 所有输出将同时写入日志文件：$RUN_LOG\033[0m"

# 5. 进入 ChatUniTest 源码目录运行核心脚本
cd $CHATUNITEST_SRC

# 核心：同时输出到控制台 + 写入日志文件（2>&1 合并 stderr/stdout，tee 实现双输出）
python3 run.py 2>&1 | tee "$RUN_LOG"

# 6. 判断运行结果并标记
RUN_EXIT_CODE=$?
if [ $RUN_EXIT_CODE -eq 0 ]; then
    echo -e "\n\033[32m✅ commons-csv 测试用例生成完成！\033[0m"
    echo -e "\n✅ 运行成功：$(date +%Y-%m-%d\ %H:%M:%S)" >> "$RUN_LOG"
else
    echo -e "\n\033[31m❌ commons-csv 测试用例生成失败！\033[0m"
    echo -e "\n❌ 运行失败（退出码：$RUN_EXIT_CODE）：$(date +%Y-%m-%d\ %H:%M:%S)" >> "$RUN_LOG"
    exit $RUN_EXIT_CODE
fi

# 7. 生成运行汇总（方便快速查看关键信息）
SUMMARY="$RESULT_ROOT/commons_csv_summary.txt"
echo -e "===== commons-csv 测试用例生成汇总 =====\n" > "$SUMMARY"
echo "项目根目录：$TARGET_PROJECT_DIR" >> "$SUMMARY"
echo "运行时间：$(date +%Y-%m-%d\ %H:%M:%S)" >> "$SUMMARY"
echo "运行状态：成功" >> "$SUMMARY"
echo "完整日志路径：$RUN_LOG" >> "$SUMMARY"
echo "数据集存储目录：$DATASET_DIR" >> "$SUMMARY"
echo "ChatUniTest 配置文件：$CONFIG_FILE" >> "$SUMMARY"

echo -e "\033[32m📋 运行汇总已保存至：$SUMMARY\033[0m"
echo -e "\033[32m🎉 全部操作完成！\033[0m"