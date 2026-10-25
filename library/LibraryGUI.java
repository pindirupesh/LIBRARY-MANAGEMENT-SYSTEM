package library;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Unit IV: Swing GUI and event handling (delegation event model).
// This class is the event listener: it implements ActionListener.
public class LibraryGUI extends JFrame implements ActionListener {

    private Library library = new Library();

    // Text fields for the form
    private JTextField txtId = new JTextField(10);
    private JTextField txtTitle = new JTextField(10);
    private JTextField txtAuthor = new JTextField(10);
    private JTextField txtMember = new JTextField(10);

    // Buttons
    private JButton btnAdd = new JButton("Add Book");
    private JButton btnIssue = new JButton("Issue Book");
    private JButton btnReturn = new JButton("Return Book");
    private JButton btnSearch = new JButton("Search");
    private JButton btnDelete = new JButton("Delete Book");
    private JButton btnLog = new JButton("View Log");
    private JButton btnRefresh = new JButton("Refresh");

    // Table to show books
    private String[] columns = {"Book ID", "Title", "Author", "Status"};
    private DefaultTableModel model = new DefaultTableModel(columns, 0);
    private JTable table = new JTable(model);
    private JLabel lblStatus = new JLabel(" Status: Ready");

    // Constructor: builds the window
    public LibraryGUI() {
        setTitle("Library Management System");
        setSize(950, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));   // BorderLayout for the main frame

        // NORTH: heading
        JLabel heading = new JLabel("Library Management System", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        add(heading, BorderLayout.NORTH);

        // WEST: form panel with GridLayout (4 rows, 2 columns)
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 10));
        form.setBorder(BorderFactory.createTitledBorder("Book Details"));
        form.add(new JLabel("Book ID:"));
        form.add(txtId);
        form.add(new JLabel("Title:"));
        form.add(txtTitle);
        form.add(new JLabel("Author:"));
        form.add(txtAuthor);
        form.add(new JLabel("Member ID:"));
        form.add(txtMember);
        JPanel left = new JPanel(new BorderLayout());
        left.add(form, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);

        // CENTER: table inside a scroll pane
        table.setRowHeight(22);
        table.getColumnModel().getColumn(1).setPreferredWidth(230);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // SOUTH: buttons in FlowLayout and the status label
        JPanel buttons = new JPanel(new FlowLayout());
        JButton[] all = {btnAdd, btnIssue, btnReturn, btnSearch, btnDelete, btnLog, btnRefresh};
        for (JButton b : all) {
            b.addActionListener(this);   // register this class as the listener
            buttons.add(b);
        }
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(buttons, BorderLayout.CENTER);
        bottom.add(lblStatus, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
    }

    // Reloads books from the database and shows them in the table
    public void refreshTable() {
        try {
            library.loadData();
            showBooks(library.getBooks());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    // Fills the table with the given list of books
    private void showBooks(List<Book> list) {
        model.setRowCount(0);
        for (Book b : list) {
            String status = b.isAvailable() ? "Available" : "Issued";
            model.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getAuthor(), status});
        }
    }

    // Used by the OverdueChecker thread to show a message
    public void setStatus(String text) {
        lblStatus.setText(" Status: " + text);
    }

    // Unit IV: called automatically when any button is clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnAdd) {
                int id = Integer.parseInt(txtId.getText().trim());
                String title = txtTitle.getText().trim();
                String author = txtAuthor.getText().trim();
                if (title.isEmpty() || author.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter title and author");
                    return;
                }
                library.addBook(new Book(id, title, author, true));
                JOptionPane.showMessageDialog(this, "Book added successfully");
            } else if (e.getSource() == btnIssue) {
                int id = Integer.parseInt(txtId.getText().trim());
                int memberId = Integer.parseInt(txtMember.getText().trim());
                JOptionPane.showMessageDialog(this, library.issueBook(id, memberId));
            } else if (e.getSource() == btnReturn) {
                int id = Integer.parseInt(txtId.getText().trim());
                JOptionPane.showMessageDialog(this, library.returnBook(id));
            } else if (e.getSource() == btnSearch) {
                search();
                return;   // keep the search result in the table
            } else if (e.getSource() == btnDelete) {
                int id = Integer.parseInt(txtId.getText().trim());
                library.deleteBook(id);
                JOptionPane.showMessageDialog(this, "Book deleted");
            } else if (e.getSource() == btnLog) {
                JTextArea area = new JTextArea(library.readLog(), 12, 60);
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Issue Log", JOptionPane.PLAIN_MESSAGE);
            }
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for ID");
        } catch (BookNotAvailableException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Not Available", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search by ID if the ID box has a value, otherwise search by title
    private void search() {
        if (!txtId.getText().trim().isEmpty()) {
            Book b = library.searchBook(Integer.parseInt(txtId.getText().trim()));   // overloaded (int)
            if (b == null) {
                JOptionPane.showMessageDialog(this, "No book found with this ID");
            } else {
                showBooks(Arrays.asList(b));
            }
        } else {
            List<Book> result = library.searchBook(txtTitle.getText().trim());     // overloaded (String)
            showBooks(result);
            setStatus(result.size() + " book(s) found");
        }
    }
}
