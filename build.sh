#!/usr/bin/env bash
# Compila el proyecto en out/production/TetrisPortfolio
set -e
mkdir -p out/production/TetrisPortfolio
javac -d out/production/TetrisPortfolio src/*.java