import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase Tablero: se encarga de la representación gráfica (Vista),
 * del ciclo de juego (javax.swing.Timer) y de las entradas del usuario
 * (Controlador) mediante Key Bindings.
 */
public class Board extends JPanel {
    private static final int COLUMNS = 10;
    private static final int ROWS = 20;
    private static final int CELL_SIZE = 30;

    private static final Color BG_TOP = new Color(0x0d0d1a);
    private static final Color BG_BOTTOM = new Color(0x1a0b2e);
    private static final Color GRID_COLOR = new Color(255, 255, 255, 20);
    private static final Color NEON = new Color(0x00F5FF);

    private Piece currentPiece;
    private Piece nextPiece;
    private Piece holdPiece;
    private GameLogic gameLogic;
    private Timer gameTimer;
    private Timer flashTimer;
    private SidePanel sidePanel;

    private boolean pausado;
    private boolean juegoTerminado;
    private boolean canHold = true;

    private List<Integer> flashRows = Collections.emptyList();
    private int flashTick;

    public Board() {
        gameLogic = new GameLogic(ROWS, COLUMNS);
        setupPanel();
        setupControls();
        nextPiece = PieceFactory.createRandomPiece();
        generateNewPiece();
        startGameLoop();
        startFlashTimer();
    }

    private void setupPanel() {
        setBackground(BG_TOP);
        setPreferredSize(new Dimension(COLUMNS * CELL_SIZE, ROWS * CELL_SIZE));
        setFocusable(true);
    }

