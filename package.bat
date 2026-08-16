@echo off
setlocal
rem Empaqueta el juego en un .jar ejecutable (detecta la ruta del JDK)
call build.bat
if not defined JAVA_HOME (
    for /f "tokens=2 delims== " %%j in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr /i "java.home"') do set "JAVA_HOME=%%j"
)
"%JAVA_HOME%\bin\jar" cfe TetrisPortfolio.jar Main -C out/production/TetrisPortfolio .