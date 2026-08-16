import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris!");
            Board board = new Board();
            SidePanel sidePanel = new SidePanel();
            board.setSidePanel(sidePanel);
            sidePanel.actualizarInfo(0, 0, 1, HighScore.getMejorPuntaje());

            frame.setLayout(new BorderLayout());
            frame.add(board, BorderLayout.CENTER);
            frame.add(sidePanel, BorderLayout.EAST);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Salir del programa al cerrar la ventana
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true);

            board.requestFocusInWindow();
        });
    }
}