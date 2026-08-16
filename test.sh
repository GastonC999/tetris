#!/usr/bin/env bash
# Compila y ejecuta la suite de tests
set -e
./build.sh
java -cp out/production/TetrisPortfolio TetrisTests