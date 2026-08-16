@echo off
rem Compila el proyecto en out/production/TetrisPortfolio
if not exist "out\production\TetrisPortfolio" mkdir "out\production\TetrisPortfolio"
javac -d out/production/TetrisPortfolio src/*.java