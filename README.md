# 🎮 Java Swing Tetris

Un juego clásico de **Tetris** desarrollado en Java utilizando la librería gráfica Swing y principios de programación orientada a objetos (POO). El proyecto implementa la mecánica completa de juego: colisiones avanzadas, física de caída, rotación de tetraminos, detección de líneas completadas y un sistema de puntaje.

---

## 🚀 Características Principales

* **7 Tetraminos Clásicos:** Incluye todas las formas oficiales (I, J, L, O, S, T, Z) con estilo **neón** y randomizador *7-bag* (sin rachas de piezas repetidas).
* **Control de Piezas:** Movimiento lateral, caída acelerada, rotación con *wall kicks* y **hard drop** (caída instantánea).
* **Motor de Colisiones:** Manejo estricto de límites de pantalla (bordes laterales y piso) y colisión contra piezas previamente fijadas.
* **Game Loop & Gravedad:** Implementación de un temporizador en segundo plano (`javax.swing.Timer`) con **velocidad progresiva por nivel**.
* **Limpieza de Líneas & Puntaje:** Bonus por líneas simultáneas (100/300/500/800 × nivel), con animación de *flash* al completarlas.
* **Sistema de Niveles:** Cada 10 líneas sube el nivel y aumenta la velocidad de caída.
* **Ghost Piece:** Sombra translúcida que muestra dónde aterrizará la pieza.
* **Hold Piece:** Guarda una pieza para usarla más adelante (una vez por pieza).
* **Pausa y Reinicio:** Pausa en cualquier momento y opción de "Jugar de nuevo" al terminar.
* **Récord Persistente:** El mejor puntaje se guarda en `~/.tetris_highscore.txt`.
* **HUD Lateral:** Panel con score, nivel, líneas y récord, además de las vistas previas de la próxima pieza y del hold.

---

## 🛠️ Tecnologías y Herramientas Utilizadas

* **Lenguaje:** Java (SE)
* **Librería Gráfica:** `javax.swing` y `java.awt` (Renderizado con `Graphics` / `Graphics2D`).
* **Manejo de Eventos:** `KeyListener` y `KeyAdapter` para la entrada de teclado.
* **Temporización:** `javax.swing.Timer` para el ciclo de vida del juego (*Game Loop*).

---

## 🏛️ Arquitectura del Proyecto

El código está refactorizado aplicando principios de **Clean Code** y **Separación de Responsabilidades**:

* **`Piece.java`:** Modela la entidad del tetramino. Contiene su forma (matriz), color, posición y los métodos para manipular su estado (movimiento y rotación). Incluye `drawAt()` para dibujar en coordenadas libres (vista previa) y `drawGhost()` para la sombra de aterrizaje. El render usa `Graphics2D` con estilo neón.
* **`GameLogic.java`:** El "cerebro" del juego. Gestiona el estado de la matriz del tablero (`Color[][]`), procesa la validación de colisiones, la eliminación de líneas completas, el puntaje, las líneas y el nivel. Es independiente de la interfaz gráfica.
* **`PieceFactory.java`:** Implementa el patrón *Factory* con randomizador *7-bag*: reparte las 7 piezas por ciclo sin repetir.
* **`Board.java`:** Actúa como la **Vista** y el **Controlador**.
    * Hereda de `JPanel` para el renderizado gráfico.
    * Orquestra el flujo del juego mediante un `javax.swing.Timer`.
    * Captura las entradas mediante **Key Bindings** (`InputMap`/`ActionMap`).
    * Dibuja fondo con gradiente, bloques fijos, ghost piece, flash de líneas y overlay de pausa.
    * Reserva de antemano la siguiente pieza (`nextPiece`) y la notifica al `SidePanel`.
* **`SidePanel.java`:** Panel lateral con el HUD (score, nivel, líneas, récord) y las vistas previas de la próxima pieza y del hold.
* **`HighScore.java`:** Persiste el mejor puntaje en `~/.tetris_highscore.txt`.
* **`TetrisTests.java`:** Suite de tests ligera (sin framework) para `GameLogic` y `Piece`.
* **`Main.java`:** Punto de entrada de la aplicación que configura la ventana principal (`JFrame`) con un `BorderLayout`: el tablero al centro y el panel lateral al este.

---

## ⌨️ Controles

| Tecla | Acción |
| :--- | :--- |
| **⬅️ / ➡️ (o A/D)** | Mover pieza a la izquierda / derecha |
| **⬇️ (o S)** | Acelerar caída |
| **⬆️ (o W)** | Rotar pieza 90° |
| **Espacio** | Caída instantánea (hard drop) |
| **C / Shift** | Guardar o intercambiar pieza en el hold |
| **P** | Pausar / reanudar |

---

## 💻 Cómo Ejecutar el Proyecto

1. Cloná este repositorio:
   ```bash
   git clone https://github.com/GastonC999/tetris.git
   ```
2. Compilá y ejecutá:
   ```bash
   javac -d out/production/TetrisPortfolio src/*.java
   java -cp out/production/TetrisPortfolio Main
   ```
   O usá los scripts incluidos: `run.bat`/`run.sh` (compila y ejecuta), `test.bat`/`test.sh` (corre los tests), `package.bat`/`package.sh` (genera `TetrisPortfolio.jar`).