package library;

// Unit I: Interface - it only says WHAT operations a library must have.
// The Library class gives the actual code (implements this interface).
public interface LibraryOperations {

    void addBook(Book book) throws Exception;

    String issueBook(int bookId, int memberId) throws BookNotAvailableException;

    String returnBook(int bookId) throws BookNotAvailableException;
}
