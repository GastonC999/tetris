import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fábrica de piezas con estilo neón. Usa un randomizador "7-bag":
 * cada ciclo reparte las 7 piezas sin repetir, evitando rachas injustas.
 */
public class PieceFactory {
    private static final List<Integer> BAG = new ArrayList<>();

    private static final int[][][] SHAPES = {
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}}, // I
            {{1, 0, 0}, {1, 1, 1}, {0, 0, 0}},                 // J
            {{0, 0, 1}, {1, 1, 1}, {0, 0, 0}},                 // L
            {{1, 1}, {1, 1}},                             // O
            {{0, 1, 1}, {1, 1, 0}, {0, 0, 0}},                 // S
            {{0, 1, 0}, {1, 1, 1}, {0, 0, 0}},                 // T
            {{1, 1, 0}, {0, 1, 1}, {0, 0, 0}}                  // Z
    };

    private static final Color[] COLORS = {
            new Color(0x00F5FF), // I cian
            new Color(0x6A5CFF), // J violeta
            new Color(0xFF7B00), // L naranja
            new Color(0xFFE600), // O amarillo
            new Color(0x39FF14), // S verde
            new Color(0xFF00DE), // T magenta
            new Color(0xFF1E56)  // Z rojo
    };

    public static Piece createRandomPiece() {
        if (BAG.isEmpty()) {
            for (int i = 0; i < SHAPES.length; i++) {
                BAG.add(i);
            }
            Collections.shuffle(BAG);
        }
        int idx = BAG.remove(BAG.size() - 1);
        return new Piece(SHAPES[idx], COLORS[idx]);
    }
}