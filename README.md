# Library Management System (Java Swing + JDBC + MySQL)

A simple desktop Library Management System made for the B.Tech II year
Object Oriented Programming through Java course.

Features: add a book, issue a book to a member, return a book, search books
(by ID or by title), delete a book, view the issue log, and refresh the table.
A background thread checks for overdue books every 30 seconds.

## Requirements

- JDK 8 or later (tested with JDK 17)
- MySQL Server 8.x (MariaDB also works)
- MySQL JDBC driver: `mysql-connector-j-8.4.0.jar` (already in the `lib` folder)
  Download link if needed: https://dev.mysql.com/downloads/connector/j/

## Folder structure

```
LibraryManagementSystem/
  library/          all Java source files (package "library")
  lib/              mysql-connector-j-8.4.0.jar
  schema.sql        database and sample data
  README.md
  EXPLANATION.md    where each syllabus topic is used
```

## Step 1: Set up the database

Open a terminal in this folder and run:

```
mysql -u root -p < schema.sql
```

This creates the database `librarydb` with three tables (`books`, `members`,
`issued_books`) and adds 5 sample books and 3 sample members.

## Step 2: Set your MySQL password

Open `library/DBConnection.java` and change `USER` and `PASSWORD`
to your own MySQL username and password:

```java
private static final String USER = "root";
private static final String PASSWORD = "root";
```

## Step 3: Compile

Run this from the `LibraryManagementSystem` folder (not inside `library`):

```
javac -d out library/*.java
```

## Step 4: Run

Windows:
```
java -cp "out;lib/mysql-connector-j-8.4.0.jar" library.Main
```

Linux / macOS:
```
java -cp "out:lib/mysql-connector-j-8.4.0.jar" library.Main
```

## How to use

| Button      | Fields to fill             | What it does                               |
|-------------|----------------------------|--------------------------------------------|
| Add Book    | Book ID, Title, Author     | Adds a new book                            |
| Issue Book  | Book ID, Member ID         | Issues the book and shows a receipt        |
| Return Book | Book ID                    | Marks the book as available again          |
| Search      | Book ID **or** Title       | Shows only the matching books              |
| Delete Book | Book ID                    | Deletes a book (only if it is not issued)  |
| View Log    | -                          | Shows the contents of `issue_log.txt`      |
| Refresh     | -                          | Reloads all books from the database        |

Sample member IDs: 1, 2, 3. Sample book IDs: 101 to 105.

`issue_log.txt` is created in the folder from where you run the program.

## Common errors

- `No suitable driver found` : the JDBC jar is not in the `-cp` path.
- `Access denied for user 'root'` : wrong password in `DBConnection.java`.
- `Unknown database 'librarydb'` : run `schema.sql` first.
- `Communications link failure` : MySQL server is not running.
