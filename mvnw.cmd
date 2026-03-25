@ECHO OFF
SETLOCAL

REM Maven Wrapper batch script for Windows
REM Works with repository-provided Maven distribution under backend\bin\apache-maven-3.9.14

SET "BASE_DIR=%~dp0"

REM Prefer bundled Maven (already committed in this repo)
SET "MAVEN_HOME=%BASE_DIR%bin\apache-maven-3.9.14"
SET "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

IF EXIST "%MAVEN_CMD%" GOTO run

REM Fallback to mvn on PATH
SET "MAVEN_CMD=mvn"

:run
"%MAVEN_CMD%" %*

ENDLOCAL

