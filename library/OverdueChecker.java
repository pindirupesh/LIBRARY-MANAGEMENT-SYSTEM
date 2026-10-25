package library;

import javax.swing.SwingUtilities;

// Unit II: Multithreading - this Runnable runs in a background thread.
// Every 30 seconds it checks for overdue books and refreshes the book table.
public class OverdueChecker implements Runnable {

    private LibraryGUI gui;
    private BookDAO dao = new BookDAO();

    public OverdueChecker(LibraryGUI gui) {
        this.gui = gui;
    }

    // run() is the code the thread executes
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Checking overdue books...");
                int count = dao.countOverdueBooks();
                String msg = "Checked overdue books: " + count + " book(s) issued more than 14 days ago";
                // update the GUI safely on the Swing thread
                SwingUtilities.invokeLater(() -> {
                    gui.setStatus(msg);
                    gui.refreshTable();
                });
            } catch (Exception e) {
                System.out.println("Overdue check failed: " + e.getMessage());
            }

            try {
                Thread.sleep(30000);   // wait 30 seconds before checking again
            } catch (InterruptedException e) {
                System.out.println("Overdue checker stopped");
                break;
            }
        }
    }
}
