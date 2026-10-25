package library;

// Unit II: Custom exception.
// Thrown when a book cannot be issued or returned (not found, already issued, etc.)
public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(String message) {
        super(message);   // pass the message to the Exception class
    }
}
