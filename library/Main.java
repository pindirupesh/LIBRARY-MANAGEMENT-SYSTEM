package library;

import javax.swing.SwingUtilities;

// Main class: starting point of the program.
public class Main {

    public static void main(String[] args) {
        // Open the window on the Swing event thread
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setLocationRelativeTo(null);
            gui.setVisible(true);

            // Unit II: start the background thread
            Thread checker = new Thread(new OverdueChecker(gui));
            checker.setDaemon(true);   // thread stops when the window is closed
            checker.start();
        });
    }
}
