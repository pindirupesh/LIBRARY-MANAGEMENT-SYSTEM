# Syllabus Mapping (for Viva)

This file shows exactly where each syllabus topic is used in the project.
Line numbers are approximate; the method names are the easiest way to find them.

---

## Unit I - Object Oriented Programming

| Topic | File | Where |
|-------|------|-------|
| Package | all files | first line: `package library;` |
| Class + encapsulation | `Book.java` | private fields `bookId`, `title`, `author`, `available` (lines 6-9), constructor, getters and setters |
| Class + encapsulation | `Person.java`, `Member.java` | private `name` and `memberId` with getters/setters |
| Library class | `Library.java` | `public class Library implements LibraryOperations` (line 12) |
| Inheritance | `Member.java` | `public class Member extends Person` (line 5) |
| `super` keyword | `Member.java` | `super(name);` in the constructor (line 11) and `super.getDetails()` in `getDetails()` |
| Method overriding | `Member.java` | `getDetails()` overrides the method of `Person` |
| Method overloading | `Library.java` | `searchBook(int bookId)` (line 48) and `searchBook(String title)` (line 53) - same name, different parameters |
| Interface | `LibraryOperations.java` | declares `addBook`, `issueBook`, `returnBook` |
| Implementing an interface | `Library.java` | `@Override` methods `addBook`, `issueBook`, `returnBook` |

**Viva tip:** Overloading is decided at compile time (by parameter list).
Overriding is decided at run time (by the object type).

---

## Unit II - Exception Handling and Multithreading

| Topic | File | Where |
|-------|------|-------|
| Custom exception | `BookNotAvailableException.java` | `extends Exception`, passes message with `super(message)` |
| `throws` | `LibraryOperations.java`, `Library.java` | `issueBook(...) throws BookNotAvailableException`, `returnBook(...) throws BookNotAvailableException` |
| `throw` | `Library.java` | `throw new BookNotAvailableException("... is already issued")` inside `issueBook` (lines 75-82) and `returnBook` (lines 109-112) |
| try-catch-finally | `Library.java` | `issueBook` (lines 72-100) and `returnBook` (lines 106-123). `finally` always prints "Issue/Return request processed ..." |
| Catching exceptions in GUI | `LibraryGUI.java` | `actionPerformed`: catches `NumberFormatException`, `BookNotAvailableException`, `Exception` |
| Thread (Runnable) | `OverdueChecker.java` | `implements Runnable`, `run()` method with a `while` loop |
| `Thread.sleep()` | `OverdueChecker.java` | waits 30 seconds between checks |
| Starting the thread | `Main.java` | `new Thread(new OverdueChecker(gui))`, `setDaemon(true)`, `start()` |
| Updating GUI from thread | `OverdueChecker.java` | `SwingUtilities.invokeLater(...)` refreshes the table and status label |
| StringBuilder | `Library.java` | receipt in `issueBook` (line 90) and reading the log in `readLog()` |

**Viva tip:** `finally` runs whether an exception happens or not.
Daemon thread = a background thread that stops automatically when the program ends.

---

## Unit III - Collections, Streams and File I/O

| Topic | File | Where |
|-------|------|-------|
| ArrayList | `Library.java` | `ArrayList<Book> books`, `ArrayList<Member> members` (lines 15-16) |
| ArrayList | `BookDAO.java` | `getAllBooks()` and `getAllMembers()` return ArrayLists |
| HashMap | `Library.java` | `HashMap<Integer, Book> bookMap` (line 17), filled in `loadData()`, used in `searchBook(int)`, `issueBook`, `returnBook`, `deleteBook` |
| Stream | `Library.java` | `searchBook(String)` uses `books.stream().filter(...).collect(...)` |
| FileWriter | `Library.java` | `writeLog()` appends a line to `issue_log.txt` (`new FileWriter(LOG_FILE, true)`) |
| FileReader | `Library.java` | `readLog()` reads `issue_log.txt` using `BufferedReader(new FileReader(...))` |
| try-with-resources | `Library.java`, `BookDAO.java` | files and DB connections close automatically |

