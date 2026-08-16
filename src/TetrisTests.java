import java.awt.*;

/**
 * Suite de tests ligera (sin framework) para GameLogic y Piece.
 * Ejecutar con: java -cp out/production/TetrisPortfolio TetrisTests
 */
public class TetrisTests {
    private static int pasados = 0;
    private static int fallados = 0;

    public static void main(String[] args) {
        testColisionBordes();
        testColisionPiso();
        testColisionPiezas();
        testLimpiarUnaLinea();
        testLimpiarMultiplesLineas();
        testRotacion();
        testDropY();
        testSpawnCentrado();
        System.out.println("Pasaron " + pasados + " tests, fallaron " + fallados + ".");
        if (fallados > 0) {
            System.exit(1);
        }
    }

    private static void testColisionBordes() {
        GameLogic gl = new GameLogic(20, 10);
        Piece p = new Piece(piezaJ(), Color.BLUE);
        assertTrue(gl.collisionDetected(-1, 0, p.getShape()), "colisión contra borde izquierdo");
        assertTrue(gl.collisionDetected(10, 0, p.getShape()), "colisión contra borde derecho");
        assertTrue(!gl.collisionDetected(4, 0, p.getShape()), "sin colisión en posición inicial");
    }

    private static void testColisionPiso() {
        GameLogic gl = new GameLogic(20, 10);
        Piece p = new Piece(piezaJ(), Color.BLUE);
        assertTrue(gl.collisionDetected(4, 19, p.getShape()), "colisión contra el piso");
        assertTrue(!gl.collisionDetected(4, 17, p.getShape()), "sin colisión cerca del piso");
    }

    private static void testColisionPiezas() {
        GameLogic gl = new GameLogic(20, 10);
        Piece p1 = new Piece(piezaJ(), Color.BLUE);
        p1.setX(4);
        p1.setY(19);
        gl.securePart(p1);
        Piece p2 = new Piece(piezaJ(), Color.BLUE);
        assertTrue(gl.collisionDetected(4, 18, p2.getShape()), "colisión contra piezas fijadas");
    }

    private static void testLimpiarUnaLinea() {
        GameLogic gl = new GameLogic(20, 10);
        // Tres piezas I llenan la fila 17 (columnas 0-9)
        secureI(gl, 0, 16);
        secureI(gl, 4, 16);
        secureI(gl, 8, 16);
        assertTrue(gl.getLines() == 1, "una línea limpia");
        assertTrue(gl.getScore() == 100, "puntaje por una línea (100 x nivel 1)");
        Color[][] matriz = gl.getMatriz();
        assertTrue(filaVacia(matriz, 19), "las líneas superiores caen al fondo");
    }

    private static void testLimpiarMultiplesLineas() {
        GameLogic gl = new GameLogic(20, 10);
        // Cinco piezas O llenan las filas 18 y 19 a la vez (2 líneas simultáneas)
        for (int x = 0; x < 10; x += 2) {
            secureO(gl, x, 18);
        }
        assertTrue(gl.getLines() == 2, "dos líneas a la vez");
        assertTrue(gl.getScore() == 300, "bonus por dos líneas (300 x nivel 1)");
    }

    private static void testRotacion() {
        Piece p = new Piece(piezaI(), Color.CYAN);
        int[][] rotada = p.getRotatedShape();
        // La I horizontal se vuelve vertical en la columna 2 de la matriz 4x4
        assertTrue(rotada[0][2] == 1 && rotada[1][2] == 1
                && rotada[2][2] == 1 && rotada[3][2] == 1, "rotación de la pieza I");
    }

    private static void testDropY() {
        GameLogic gl = new GameLogic(20, 10);
        Piece p = new Piece(piezaJ(), Color.BLUE);
        assertTrue(gl.getDropY(p) == 18, "cálculo de tierra para pieza J");
    }

    private static void testSpawnCentrado() {
        Piece i = new Piece(piezaI(), Color.CYAN);
        assertTrue(i.getX() == 3, "pieza I (ancho 4) centrada en columna 3");
        Piece j = new Piece(piezaJ(), Color.BLUE);
        assertTrue(j.getX() == 3, "pieza de ancho 3 centrada en columna 3");
        Piece o = new Piece(new int[][]{{1, 1}, {1, 1}}, Color.YELLOW);
        assertTrue(o.getX() == 4, "pieza O (ancho 2) centrada en columna 4");
    }

    // ---- Helpers ----

    private static int[][] piezaI() {
        return new int[][]{{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}};
    }

    private static int[][] piezaJ() {
        return new int[][]{{1, 0, 0}, {1, 1, 1}, {0, 0, 0}};
    }

    private static void secureI(GameLogic gl, int x, int y) {
        Piece p = new Piece(piezaI(), Color.CYAN);
        p.setX(x);
        p.setY(y);
        gl.securePart(p);
    }

    private static void secureO(GameLogic gl, int x, int y) {
        Piece p = new Piece(new int[][]{{1, 1}, {1, 1}}, Color.YELLOW);
        p.setX(x);
        p.setY(y);
        gl.securePart(p);
    }

    private static boolean filaVacia(Color[][] matriz, int fila) {
        for (int c = 0; c < matriz[fila].length; c++) {
            if (matriz[fila][c] != null) return false;
        }
        return true;
    }

    private static void assertTrue(boolean condicion, String descripcion) {
        if (condicion) {
            pasados++;
            System.out.println("[OK] " + descripcion);
        } else {
            fallados++;
            System.out.println("[FALLO] " + descripcion);
        }
    }
}