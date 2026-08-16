import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral con el HUD (score, nivel, líneas, récord), la vista previa
 * de la siguiente pieza y la pieza en hold.
 */
public class SidePanel extends JPanel {
    private static final int PADDING = 15;
    private static final int PREVIEW_CELL = 18;
    private static final int MAX_SHAPE_SIZE = 4;
    private static final int PREVIEW_WIDTH = MAX_SHAPE_SIZE * PREVIEW_CELL;
    private static final int PREVIEW_HEIGHT = MAX_SHAPE_SIZE * PREVIEW_CELL;

    private static final Color BG = new Color(0x0d0d1a);
    private static final Color TITULO = new Color(0x00F5FF);
    private static final Color TEXTO = new Color(0xE8E8F0);
    private static final Color TEXTO_DIM = new Color(0x8A8AA0);
    private static final Color CAJA = new Color(0x161627);
    private static final Color BORDE = new Color(0x2a2a4a);

    private Piece nextPiece;
    private Piece holdPiece;
    private int score;
    private int lines;
    private int level;
    private int best;

    public SidePanel() {
        setBackground(BG);
        setPreferredSize(new Dimension(PREVIEW_WIDTH + PADDING * 2, 600));
    }

    public void setNextPiece(Piece nextPiece) {
        this.nextPiece = nextPiece;
        repaint();
    }

    public void setHoldPiece(Piece holdPiece) {
        this.holdPiece = holdPiece;
        repaint();
    }

    public void actualizarInfo(int score, int lines, int level, int best) {
        this.score = score;
        this.lines = lines;
        this.level = level;
        this.best = best;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(TITULO);
        g2.drawString("TETRIS", PADDING, 26);

        int y = 52;
        y = dibujarDato(g2, y, "SCORE", String.valueOf(score));
        y = dibujarDato(g2, y, "NIVEL", String.valueOf(level));
        y = dibujarDato(g2, y, "LINEAS", String.valueOf(lines));
        y = dibujarDato(g2, y, "RECORD", String.valueOf(best));

        y += 12;
        y = dibujarCaja(g2, y, "SIGUIENTE", nextPiece);
        y = dibujarCaja(g2, y, "HOLD", holdPiece);

        y += 8;
        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2.setColor(TEXTO_DIM);
        String[] ayuda = {"← → Mover", "↑ Rotar", "↓ Bajar", "ESPACIO Hard drop", "C Hold", "P Pausa"};
        for (String linea : ayuda) {
            g2.drawString(linea, PADDING, y);
            y += 16;
        }
    }

    private int dibujarDato(Graphics2D g2, int y, String etiqueta, String valor) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2.setColor(TEXTO_DIM);
        g2.drawString(etiqueta, PADDING, y);
        g2.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2.setColor(TEXTO);
        g2.drawString(valor, PADDING, y + 18);
        return y + 34;
    }

    private int dibujarCaja(Graphics2D g2, int y, String titulo, Piece pieza) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2.setColor(TEXTO_DIM);
        g2.drawString(titulo, PADDING, y);

        int boxY = y + 8;
        g2.setColor(CAJA);
        g2.fillRoundRect(PADDING, boxY, PREVIEW_WIDTH, PREVIEW_HEIGHT, 10, 10);
        g2.setColor(BORDE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(PADDING, boxY, PREVIEW_WIDTH, PREVIEW_HEIGHT, 10, 10);
        g2.setStroke(new BasicStroke(1f));

        if (pieza != null) {
            int[][] shape = pieza.getShape();
            int shapeWidth = shape[0].length * PREVIEW_CELL;
            int shapeHeight = shape.length * PREVIEW_CELL;
            int originX = PADDING + (PREVIEW_WIDTH - shapeWidth) / 2;
            int originY = boxY + (PREVIEW_HEIGHT - shapeHeight) / 2;
            pieza.drawAt(g2, PREVIEW_CELL, originX, originY);
        } else {
            g2.setFont(new Font("Monospaced", Font.ITALIC, 11));
            g2.setColor(TEXTO_DIM);
            g2.drawString("VACIO", PADDING + PREVIEW_WIDTH / 2 - 22, boxY + PREVIEW_HEIGHT / 2 + 4);
        }
        return boxY + PREVIEW_HEIGHT + 18;
    }
}