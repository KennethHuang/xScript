@echo off
set inPath=%1
if defined inPath (
  goto op1
) else (
  goto tip
)

goto end

:tip

echo Please use -f:{file} or -file:{file} to specify the xScript doc.
goto end

:op1

set fstr=%inPath:~0,3%

if "%fstr%"=="-f:" (
  set fstr_after=%inPath:~3%

  if defined fstr_after ( 
    goto op2 
  ) else (
    goto tip
  )
) else (
  set fistr=%inPath:~0,6%
  
  if "%fistr%"=="-file:" (
    set fistr_after=%inPath:~6%
	
	if defined fistr_after ( 
	  goto op2 
	) else (
	  goto tip
	)
  )
)

goto end

:op2

java -Xmx512m -Xms512m -jar jboss-modules.jar -mp "..\modules" kenh.xscript %1
goto end

:end
