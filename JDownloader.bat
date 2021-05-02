@echo off
if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1

if "%1" == "h" goto begin
mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit
:begin
REM

set "JAVA=%JAVA_HOME%\bin\java.exe"
 
setlocal enabledelayedexpansion
 
set BASE_DIR=%~dp0

set SERVICE_NAME=JDownloader

title %SERVICE_NAME%
call "%JAVA%" -jar %BASE_DIR%\JDownloader.jar