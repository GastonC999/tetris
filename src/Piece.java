import java.awt.*;

public class Piece {
    private int[][] shape;
    private final Color color;
    private int x;
    private int y;

    public Piece(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        // Posición inicial estándar
        this.x = 4;
        this.y = 0;
    }

    public void draw(Graphics g, int cellSize) {
        g.setColor(color);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int posX = (x + j) * cellSize;
                    int posY = (y + i) * cellSize;

                    g.fillRect(posX, posY, cellSize, cellSize);
                    g.setColor(Color.WHITE);
                    g.drawRect(posX, posY, cellSize, cellSize);
                    g.setColor(color);
                }
            }
        }
    }

    // Métodos simples de movimiento
    public void moveDown() {
        y++;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    // Getters y Setters
    public int[][] getShape() {
        return shape;
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

    public int[][] getRotatedShape() {
        int n = shape.length;
        int[][] rotada = new int[n][n];
        for (int f = 0; f < n; f++) {
            for (int c = 0; c < n; c++) {
                rotada[c][n - 1 - f] = shape[f][c];
            }
        }
        return rotada;
    }

    public void setShape(int[][] newShape) {
        this.shape = newShape;
    }
}