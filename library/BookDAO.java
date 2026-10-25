package library;

import java.sql.*;
import java.util.ArrayList;

// Unit V: DAO (Data Access Object) - all SQL queries for books, members and issued_books.
// Uses Connection, PreparedStatement and ResultSet. Implements CRUD.
public class BookDAO {

    // CREATE: insert a new book into the books table
    public void addBook(Book b) throws SQLException {
        String sql = "INSERT INTO books (book_id, title, author, available) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, b.getBookId());
            ps.setString(2, b.getTitle());
            ps.setString(3, b.getAuthor());
            ps.setBoolean(4, b.isAvailable());
            ps.executeUpdate();
        }
    }

    // RETRIEVE: get all books from the books table
    public ArrayList<Book> getAllBooks() throws SQLException {
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY book_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Book(rs.getInt("book_id"), rs.getString("title"),
                        rs.getString("author"), rs.getBoolean("available")));
            }
        }
        return list;
    }

    // RETRIEVE: get all members from the members table
    public ArrayList<Member> getAllMembers() throws SQLException {
        ArrayList<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY member_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Member(rs.getInt("member_id"), rs.getString("name")));
            }
        }
        return list;
    }

    // UPDATE: mark a book as available (true) or issued (false)
    public void updateAvailability(int bookId, boolean available) throws SQLException {
        String sql = "UPDATE books SET available = ? WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        }
    }

    // DELETE: remove a book from the books table
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
    }

    // CREATE: save an issue record in the issued_books table
    public void addIssueRecord(int bookId, int memberId) throws SQLException {
        String sql = "INSERT INTO issued_books (book_id, member_id, issue_date) VALUES (?, ?, CURDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.executeUpdate();
        }
    }

    // DELETE: remove the issue record when the book is returned
    public void removeIssueRecord(int bookId) throws SQLException {
        String sql = "DELETE FROM issued_books WHERE book_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
    }

    // RETRIEVE: count books issued more than 14 days ago (used by the thread)
    public int countOverdueBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM issued_books WHERE issue_date < CURDATE() - INTERVAL 14 DAY";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
