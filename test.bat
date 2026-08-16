@echo off
rem Compila y ejecuta la suite de tests
call build.bat
java -cp out/production/TetrisPortfolio TetrisTests