import java.awt.*;
import java.util.Random;

public class PiezaFactory {
    private static final Random random = new Random();

    private static final int[][][] FORMAS = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}}, // I
            {{1, 0, 0}, {1, 1, 1}, {0, 0, 0}},                 // J
            {{0, 0, 1}, {1, 1, 1}, {0, 0, 0}},                 // L
            {{1, 1}, {1, 1}},                             // O
            {{0, 1, 1}, {1, 1, 0}, {0, 0, 0}},                 // S
            {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}},                 // T
            {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}}                  // Z
    };

    private static final Color[] COLORES = {
            Color.CYAN, Color.BLUE, Color.ORANGE,
            Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
    };

    public static Pieza crearPiezaAleatoria() {
        int i = random.nextInt(FORMAS.length);
        return new Pieza(FORMAS[i], COLORES[i]);
    }
}