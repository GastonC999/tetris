import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Clase Tablero: Se encarga exclusivamente de la representación gráfica (Vista)
 * y de capturar las entradas del usuario (Controlador).
 */
public class Tablero extends JPanel {
    private static final int COLUMNAS = 10;
    private static final int FILAS = 20;
    private static final int TAMANIO_CELDA = 30;

    private Pieza piezaActual;
    private Timer timer;
    private final LogicaJuego logica;

    public Tablero() {
        logica = new LogicaJuego(FILAS, COLUMNAS);
        configurarPanel();
        configurarControles();
        generarNuevaPieza();
        iniciarGameLoop();
    }

    private void configurarPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(COLUMNAS * TAMANIO_CELDA, FILAS * TAMANIO_CELDA));
        setFocusable(true);
    }

    private void configurarControles() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    private void generarNuevaPieza() {
        piezaActual = PiezaFactory.crearPiezaAleatoria();
        // Si al generar una pieza ya hay colisión, el juego termina
        if (logica.hayColision(piezaActual.getX(), piezaActual.getY(), piezaActual.getForma())) {
            if (timer != null) timer.stop();
            JOptionPane.showMessageDialog(this, "¡Game Over! Puntaje final: " + logica.getPuntaje());
        }
    }

    private void iniciarGameLoop() {
        timer = new Timer(500, e -> {
            moverAbajo();
            repaint();
        });
        timer.start();
    }

    private void handleKeyPress(KeyEvent e) {
        int x = piezaActual.getX();
        int y = piezaActual.getY();
        int[][] forma = piezaActual.getForma();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (!logica.hayColision(x - 1, y, forma)) {
                    piezaActual.moverIzquierda();
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (!logica.hayColision(x + 1, y, forma)) {
                    piezaActual.moverDerecha();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (!logica.hayColision(x, y + 1, forma)) {
                    piezaActual.moverAbajo();
                }
                break;
            case KeyEvent.VK_UP:
                int[][] formaRotada = piezaActual.getFormaRotada();
                if (!logica.hayColision(piezaActual.getX(), piezaActual.getY(), formaRotada)) {
                    piezaActual.setForma(formaRotada);
                }
                break;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Dibujar bloques fijados en la lógica
        Color[][] matriz = logica.getMatriz();
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                if (matriz[f][c] != null) {
                    g.setColor(matriz[f][c]);
                    g.fillRect(c * TAMANIO_CELDA, f * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(c * TAMANIO_CELDA, f * TAMANIO_CELDA, TAMANIO_CELDA, TAMANIO_CELDA);
                }
            }
        }

        // 2. Dibujar la pieza que está cayendo actualmente
        piezaActual.draw(g, TAMANIO_CELDA);

        // 3. Dibujar la rejilla (Grid)
        drawGrid(g);

        // 4. Dibujar información de la UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Puntaje: " + logica.getPuntaje(), 10, 20);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= COLUMNAS; i++) {
            g.drawLine(i * TAMANIO_CELDA, 0, i * TAMANIO_CELDA, FILAS * TAMANIO_CELDA);
        }
        for (int i = 0; i <= FILAS; i++) {
            g.drawLine(0, i * TAMANIO_CELDA, COLUMNAS * TAMANIO_CELDA, i * TAMANIO_CELDA);
        }
    }

    private void moverAbajo() {
        if (!logica.hayColision(piezaActual.getX(), piezaActual.getY() + 1, piezaActual.getForma())) {
            piezaActual.moverAbajo();
        } else {
            // Si colisiona abajo, la fijamos en la matriz lógica
            logica.fijarPieza(piezaActual);
            generarNuevaPieza();
        }
    }
}
