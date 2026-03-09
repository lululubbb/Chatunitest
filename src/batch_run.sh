#!/bin/bash

# 定义核心路径
CONFIG_FILE="/home/chenlu/ChatUniTest_GPT3.5/config/config.ini"  # ChatUniTest配置文件路径
PROJECT_ROOT="/home/chenlu/ChatUniTest_GPT3.5/defect4j_projects"  # 缺陷版本根目录
CHATUNITEST_SRC="/home/chenlu/ChatUniTest_GPT3.5/src"  # ChatUniTest源码目录
RESULT_ROOT="/home/chenlu/ChatUniTest_GPT3.5/results_batch"  # 批量结果存储根目录
DATASET_GLOBAL_ROOT="/home/chenlu/ChatUniTest_GPT3.5/dataset_batch"

# 创建批量结果目录+全局dataset根目录（避免覆盖）
mkdir -p $RESULT_ROOT
mkdir -p $DATASET_GLOBAL_ROOT

# 遍历defect4j_projects下的所有缺陷版本文件夹（如Lang_1_b、Math_1_b）
find "$PROJECT_ROOT" -maxdepth 1 -type d -name "Csv*_1_b" | \
    sort -V | \
    while read -r defect_dir; do
    # 获取缺陷版本名称（如Lang_1_b）
    defect_name=$(basename $defect_dir)
    echo "========================================"
    echo "开始处理：$defect_name"
    echo "========================================"

    # 判断当前缺陷版本是否已处理过（有结果目录+日志文件则跳过）
    defect_result_dir="$RESULT_ROOT/$defect_name"
    defect_dataset_dir="$DATASET_GLOBAL_ROOT/$defect_name" 
    
    if [ -d "$defect_result_dir" ] && [ -f "$defect_result_dir/run_log.txt" ]; then
        echo "⚠️ $defect_name 已处理过，跳过！"
        echo "========================================"
        continue
    fi

    # 1. 修改config.ini中的核心配置（关键：为每个版本分配专属路径）
    sed -i \
        -e "s|^project_dir = .*|project_dir = $defect_dir|g" \
        -e "s|^result_dir = .*|result_dir = $defect_result_dir|g" \
        -e "s|^dataset_dir = .*|dataset_dir = $defect_dataset_dir|g" \
        $CONFIG_FILE
    # 验证修改是否成功
    echo "已修改配置："
    grep "^project_dir = " $CONFIG_FILE | xargs
    grep "^result_dir = " $CONFIG_FILE | xargs
    grep "^dataset_dir = " $CONFIG_FILE | xargs
    
    # 创建当前版本的专属目录（结果+dataset）
    mkdir -p $defect_result_dir
    mkdir -p $defect_dataset_dir  # 创建专属dataset目录

    # 3. 运行ChatUniTest（进入src目录执行）
    cd $CHATUNITEST_SRC
    echo "开始运行ChatUniTest："
    echo "  - 结果目录：$defect_result_dir"
    echo "  - Dataset目录：$defect_dataset_dir"
    python3 run.py > "$defect_result_dir/run_log.txt" 2>&1

    # 4. 运行状态判断
    if [ $? -eq 0 ]; then
        echo "✅ $defect_name 处理完成！日志：$defect_result_dir/run_log.txt"
    else
        echo "❌ $defect_name 处理失败！日志：$defect_result_dir/run_log.txt"
    fi

    # 短暂休眠，避免资源占用过高
done