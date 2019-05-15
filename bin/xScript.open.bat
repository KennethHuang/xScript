@echo off
if exist "%XSCRIPT_HOME%\bin\xScript.bat" goto okHome
echo The XSCRIPT_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end

:okHome

java -Xmx512m -Xms512m -jar %XSCRIPT_HOME%\bin\jboss-modules-1.9.1.Final.jar -mp "%XSCRIPT_HOME%\modules" kenh.xscript 

:end