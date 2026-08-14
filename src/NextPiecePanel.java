import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral que muestra una vista previa de la siguiente pieza que caerá.
 */
public class NextPiecePanel extends JPanel {
    private static final int PREVIEW_CELL = 18;
    private static final int MAX_SHAPE_SIZE = 4;
    private static final int PREVIEW_WIDTH = MAX_SHAPE_SIZE * PREVIEW_CELL;
    private static final int PREVIEW_HEIGHT = MAX_SHAPE_SIZE * PREVIEW_CELL;
    private static final int PADDING = 15;

    private Piece nextPiece;

    public NextPiecePanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(PREVIEW_WIDTH + PADDING * 2, PREVIEW_HEIGHT + PADDING * 2 + 25));
    }

    public void setNextPiece(Piece nextPiece) {
        this.nextPiece = nextPiece;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Next", PADDING, 22);

        int boxX = PADDING;
        int boxY = 30;

        drawPreviewGrid(g, boxX, boxY);

        if (nextPiece != null) {
            int[][] shape = nextPiece.getShape();
            int shapeWidth = shape[0].length * PREVIEW_CELL;
            int shapeHeight = shape.length * PREVIEW_CELL;
            int originX = boxX + (PREVIEW_WIDTH - shapeWidth) / 2;
            int originY = boxY + (PREVIEW_HEIGHT - shapeHeight) / 2;
            nextPiece.drawAt(g, PREVIEW_CELL, originX, originY);
        }
    }

    private void drawPreviewGrid(Graphics g, int boxX, int boxY) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= MAX_SHAPE_SIZE; i++) {
            g.drawLine(boxX + i * PREVIEW_CELL, boxY, boxX + i * PREVIEW_CELL, boxY + PREVIEW_HEIGHT);
            g.drawLine(boxX, boxY + i * PREVIEW_CELL, boxX + PREVIEW_WIDTH, boxY + i * PREVIEW_CELL);
        }
    }
}
