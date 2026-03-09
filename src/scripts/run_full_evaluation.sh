#!/usr/bin/env bash
set -euo pipefail

# run_full_evaluation.sh
# Usage: bash run_full_evaluation.sh [PROJECT_ROOT] [--backup] [--copy-source] [--inject-jacoco] [--skip-mvn-compile]
# Example: bash run_full_evaluation.sh /home/chenlu/ChatUniTest_GPT3.5/defect4j_projects/Csv_12_b --backup --force-copy
# bash run_full_evaluation.sh /home/chenlu/ChatUniTest_GPT3.5/defect4j_projects/Csv_13_b --backup --inject-jacoco --force-copy
PROJECT_ROOT=${1:-"/home/chenlu/ChatUniTest_GPT3.5/defect4j_projects/Csv_1_b"}
shift || true

BACKUP=false
REMOVE_SOURCE=false
INJECT_JACOCO=false
SKIP_MVN_COMPILE=false
DELETE_ORIGINAL=false
SKIP_COPY=false
FORCE_COPY=false

while (( "$#" )); do
  case "$1" in
    --backup) BACKUP=true; shift;;
    --remove-source) REMOVE_SOURCE=true; shift;;
    --delete-original) DELETE_ORIGINAL=true; shift;;
    --inject-jacoco) INJECT_JACOCO=true; shift;;
    --skip-mvn-compile) SKIP_MVN_COMPILE=true; shift;;
    --skip-copy) SKIP_COPY=true; shift;;
    --force-copy) FORCE_COPY=true; shift;;
    -h|--help) echo "Usage: $0 [PROJECT_ROOT] [--backup] [--remove-source] [--delete-original] [--inject-jacoco] [--skip-mvn-compile] [--skip-copy] [--force-copy]"; exit 0;;
    *) echo "Unknown arg: $1"; exit 1;;
  esac
done

PROJECT_NAME=$(basename "$PROJECT_ROOT")
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
TEST_DIR="$PROJECT_ROOT/src/test/java/org/apache/commons/csv"
POM_XML="$PROJECT_ROOT/pom.xml"

echo "Project: $PROJECT_NAME"
echo "Project root: $PROJECT_ROOT"
echo "Test dir: $TEST_DIR"

# 1) 找到最新的 tests* 目录
NEWEST_TEST_DIR=""
if ls -d "$PROJECT_ROOT"/tests* >/dev/null 2>&1; then
  NEWEST_TEST_DIR=$(ls -td "$PROJECT_ROOT"/tests* 2>/dev/null | head -n1)
fi

# 如果没有找到 tests* 目录：若用户选择跳过复制（--skip-copy）或已有 $TEST_DIR 的测试文件，则继续运行；否则输出警告并继续
if [ -z "$NEWEST_TEST_DIR" ] || [ ! -d "$NEWEST_TEST_DIR" ]; then
  if [ "$SKIP_COPY" = true ]; then
    echo "No tests* directory found; --skip-copy specified -> proceeding without copying"
    NEWEST_TEST_DIR=""
  elif compgen -G "$TEST_DIR/*.java" >/dev/null 2>&1; then
    echo "No tests* directory found; existing test files found in $TEST_DIR -> proceeding without copying"
    NEWEST_TEST_DIR=""
  else
    echo "Warning: no tests* directory found under $PROJECT_ROOT and $TEST_DIR has no test files. Proceeding without copying tests."
    NEWEST_TEST_DIR=""
  fi
else
  echo "Found newest tests dir: $NEWEST_TEST_DIR"
fi

# 2) Prepare TEST_DIR
mkdir -p "$TEST_DIR"

if [ "$BACKUP" = true ] && [ -d "$TEST_DIR" ]; then
  BACKUP_DIR="${TEST_DIR}_backup_$(date +%s)"
  echo "Backing up current $TEST_DIR -> $BACKUP_DIR"
  # 使用复制而非移动，避免误删或替换原有项目测试目录中的文件
  mkdir -p "$(dirname "$BACKUP_DIR")"
  cp -a "$TEST_DIR" "$BACKUP_DIR"
  echo "Backup completed: $BACKUP_DIR"
fi

# 3) Copy .java files into TEST_DIR (flatten into the package folder)
if [ -n "$NEWEST_TEST_DIR" ]; then
  if [ "$SKIP_COPY" = true ]; then
    echo "--skip-copy specified; skipping copying from $NEWEST_TEST_DIR"
  elif compgen -G "$TEST_DIR/*.java" >/dev/null 2>&1 && [ "$FORCE_COPY" != true ]; then
    echo "Existing tests found in $TEST_DIR; skipping copy. Use --force-copy to overwrite."
  else
    JAVA_FILES_FOUND=$(find "$NEWEST_TEST_DIR" -name "*.java" | wc -l)
    if [ "$JAVA_FILES_FOUND" -eq 0 ]; then
      echo "Warning: no .java files found in $NEWEST_TEST_DIR"
    else
      echo "Copying $JAVA_FILES_FOUND java files into $TEST_DIR"
      # Copy all .java files into TEST_DIR (overwrite)
      find "$NEWEST_TEST_DIR" -name "*.java" -exec cp -f {} "$TEST_DIR" \;
    fi
  fi
else
  echo "No tests* directory to copy from; skipping copy step."
fi

