import java.awt.*;

public class Pieza {
    private int[][] forma;
    private Color color;
    private int x;
    private int y;

    public Pieza(int[][] forma, Color color) {
        this.forma = forma;
        this.color = color;
        // Posición inicial estándar
        this.x = 4;
        this.y = 0;
    }

    public void draw(Graphics g, int tamanioCelda) {
        g.setColor(color);
        for (int i = 0; i < forma.length; i++) {
            for (int j = 0; j < forma[i].length; j++) {
                if (forma[i][j] == 1) {
                    int posX = (x + j) * tamanioCelda;
                    int posY = (y + i) * tamanioCelda;

                    g.fillRect(posX, posY, tamanioCelda, tamanioCelda);
                    g.setColor(Color.WHITE);
                    g.drawRect(posX, posY, tamanioCelda, tamanioCelda);
                    g.setColor(color);
                }
            }
        }
    }

    // Métodos simples de movimiento
    public void moverAbajo() {
        y++;
    }

    public void moverIzquierda() {
        x--;
    }

    public void moverDerecha() {
        x++;
    }

    // Getters y Setters
    public int[][] getForma() {
        return forma;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getFormaRotada() {
        int n = forma.length;
        int[][] rotada = new int[n][n];
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                rotada[c][n - 1 - f] = forma[f][c];
            }
        }
        return rotada;
    }

    public void setForma(int[][] nuevaForma) {
        this.forma = nuevaForma;
    }
}
