#!/usr/bin/env bash
# Empaqueta el juego en un .jar ejecutable (detecta la ruta del JDK)
set -e
./build.sh
JAVA_HOME="${JAVA_HOME:-$(java -XshowSettings:properties -version 2>&1 | grep -oP 'java.home = \K.*')}"
"$JAVA_HOME/bin/jar" cfe TetrisPortfolio.jar Main -C out/production/TetrisPortfolio .