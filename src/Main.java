import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris!");
            Board board = new Board();
            NextPiecePanel nextPiecePanel = new NextPiecePanel();
            board.setNextPiecePanel(nextPiecePanel);

            frame.setLayout(new BorderLayout());
            frame.add(board, BorderLayout.CENTER);
            frame.add(nextPiecePanel, BorderLayout.EAST);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Salir del programa al cerrar la ventana
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true);

            board.requestFocusInWindow();
        });
    }
}