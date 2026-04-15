#!/bin/bash
# batch_run.sh — 支持 _f (fixed-first) 和 _b (buggy-first) 两种模式
# 用法: bash batch_run.sh
#
# Fixed-first（推荐，新默认）：
#   PROJECT_ROOT 下的 Csv*_*_f 目录会被自动发现
#   测试用例在 _f 版本的代码上生成
#   bug_revealing 单独在 _b 版本上运行（见 run_bug_revealing.py）
#
# 兼容旧版 Buggy-first：
#   若 PROJECT_ROOT 下没有 _f 目录，自动回退到查找 _b 目录

# ── 配置路径 ──────────────────────────────────────────────────────
CONFIG_FILE="/home/chenlu/ChatUniTest_GPT3.5/config/config.ini"
PROJECT_ROOT="/home/chenlu/ChatUniTest_GPT3.5/defect4j_projects"
CHATUNITEST_SRC="/home/chenlu/ChatUniTest_GPT3.5/src"
RESULT_ROOT="/home/chenlu/ChatUniTest_GPT3.5/results_batch"
DATASET_GLOBAL_ROOT="/home/chenlu/ChatUniTest_GPT3.5/dataset_batch"

mkdir -p "$RESULT_ROOT"
mkdir -p "$DATASET_GLOBAL_ROOT"

# ── 自动检测使用 _f 还是 _b ────────────────────────────────────────
# 优先使用 _f（fixed-first），如果没有 _f 才使用 _b（兼容旧版）
FIXED_DIRS=$(find "$PROJECT_ROOT" -maxdepth 1 -type d -name "Csv*_*_f" 2>/dev/null | sort -V)
BUGGY_DIRS=$(find "$PROJECT_ROOT" -maxdepth 1 -type d -name "Csv*_*_b" 2>/dev/null | sort -V)

if [ -n "$FIXED_DIRS" ]; then
    echo "=== Fixed-first 模式：在 _f 版本上生成测试用例 ==="
    TARGET_DIRS="$FIXED_DIRS"
    MODE="fixed"
elif [ -n "$BUGGY_DIRS" ]; then
    echo "=== Buggy-first 模式（兼容旧版）：在 _b 版本上生成测试用例 ==="
    TARGET_DIRS="$BUGGY_DIRS"
    MODE="buggy"
else
    echo "❌ 在 $PROJECT_ROOT 下未找到任何 Csv*_*_f 或 Csv*_*_b 目录"
    exit 1
fi

# ── 遍历目标目录 ───────────────────────────────────────────────────
echo "$TARGET_DIRS" | while read -r defect_dir; do
    [ -z "$defect_dir" ] && continue

    defect_name=$(basename "$defect_dir")
    echo "========================================"
    echo "开始处理：$defect_name  (模式: $MODE)"
    echo "========================================"

    defect_result_dir="$RESULT_ROOT/$defect_name"
    defect_dataset_dir="$DATASET_GLOBAL_ROOT/$defect_name"

    # 断点续跑：有结果目录+日志文件则跳过
    if [ -d "$defect_result_dir" ] && [ -f "$defect_result_dir/run_log.txt" ]; then
        echo "⚠️ $defect_name 已处理过，跳过！"
        echo "========================================"
        continue
    fi

    # 修改 config.ini，project_dir 指向当前目录（可能是 _f 或 _b）
    sed -i \
        -e "s|^project_dir = .*|project_dir = $defect_dir|g" \
        -e "s|^result_dir = .*|result_dir = $defect_result_dir|g" \
        -e "s|^dataset_dir = .*|dataset_dir = $defect_dataset_dir|g" \
        "$CONFIG_FILE"

    echo "已修改配置："
    grep "^project_dir = " "$CONFIG_FILE" | xargs
    grep "^result_dir = " "$CONFIG_FILE" | xargs
    grep "^dataset_dir = " "$CONFIG_FILE" | xargs

    mkdir -p "$defect_result_dir"
    mkdir -p "$defect_dataset_dir"

    cd "$CHATUNITEST_SRC" || exit 1
    echo "开始运行 ChatUniTest（project_dir=$defect_dir）..."
    python3 run.py > "$defect_result_dir/run_log.txt" 2>&1

    if [ $? -eq 0 ]; then
        echo "✅ $defect_name 处理完成！日志：$defect_result_dir/run_log.txt"
    else
        echo "❌ $defect_name 处理失败！日志：$defect_result_dir/run_log.txt"
    fi
done

echo ""
echo "========================================"
echo "所有项目处理完毕"
echo "如需运行 bug_revealing，执行："
echo "  python3 $CHATUNITEST_SRC/run_bug_revealing.py $PROJECT_ROOT"
echo "========================================"