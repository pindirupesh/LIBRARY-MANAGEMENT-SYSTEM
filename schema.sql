-- Unit V: Database for the Library Management System
-- Run this file in MySQL:  mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

-- Table to store all books
CREATE TABLE IF NOT EXISTS books (
    book_id   INT PRIMARY KEY,
    title     VARCHAR(100) NOT NULL,
    author    VARCHAR(100) NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- Table to store library members
CREATE TABLE IF NOT EXISTS members (
    member_id INT PRIMARY KEY,
    name      VARCHAR(100) NOT NULL
);

-- Table to store which member has which book
CREATE TABLE IF NOT EXISTS issued_books (
    issue_id   INT PRIMARY KEY AUTO_INCREMENT,
    book_id    INT NOT NULL,
    member_id  INT NOT NULL,
    issue_date DATE NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);

-- Sample books
INSERT INTO books (book_id, title, author, available) VALUES
(101, 'Java: The Complete Reference', 'Herbert Schildt', TRUE),
(102, 'Head First Java', 'Kathy Sierra', TRUE),
(103, 'Let Us C', 'Yashavant Kanetkar', TRUE),
(104, 'Data Structures Using C', 'Reema Thareja', TRUE),
(105, 'Database System Concepts', 'Abraham Silberschatz', TRUE);

-- Sample members
INSERT INTO members (member_id, name) VALUES
(1, 'Rupesh'),
(2, 'Arnav'),
(3, 'Kavya');
