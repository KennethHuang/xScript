@echo off

if exist "%JAVA_HOME%\bin\java.exe" (
  set "LOCAL_JAVA=%JAVA_HOME%\bin\java.exe"
) else (
  set LOCAL_JAVA=java.exe
)

echo Using java: %LOCAL_JAVA%

:launchs
SET CP="C:\Users\Kennethh\Desktop\D\Huang\Program\xScript\xScript.all\target\xScript.all-1.0.0.jar;.\target\dependency\*;."

"%LOCAL_JAVA%" -cp %CP% kenh.xscript.ScriptUtils %1

:Exit
