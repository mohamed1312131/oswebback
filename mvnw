#!/bin/sh
#
# Minimal Maven Wrapper bootstrap script.
#
# This project expects the Maven Wrapper JAR to be present at:
#   .mvn/wrapper/maven-wrapper.jar
# If it is missing, generate it using a Maven installation:
#   mvn -N wrapper:wrapper
#

BASEDIR=$(cd "$(dirname "$0")" && pwd)
WRAPPER_JAR="$BASEDIR/.mvn/wrapper/maven-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Maven Wrapper jar not found. Please ensure .mvn/wrapper/maven-wrapper.jar exists."
  exit 1
fi

exec java -classpath "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"