**Viva tip:** HashMap gives fast lookup by key (book ID) without looping through the whole list.
`true` in `new FileWriter(file, true)` means append mode (old lines are not erased).

---

## Unit IV - GUI with Swing and Event Handling

| Topic | File | Where |
|-------|------|-------|
| JFrame | `LibraryGUI.java` | `public class LibraryGUI extends JFrame` |
| JPanel, JLabel, JTextField, JButton | `LibraryGUI.java` | fields at the top and the constructor |
| JTable + JScrollPane | `LibraryGUI.java` | `new JTable(model)` with `DefaultTableModel`, added inside `new JScrollPane(table)` |
| BorderLayout | `LibraryGUI.java` | `setLayout(new BorderLayout(10, 10))`: heading NORTH, form WEST, table CENTER, buttons SOUTH |
| GridLayout | `LibraryGUI.java` | form panel `new GridLayout(4, 2, 5, 10)` |
| FlowLayout | `LibraryGUI.java` | button panel `new FlowLayout()` |
| ActionListener (delegation event model) | `LibraryGUI.java` | class `implements ActionListener`; each button calls `addActionListener(this)`; `actionPerformed(ActionEvent e)` uses `e.getSource()` to find which button was clicked |
| JOptionPane | `LibraryGUI.java` | `showMessageDialog` for success, error and receipt messages |
| Buttons | `LibraryGUI.java` | Add Book, Issue Book, Return Book, Search, Delete Book, View Log, Refresh |

**Viva tip (delegation event model):** the button is the *event source*,
`ActionEvent` is the *event object*, and `LibraryGUI` is the *listener*.
The source "delegates" the handling to the registered listener.

---

## Unit V - JDBC

| Topic | File | Where |
|-------|------|-------|
| Database + tables | `schema.sql` | `librarydb` with `books`, `members`, `issued_books` |
| DriverManager + Connection | `DBConnection.java` | `DriverManager.getConnection(URL, USER, PASSWORD)` |
| PreparedStatement | `BookDAO.java` | every method uses `con.prepareStatement(sql)` with `?` placeholders |
| ResultSet | `BookDAO.java` | `getAllBooks()`, `getAllMembers()`, `countOverdueBooks()` read rows with `rs.next()` |
| **C**reate | `BookDAO.addBook()` | `INSERT INTO books ...` ; `addIssueRecord()` inserts into `issued_books` |
| **R**etrieve | `BookDAO.getAllBooks()` | `SELECT * FROM books` (search filters this list) |
| **U**pdate | `BookDAO.updateAvailability()` | `UPDATE books SET available = ?` (issue = false, return = true) |
| **D**elete | `BookDAO.deleteBook()` | `DELETE FROM books WHERE book_id = ?` ; `removeIssueRecord()` on return |
| DAO | `BookDAO.java` | keeps all SQL in one class, separate from the GUI |

**Viva tip:** PreparedStatement is safer than Statement because the values are
sent separately from the SQL, which prevents SQL injection, and it is faster
when the same query runs many times.

---

## Flow of one action (Issue Book)

1. User types Book ID and Member ID and clicks **Issue Book**.
2. `actionPerformed()` in `LibraryGUI` is called (event handling).
3. It calls `library.issueBook(id, memberId)`.
4. `issueBook()` looks up the book in the HashMap and the member in the ArrayList.
   If something is wrong it throws `BookNotAvailableException`.
5. `BookDAO.updateAvailability()` and `BookDAO.addIssueRecord()` update MySQL.
6. `writeLog()` writes a line into `issue_log.txt` using FileWriter.
7. A receipt is built with StringBuilder and shown with JOptionPane.
8. `finally` prints a message, and the table is refreshed.