# Optionally remove or move source (only if we actually have a source tests dir)
if [ "$REMOVE_SOURCE" = true ] && [ -n "$NEWEST_TEST_DIR" ] && [ -d "$NEWEST_TEST_DIR" ]; then
  if [ "$DELETE_ORIGINAL" = true ]; then
    echo "Deleting original test directory $NEWEST_TEST_DIR"
    rm -rf "$NEWEST_TEST_DIR"
  else
    MOVE_TO="${NEWEST_TEST_DIR}_moved_$(date +%s)"
    echo "Moving original test directory to $MOVE_TO (safe move, no deletion). Use --delete-original to permanently delete."
    mv "$NEWEST_TEST_DIR" "$MOVE_TO"
  fi
elif [ "$REMOVE_SOURCE" = true ]; then
  echo "--remove-source specified but no original tests* dir found; nothing to remove."
fi

# 4) Check pom.xml for jacoco plugin
if [ -f "$POM_XML" ]; then
  if grep -q "artifactId>jacoco-maven-plugin" "$POM_XML" 2>/dev/null; then
    echo "jacoco-maven-plugin already present in pom.xml"
  else
    echo "jacoco-maven-plugin not found in pom.xml"
    if [ "$INJECT_JACOCO" = true ]; then
      echo "Injecting jacoco plugin into pom.xml (backup created)"
      cp "$POM_XML" "$POM_XML.bak_$(date +%s)"
      # Try to insert into an existing <plugins> block; if not exist, add <build><plugins>...</plugins></build> before </project>
      JACOCO_SNIPPET="        <plugin>\n          <groupId>org.jacoco</groupId>\n          <artifactId>jacoco-maven-plugin</artifactId>\n          <version>0.8.11</version>\n          <executions>\n            <execution>\n              <id>prepare-agent</id>\n              <goals>\n                <goal>prepare-agent</goal>\n              </goals>\n            </execution>\n            <execution>\n              <id>report</id>\n              <phase>prepare-package</phase>\n              <goals>\n                <goal>report</goal>\n              </goals>\n            </execution>\n          </executions>\n        </plugin>"
      # Insert jacoco plugin into pom.xml within <build><plugins> if possible, avoiding <pluginManagement>.
      # We try to place the snippet inside the <build> block's <plugins> (creating <plugins> if needed).
      if grep -q "<build" "$POM_XML" 2>/dev/null; then
        build_start=$(grep -n -m1 "<build" "$POM_XML" | cut -d: -f1)
        build_end=$(grep -n -m1 "</build>" "$POM_XML" | cut -d: -f1)

        # Convert escaped newlines into real newlines for safe insertion
        JACOCO_SNIPPET_RAW=$(printf "%b" "$JACOCO_SNIPPET")

        # Search for the last </plugins> inside the build block (to avoid pluginManagement earlier in build)
        plugin_rel_line=$(sed -n "${build_start},${build_end}p" "$POM_XML" | grep -n "</plugins>" | tail -n1 | cut -d: -f1)
        if [ -n "$plugin_rel_line" ]; then
          plugin_abs_line=$((build_start + plugin_rel_line - 1))
          # Insert before the last </plugins> inside the build (so we avoid pluginManagement if it appears earlier)
          awk -v LN="$plugin_abs_line" -v snippet="$JACOCO_SNIPPET_RAW" 'NR==LN{print snippet} {print}' "$POM_XML" > "$POM_XML.tmp" && mv "$POM_XML.tmp" "$POM_XML"
        else
          # No <plugins> inside <build>, insert a new <plugins> block before </build>
          awk -v ENDLN="$build_end" -v snippet="    <plugins>\n$JACOCO_SNIPPET_RAW\n    </plugins>" 'NR==ENDLN{print snippet} {print}' "$POM_XML" > "$POM_XML.tmp" && mv "$POM_XML.tmp" "$POM_XML"
        fi
      else
        # No <build> block, insert a build/plugins block before </project>
        sed -i "s|</project>|  <build>\n    <plugins>\n$JACOCO_SNIPPET\n    </plugins>\n  </build>\n</project>|" "$POM_XML"
      fi
      echo "Injected jacoco plugin into $POM_XML"
    else
      echo "To inject jacoco plugin automatically pass --inject-jacoco, or add the plugin snippet manually (I did not modify pom)."
    fi
  fi
else
  echo "Warning: pom.xml not found at $POM_XML"
fi

# 5) Ensure mvn dependencies and compile (unless skipped)
if [ "$SKIP_MVN_COMPILE" = false ]; then
  if command -v mvn >/dev/null 2>&1; then
    echo "Running mvn -q -DskipTests=true compile"
    (cd "$PROJECT_ROOT" && mvn -q -DskipTests=true compile)
  else
    echo "Warning: mvn not found in PATH. Please run 'mvn compile' in $PROJECT_ROOT"
  fi
else
  echo "Skipping mvn compile as requested"
fi

# 6) Run the consolidated stats script
if [ -f "$SCRIPT_DIR/stats_all_modified.sh" ]; then
  echo "Running stats_all_modified.sh for $PROJECT_ROOT"
  bash "$SCRIPT_DIR/stats_all_modified.sh" "$PROJECT_ROOT"
else
  echo "Error: stats_all_modified.sh not found in $SCRIPT_DIR"
  exit 1
fi

echo "Full project evaluation completed for $PROJECT_NAME"
