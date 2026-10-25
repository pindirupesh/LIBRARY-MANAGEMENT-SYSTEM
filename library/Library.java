package library;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// Unit I + II + III: Library class implements the LibraryOperations interface.
// Keeps books and members in an ArrayList and a HashMap, and writes the issue log file.
public class Library implements LibraryOperations {

    // Unit III: Collections
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();
    private HashMap<Integer, Book> bookMap = new HashMap<>();   // book ID -> Book

    private BookDAO dao = new BookDAO();
    private static final String LOG_FILE = "issue_log.txt";

    // Loads all books and members from the database into the lists and the map
    public void loadData() throws SQLException {
        books = dao.getAllBooks();
        members = dao.getAllMembers();
        bookMap.clear();
        for (Book b : books) {
            bookMap.put(b.getBookId(), b);
        }
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    // Unit I: adds a new book (database + ArrayList + HashMap)
    @Override
    public void addBook(Book book) throws Exception {
        if (bookMap.containsKey(book.getBookId())) {
            throw new Exception("Book ID " + book.getBookId() + " already exists");
        }
        dao.addBook(book);
        books.add(book);
        bookMap.put(book.getBookId(), book);
    }

    // Unit I: Method overloading - search by ID (uses HashMap for quick lookup)
    public Book searchBook(int bookId) {
        return bookMap.get(bookId);
    }

    // Unit I: Method overloading - search by title (Unit III: uses a Stream)
    public List<Book> searchBook(String title) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Finds a member by ID from the ArrayList
    private Member findMember(int memberId) {
        for (Member m : members) {
            if (m.getMemberId() == memberId) {
                return m;
            }
        }
        return null;
    }

    // Unit II: throw / throws and try-catch-finally. Returns a receipt message.
    @Override
    public String issueBook(int bookId, int memberId) throws BookNotAvailableException {
        try {
            Book book = bookMap.get(bookId);
            if (book == null) {
                throw new BookNotAvailableException("Book ID " + bookId + " not found");
            }
            if (!book.isAvailable()) {
                throw new BookNotAvailableException("'" + book.getTitle() + "' is already issued");
            }
            Member member = findMember(memberId);
            if (member == null) {
                throw new BookNotAvailableException("Member ID " + memberId + " not found");
            }
            dao.updateAvailability(bookId, false);   // Unit V: UPDATE
            dao.addIssueRecord(bookId, memberId);
            book.setAvailable(false);
            writeLog("ISSUED", book, member.getName());

            // Unit II: StringBuilder to build the receipt
            StringBuilder receipt = new StringBuilder();
            receipt.append("Book Issued Successfully\n");
            receipt.append("Book  : ").append(book.getTitle()).append("\n");
            receipt.append("Member: ").append(member.getDetails()).append("\n");
            receipt.append("Date  : ").append(java.time.LocalDate.now());
            return receipt.toString();
        } catch (SQLException e) {
            throw new BookNotAvailableException("Database error: " + e.getMessage());
        } finally {
            System.out.println("Issue request processed for Book ID " + bookId);
        }
    }

    // Unit II: return a book; throws the custom exception if it was not issued
    @Override
    public String returnBook(int bookId) throws BookNotAvailableException {
        try {
            Book book = bookMap.get(bookId);
            if (book == null) {
                throw new BookNotAvailableException("Book ID " + bookId + " not found");
            }
            if (book.isAvailable()) {
                throw new BookNotAvailableException("'" + book.getTitle() + "' was not issued");
            }
            dao.updateAvailability(bookId, true);   // Unit V: UPDATE
            dao.removeIssueRecord(bookId);
            book.setAvailable(true);
            writeLog("RETURNED", book, "-");
            return "Book Returned Successfully\nBook: " + book.getTitle();
        } catch (SQLException e) {
            throw new BookNotAvailableException("Database error: " + e.getMessage());
        } finally {
            System.out.println("Return request processed for Book ID " + bookId);
        }
    }

    // Unit V: DELETE a book (only if it is not issued)
    public void deleteBook(int bookId) throws Exception {
        Book book = bookMap.get(bookId);
        if (book == null) {
            throw new Exception("Book ID " + bookId + " not found");
        }
        if (!book.isAvailable()) {
            throw new Exception("Cannot delete, the book is issued");
        }
        dao.deleteBook(bookId);
        books.remove(book);
        bookMap.remove(bookId);
    }

    // Unit III: FileWriter - adds one line to issue_log.txt (true = append mode)
    private void writeLog(String action, Book book, String memberName) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(time + " | " + action + " | Book " + book.getBookId() + " - "
                    + book.getTitle() + " | Member: " + memberName + "\n");
        } catch (IOException e) {
            System.out.println("Could not write log: " + e.getMessage());
        }
    }

    // Unit III: FileReader - reads the whole issue_log.txt file
    public String readLog() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "No issue log found yet.";
        }
        return sb.toString();
    }
}
