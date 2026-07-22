import java.awt.*;

public class GameLogic {
    private final int rows;
    private final int columns;
    private final Color[][] matriz;
    private int score;

    public GameLogic(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matriz = new Color[rows][columns];
        this.score = 0;
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

    public void securePart(Piece p) {
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
        limpiarLineas();
    }

    private void limpiarLineas() {
        for (int f = rows - 1; f >= 0; f--) {
            boolean llena = true;
            for (int c = 0; c < columns; c++) {
                if (matriz[f][c] == null) {
                    llena = false;
                    break;
                }
            }
            if (llena) {
                eliminarFila(f);
                score += 100;
                f++; // Re-chequear la misma fila
            }
        }
    }

    private void eliminarFila(int fila) {
        for (int f = fila; f > 0; f--) {
            System.arraycopy(matriz[f - 1], 0, matriz[f], 0, columns);
        }
        matriz[0] = new Color[columns];
    }

    public Color[][] getMatriz() {
        return matriz;
    }

    public int getScore() {
        return score;
    }
}