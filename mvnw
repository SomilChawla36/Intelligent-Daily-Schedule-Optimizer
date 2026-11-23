#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper Script for Unix / Linux / macOS
# ----------------------------------------------------------------------------
DIRNAME="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIRNAME/.mvn/wrapper/maven-wrapper.jar"
exec java -Dmaven.multiModuleProjectDirectory="$DIRNAME" -jar "$WRAPPER_JAR" "$@"