    private void setupControls() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moverIzquierda");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moverIzquierda");
        am.put("moverIzquierda", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoActivo()) moverIzquierda();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moverDerecha");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moverDerecha");
        am.put("moverDerecha", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoActivo()) moverDerecha();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "bajar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "bajar");
        am.put("bajar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoActivo()) bajar();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "rotar");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "rotar");
        am.put("rotar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoActivo()) rotar();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "hardDrop");
        am.put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (juegoActivo()) hardDrop();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pausa");
        am.put("pausa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausar();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "hold");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0), "hold");
        am.put("hold", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarHold();
            }
        });
    }

    private void generateNewPiece() {
        currentPiece = nextPiece;
        nextPiece = PieceFactory.createRandomPiece();
        // DEBUG temporal: mostrar dónde nace cada pieza
        System.out.println("DEBUG NUEVA PIEZA x=" + currentPiece.getX() + " y=" + currentPiece.getY());
        // Si al generar una pieza ya hay colisión, el juego termina
        if (gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY(), currentPiece.getShape())) {
            juegoTerminado = true;
            gameOver();
            return;
        }
        actualizarPanel();
    }

    private void fijarPieza() {
        int[] filas = gameLogic.securePart(currentPiece);
        if (filas.length > 0) {
            startFlash(filas);
            actualizarVelocidad();
        }
        canHold = true;
        generateNewPiece();
    }

    private void gameOver() {
        gameTimer.stop();
        if (flashTimer != null) {
            flashTimer.stop();
        }
        HighScore.registrarPuntaje(gameLogic.getScore());
        actualizarPanel();
        repaint();

        int opcion = JOptionPane.showOptionDialog(this,
                "¡Game Over!\nPuntaje final: " + gameLogic.getScore()
                        + "\nMejor puntaje: " + HighScore.getMejorPuntaje(),
                "Tetris", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Jugar de nuevo", "Salir"}, "Jugar de nuevo");

        if (opcion == JOptionPane.YES_OPTION) {
            reiniciar();
        } else {
            Window ventana = SwingUtilities.getWindowAncestor(this);
            if (ventana != null) {
                ventana.dispose();
            }
        }
    }

    private void reiniciar() {
        gameLogic = new GameLogic(ROWS, COLUMNS);
        holdPiece = null;
        canHold = true;
        pausado = false;
        juegoTerminado = false;
        flashRows = Collections.emptyList();
        flashTick = 0;
        if (flashTimer != null) {
            flashTimer.stop();
        }
        nextPiece = PieceFactory.createRandomPiece();
        generateNewPiece();
        gameTimer.setDelay(500);
        gameTimer.start();
        repaint();
    }

    private void startGameLoop() {
        gameTimer = new Timer(500, e -> {
            if (juegoTerminado || pausado) return;
            moveDown();
            // DEBUG temporal: verificar que la pieza cae (y debe aumentar por tick)
            System.out.println("DEBUG tick x=" + currentPiece.getX() + " y=" + currentPiece.getY()
                    + " tierra=" + gameLogic.getDropY(currentPiece));
            repaint();
        });
        gameTimer.start();
    }

    private void startFlashTimer() {
        flashTimer = new Timer(30, e -> {
            if (flashTick <= 0) {
                flashTimer.stop();
                return;
            }
            flashTick--;
            repaint();
        });
    }

    private void startFlash(int[] filas) {
        flashRows = new ArrayList<>();
        for (int f : filas) {
            flashRows.add(f);
        }
        flashTick = 8;
        if (flashTimer != null) {
            flashTimer.restart();
        }
    }

    private void actualizarVelocidad() {
        int delay = Math.max(90, 500 - (gameLogic.getLevel() - 1) * 35);
        gameTimer.setDelay(delay);
    }

    private boolean juegoActivo() {
        return !juegoTerminado && !pausado;
    }

    // ---- Acciones ----

    private void moverIzquierda() {
        int x = currentPiece.getX();
        if (!gameLogic.collisionDetected(x - 1, currentPiece.getY(), currentPiece.getShape())) {
            currentPiece.moveLeft();
        }
        repaint();
    }

    private void moverDerecha() {
        int x = currentPiece.getX();
        if (!gameLogic.collisionDetected(x + 1, currentPiece.getY(), currentPiece.getShape())) {
            currentPiece.moveRight();
        }
        repaint();
    }

    private void bajar() {
        if (!gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY() + 1, currentPiece.getShape())) {
            currentPiece.moveDown();
        }
        repaint();
    }

    private void rotar() {
        int[][] rotada = currentPiece.getRotatedShape();
        int x = currentPiece.getX();
        int[] kicks = {0, -1, 1, -2, 2};
        for (int dx : kicks) {
            if (!gameLogic.collisionDetected(x + dx, currentPiece.getY(), rotada)) {
                currentPiece.setShape(rotada);
                currentPiece.setX(x + dx);
                break;
            }
        }
        repaint();
    }

    private void hardDrop() {
        int distancia = 0;
        while (!gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY() + 1, currentPiece.getShape())) {
            currentPiece.moveDown();
            distancia++;
        }
        gameLogic.addScore(distancia * 2 * gameLogic.getLevel());
        fijarPieza();
        repaint();
    }

    private void pausar() {
        if (juegoTerminado) return;
        pausado = !pausado;
        if (pausado) {
            gameTimer.stop();
        } else {
            gameTimer.start();
        }
        repaint();
    }

    private void cambiarHold() {
        if (!canHold || juegoTerminado) return;
        canHold = false;
        if (holdPiece == null) {
            holdPiece = currentPiece;
            currentPiece = nextPiece;
            nextPiece = PieceFactory.createRandomPiece();
        } else {
            Piece temporal = holdPiece;
            holdPiece = currentPiece;
            currentPiece = temporal;
            currentPiece.resetPosition();
        }
        if (gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY(), currentPiece.getShape())) {
            juegoTerminado = true;
            gameOver();
            return;
        }
        actualizarPanel();
        repaint();
    }

    private void moveDown() {
        if (!gameLogic.collisionDetected(currentPiece.getX(), currentPiece.getY() + 1, currentPiece.getShape())) {
            currentPiece.moveDown();
        } else {
            fijarPieza();
        }
    }

    // ---- Panel lateral ----

    public void setSidePanel(SidePanel sidePanel) {
        this.sidePanel = sidePanel;
        if (sidePanel != null) {
            actualizarPanel();
        }
    }

    private void actualizarPanel() {
        if (sidePanel != null) {
            sidePanel.setNextPiece(nextPiece);
            sidePanel.setHoldPiece(holdPiece);
            sidePanel.actualizarInfo(gameLogic.getScore(), gameLogic.getLines(),
                    gameLogic.getLevel(), HighScore.getMejorPuntaje());
        }
    }

    // ---- Renderizado ----

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        pintarFondo(g2);

        // 1. Bloques fijados en la lógica
        Color[][] matrix = gameLogic.getMatriz();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                if (matrix[r][c] != null) {
                    Piece.dibujarBloque(g2, c * CELL_SIZE, r * CELL_SIZE, CELL_SIZE, matrix[r][c]);
                }
            }
        }

        // 2. Flash de líneas limpiadas
        if (flashTick > 0) {
            float alpha = flashTick / 8f;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.WHITE);
            for (int fila : flashRows) {
                g2.fillRect(0, fila * CELL_SIZE, COLUMNS * CELL_SIZE, CELL_SIZE);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        // 3. Pieza que cae actualmente
        currentPiece.draw(g2, CELL_SIZE);

        // 4. Overlay de pausa
        if (pausado) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g2.setColor(NEON);
            g2.setFont(new Font("Monospaced", Font.BOLD, 28));
            String mensaje = "PAUSADO";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(mensaje, (getWidth() - fm.stringWidth(mensaje)) / 2, getHeight() / 2);
        }
    }

    private void pintarFondo(Graphics2D g2) {
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(GRID_COLOR);
        for (int i = 0; i <= COLUMNS; i++) {
            g2.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, ROWS * CELL_SIZE);
        }
        for (int i = 0; i <= ROWS; i++) {
            g2.drawLine(0, i * CELL_SIZE, COLUMNS * CELL_SIZE, i * CELL_SIZE);
        }
    }
}