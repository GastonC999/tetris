import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Modelo del juego: matriz del tablero, colisiones, limpieza de líneas,
 * puntaje, nivel y líneas. Independiente de la interfaz gráfica.
 */
public class GameLogic {
    private final int rows;
    private final int columns;
    private Color[][] matriz;
    private int score;
    private int lines;

    // Bonus por cantidad de líneas limpiadas a la vez (1, 2, 3 o 4 líneas)
    private static final int[] BONUS = {0, 100, 300, 500, 800};

    public GameLogic(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matriz = new Color[rows][columns];
        this.score = 0;
        this.lines = 0;
    }

    public boolean collisionDetected(int x, int y, int[][] forma) {
        for (int f = 0; f < forma.length; f++) {
            for (int c = 0; c < forma[f].length; c++) {
                if (forma[f][c] != 0) {
                    int px = x + c;
                    int py = y + f;
                    if (px < 0 || px >= columns || py >= rows) return true;
                    if (py >= 0 && matriz[py][px] != null) return true;
                }
            }
        }
        return false;
    }

    /** Calcula la fila en la que aterrizaría la pieza (para el ghost piece). */
    public int getDropY(Piece p) {
        int y = p.getY();
        while (!collisionDetected(p.getX(), y + 1, p.getShape())) {
            y++;
        }
        return y;
    }

    /**
     * Fija la pieza en la matriz y limpia líneas completas.
     * Devuelve las filas limpiadas (para animación), o un arreglo vacío.
     */
    public int[] securePart(Piece p) {
        int[][] forma = p.getShape();
        for (int f = 0; f < forma.length; f++) {
            for (int c = 0; c < forma[f].length; c++) {
                if (forma[f][c] != 0) {
                    int py = p.getY() + f;
                    int px = p.getX() + c;
                    if (py >= 0 && py < rows && px >= 0 && px < columns) {
                        matriz[py][px] = p.getColor();
                    }
                }
            }
        }
        return limpiarLineas();
    }

    private int[] limpiarLineas() {
        List<Integer> filas = new ArrayList<>();
        for (int f = rows - 1; f >= 0; f--) {
            if (filaLlena(f)) {
                filas.add(f);
            }
        }
        if (!filas.isEmpty()) {
            eliminarFilas(filas);
            int n = filas.size();
            score += BONUS[Math.min(n, 4)] * getLevel();
            lines += n;
        }
        int[] resultado = new int[filas.size()];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = filas.get(i);
        }
        return resultado;
    }

    private boolean filaLlena(int f) {
        for (int c = 0; c < columns; c++) {
            if (matriz[f][c] == null) return false;
        }
        return true;
    }

    private void eliminarFilas(List<Integer> filas) {
        Set<Integer> set = new HashSet<>(filas);
        Color[][] nueva = new Color[rows][columns];
        int destino = rows - 1;
        for (int f = rows - 1; f >= 0; f--) {
            if (!set.contains(f)) {
                nueva[destino--] = matriz[f];
            }
        }
        for (int f = destino; f >= 0; f--) {
            nueva[f] = new Color[columns];
        }
        matriz = nueva;
    }

    public Color[][] getMatriz() {
        Color[][] copia = new Color[rows][columns];
        for (int f = 0; f < rows; f++) {
            System.arraycopy(matriz[f], 0, copia[f], 0, columns);
        }
        return copia;
    }

    public void addScore(int puntos) {
        score += puntos;
    }

    public int getScore() {
        return score;
    }

    public int getLines() {
        return lines;
    }

    public int getLevel() {
        return lines / 10 + 1;
    }
}