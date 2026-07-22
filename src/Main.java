import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris!");
        Tablero tablero = new Tablero();

        frame.add(tablero);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Si cierra la ventana, finaliza el programa
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        frame.setVisible(true);
        tablero.requestFocusInWindow();
    }
}