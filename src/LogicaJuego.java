import java.awt.*;

public class LogicaJuego {
    private final int filas;
    private final int columnas;
    private final Color[][] matriz;
    private int puntaje;

    public LogicaJuego(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new Color[filas][columnas];
        this.puntaje = 0;
    }

    public boolean hayColision(int x, int y, int[][] forma) {
        for (int f = 0; f < forma.length; f++) {
            for (int c = 0; c < forma[f].length; c++) {
                if (forma[f][c] != 0) {
                    int px = x + c;
                    int py = y + f;
                    if (px < 0 || px >= columnas || py >= filas) return true;
                    if (py >= 0 && matriz[py][px] != null) return true;
                }
            }
        }
        return false;
    }

    public void fijarPieza(Pieza p) {
        int[][] forma = p.getForma();
        for (int f = 0; f < forma.length; f++) {
            for (int c = 0; c < forma[f].length; c++) {
                if (forma[f][c] != 0) {
                    int py = p.getY() + f;
                    int px = p.getX() + c;
                    if (py >= 0 && py < filas && px >= 0 && px < columnas) {
                        matriz[py][px] = p.getColor();
                    }
                }
            }
        }
        limpiarLineas();
    }

    private void limpiarLineas() {
        for (int f = filas - 1; f >= 0; f--) {
            boolean llena = true;
            for (int c = 0; c < columnas; c++) {
                if (matriz[f][c] == null) {
                    llena = false;
                    break;
                }
            }
            if (llena) {
                eliminarFila(f);
                puntaje += 100;
                f++; // Re-chequear la misma fila
            }
        }
    }

    private void eliminarFila(int fila) {
        for (int f = fila; f > 0; f--) {
            System.arraycopy(matriz[f - 1], 0, matriz[f], 0, columnas);
        }
        matriz[0] = new Color[columnas];
    }

    public Color[][] getMatriz() {
        return matriz;
    }

    public int getPuntaje() {
        return puntaje;
    }
}