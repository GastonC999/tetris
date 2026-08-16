import java.awt.*;

/**
 * Entidad tetramino: forma (matriz), color, posición y dibujado neón.
 */
public class Piece {
    private int[][] shape;
    private final Color color;
    private int x;
    private int y;

    public Piece(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        // Posición inicial estándar, centrada horizontalmente
        this.x = (10 - shape[0].length) / 2;
        this.y = 0;
    }

    public void draw(Graphics g, int cellSize) {
        dibujarForma(g, cellSize, x, y);
    }

    public void drawAt(Graphics g, int cellSize, int originX, int originY) {
        dibujarForma(g, cellSize, originX, originY);
    }

    private void dibujarForma(Graphics g, int cellSize, int originX, int originY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    int px = originX + j * cellSize;
                    int py = originY + i * cellSize;
                    dibujarBloque(g2, px, py, cellSize, color);
                }
            }
        }
    }

    /** Dibuja un bloque neón sólido (reutilizado por el tablero y los paneles laterales). */
    public static void dibujarBloque(Graphics2D g2, int px, int py, int cellSize, Color color) {
        g2.setColor(color);
        g2.fillRoundRect(px + 1, py + 1, cellSize - 2, cellSize - 2, 6, 6);
        // Borde brillante (glow)
        g2.setColor(color.brighter());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(px + 2, py + 2, cellSize - 5, cellSize - 5, 6, 6);
        g2.setStroke(new BasicStroke(1f));
        // Brillo superior-izquierdo
        g2.setColor(new Color(255, 255, 255, 80));
        g2.fillRect(px + 3, py + 3, cellSize - 6, 2);
        g2.fillRect(px + 3, py + 3, 2, cellSize - 6);
        // Sombra inferior-derecha
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRect(px + 3, py + cellSize - 5, cellSize - 6, 2);
        g2.fillRect(px + cellSize - 5, py + 3, 2, cellSize - 6);
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

    public void resetPosition() {
        this.x = (10 - shape[0].length) / 2;
        this.y = 0;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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