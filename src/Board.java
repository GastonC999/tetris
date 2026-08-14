import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Clase Tablero: Se encarga exclusivamente de la representación gráfica (Vista)
 * y de capturar las entradas del usuario (Controlador).
 */
public class Board extends JPanel {
    private static final int COLUMNS = 10;
    private static final int ROWS = 20;
    private static final int CELL_SIZE = 30;

    private Piece currentPiece;
    private Piece nextPiece;
    private Timer gameTimer;
    private final GameLogic gameLogic;
    private NextPiecePanel nextPiecePanel;

    public Board() {
        gameLogic = new GameLogic(ROWS, COLUMNS);
        setupPanel();
        setupControls();
        nextPiece = PieceFactory.createRandomPiece();
        generateNewPiece();
        startGameLoop();
    }

    private void setupPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
        setFocusable(true);
    }

    private void setupControls() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    private void generateNewPiece() {
        currentPiece = nextPiece;
        nextPiece = PieceFactory.createRandomPiece();
        if (nextPiecePanel != null) {
            nextPiecePanel.setNextPiece(nextPiece);
        }
        //Si al generar una pieza ya hay colisión, el juego termina
        if (gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY(), currentPiece.getShape())) {
            if (gameTimer != null) gameTimer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + gameLogic.getScore());
        }
    }

    public Piece getNextPiece() {
        return nextPiece;
    }

    public void setNextPiecePanel(NextPiecePanel nextPiecePanel) {
        this.nextPiecePanel = nextPiecePanel;
        if (nextPiecePanel != null) {
            nextPiecePanel.setNextPiece(nextPiece);
        }
    }

    private void startGameLoop() {
        gameTimer = new Timer(500, e -> { // Every 500ms a block falls
            moveDown();
            repaint();
        });
        gameTimer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        int x = currentPiece.getX();
        int y = currentPiece.getY();
        int[][] shape = currentPiece.getShape();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (!gameLogic.collisionDetected(x - 1, y, shape)) {
                    currentPiece.moveLeft(); // Mover izquierda
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!gameLogic.collisionDetected(x + 1, y, shape)) {
                    currentPiece.moveRight(); // Mover derecha
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!gameLogic.collisionDetected(x, y + 1, shape)) {
                    currentPiece.moveDown(); // Mover abajo
                }
                break;
            case KeyEvent.VK_UP:
                int[][] rotatedShape = currentPiece.getRotatedShape();
                if (!gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY(), rotatedShape)) {
                    currentPiece.setShape(rotatedShape);
                }
                break;
        }
        repaint(); // Redibuja el panel en la nueva posición
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpiar pantalla

        // 1. Dibujar bloques fijados en la lógica
        Color[][] matrix = gameLogic.getMatriz();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (matrix[r][c] != null) {
                    g.setColor(matrix[r][c]);
                    g.fillRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // 2. Dibujar la pieza que está cayendo actualmente
        currentPiece.draw(g, CELL_SIZE);

        // 3. Dibujar la rejilla (Grid)
        drawGrid(g);

        // 4. Dibujar la informacion UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + gameLogic.getScore(), 10, 20);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        // ibujar lineas verticales
        for (int i = 0; i <= COLUMNS; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, ROWS * CELL_SIZE);
        }
        // Dibujar lineas horizontales
        for (int i = 0; i <= ROWS; i++) {
            g.drawLine(0, i * CELL_SIZE, COLUMNS * CELL_SIZE, i * CELL_SIZE);
        }
    }

    private void moveDown() {
        if (!gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY() + 1, currentPiece.getShape())) {
            currentPiece.moveDown();
        } else {
            // Si colisiona abajo, la fijamos en la matriz lógica
            gameLogic.securePart(currentPiece);
            generateNewPiece();
        }
    }
}
