# 🎮 Java Swing Tetris

Un juego clásico de **Tetris** desarrollado en Java utilizando la librería gráfica Swing y principios de programación orientada a objetos (POO). El proyecto implementa la mecánica completa de juego: colisiones avanzadas, física de caída, rotación de tetraminos, detección de líneas completadas y un sistema de puntaje.

---

## 🚀 Características Principales

* **7 Tetraminos Clásicos:** Incluye todas las formas oficiales (I, J, L, O, S, T, Z) con sus respectivos colores.
* **Control de Piezas:** Movimiento lateral, caída acelerada y rotación de piezas en sentido horario.
* **Motor de Colisiones:** Manejo estricto de límites de pantalla (bordes laterales y piso) y colisión contra piezas previamente fijadas.
* **Game Loop & Gravedad:** Implementación de un temporizador en segundo plano (`javax.swing.Timer`) para controlar la caída continua.
* **Limpieza de Líneas & Puntaje:** Identificación de filas completas, desplazamiento de bloques superiores y otorgamiento de puntos según líneas destruidas.
* **Game Over:** Detección automática de saturación en la matriz superior e interrupción de la partida.

---

## 🛠️ Tecnologías y Herramientas Utilizadas

* **Lenguaje:** Java (SE)
* **Librería Gráfica:** `javax.swing` y `java.awt` (Renderizado con `Graphics` / `Graphics2D`).
* **Manejo de Eventos:** `KeyListener` y `KeyAdapter` para la entrada de teclado.
* **Temporización:** `javax.swing.Timer` para el ciclo de vida del juego (*Game Loop*).

---

## 🏛️ Arquitectura del Proyecto

El código está refactorizado aplicando principios de **Clean Code** y **Separación de Responsabilidades**:

* **`Pieza.java`:** Modela la entidad del tetramino. Contiene su forma (matriz), color, posición y los métodos para manipular su estado (movimiento y rotación).
* **`LogicaJuego.java`:** El "cerebro" del juego. Gestiona el estado de la matriz del tablero (`Color[][]`), procesa la validación de colisiones, la eliminación de líneas completas y el cálculo del puntaje. Es independiente de la interfaz gráfica.
* **`PiezaFactory.java`:** Implementa el patrón *Factory* para centralizar la creación de piezas. Define las formas y colores oficiales y entrega nuevas instancias aleatorias.
* **`Tablero.java`:** Actúa como la **Vista** y el **Controlador**.
    * Hereda de `JPanel` para el renderizado gráfico.
    * Orquestra el flujo del juego mediante un `javax.swing.Timer`.
    * Captura y delega las entradas de teclado a la lógica correspondiente.
    * Dibuja el estado actual procesando la información provista por `LogicaJuego`.
* **`Main.java`:** Punto de entrada de la aplicación que configura la ventana principal (`JFrame`).

---

## ⌨️ Controles

| Tecla | Acción |
| :--- | :--- |
| **⬅️ Flecha Izquierda** | Mover pieza a la izquierda |
| **➡️ Flecha Derecha** | Mover pieza a la derecha |
| **⬇️ Flecha Abajo** | Acelerar caída |
| **⬆️ Flecha Arriba** | Rotar pieza 90° |

---

## 💻 Cómo Ejecutar el Proyecto

1. Cloná este repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/TU_REPOSITTORIO.git](https://github.com/TU_USUARIO/TU_REPOSITTORIO.git)