import java.io.*;

/**
 * Persiste el mejor puntaje en un archivo dentro del directorio del usuario.
 */
public class HighScore {
    private static final String FILE =
            System.getProperty("user.home") + File.separator + ".tetris_highscore.txt";
    private static int best = -1;

    private HighScore() {
    }

    public static int getMejorPuntaje() {
        if (best < 0) {
            cargar();
        }
        return best;
    }

    public static void registrarPuntaje(int score) {
        if (score > getMejorPuntaje()) {
            best = score;
            guardar();
        }
    }

    private static void cargar() {
        best = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String linea = br.readLine();
            if (linea != null) {
                best = Integer.parseInt(linea.trim());
            }
        } catch (IOException | NumberFormatException e) {
            best = 0;
        }
    }

    private static void guardar() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            bw.write(String.valueOf(best));
        } catch (IOException e) {
            // Si no se puede escribir, se ignora sin interrumpir el juego.
        }
    }
}