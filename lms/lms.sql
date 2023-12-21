CREATE TABLE Books (
    isbn char(17) NOT NULL PRIMARY KEY,
    title char(200) NOT NULL,
    genre char(200) NOT NULL,
    length int NOT NULL,
    publish_date date NOT NULL,
    added_date date NOT NULL
); 

DELIMITER $$
CREATE TRIGGER insert_books_added_date
  BEFORE INSERT ON Books
  FOR EACH ROW
  BEGIN
  IF NEW.added_date IS NULL THEN
  SET NEW.added_date = CURDATE();
  END IF;
 END;
 $$
 DELIMITER ;  
 
CREATE TABLE Copies (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    isbn char(17) NOT NULL,
    FOREIGN KEY (isbn) REFERENCES Books(isbn) ON DELETE CASCADE
);

CREATE TABLE Book_Authors (
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    isbn char(17) NOT NULL,
    name char(100) NOT NULL,
    FOREIGN KEY (isbn) REFERENCES Books(isbn) ON DELETE CASCADE
);

CREATE TABLE Users (
    card_number int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name char(100) NOT NULL,
    phone_number char(20) NOT NULL,
    created_at timestamp NOT NULL default NOW()
);


CREATE TABLE Checkouts (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    copy_id int NOT NULL,
    checkout_date date NOT NULL,
    card_number int NOT NULL,
    due_date date NOT NULL,
    return_date date,
    FOREIGN KEY (copy_id) REFERENCES Copies(id) ON DELETE CASCADE,
    FOREIGN KEY (card_number) REFERENCES Users(card_number) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER insert_checkouts_due_date
  BEFORE INSERT ON Checkouts
  FOR EACH ROW
  BEGIN
  IF NEW.due_date IS NULL THEN
  SET NEW.due_date = CURDATE() + INTERVAL 1 WEEK;
  ELSEIF NEW.due_date < CURDATE() THEN
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = "Cannot set a due date to be earlier than the current date";
  END IF;
 END;
 $$
 DELIMITER ;  
  
CREATE TABLE Fines (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    checkout_id int NOT NULL,
    amount decimal(5,2) NOT NULL,
    paid_date date,
    assigned_date date NOT NULL,
    FOREIGN KEY (checkout_id) REFERENCES Checkouts(id) ON DELETE CASCADE
);

INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (6, 1.50, '2023-12-7');
INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (5, 1.50, '2023-12-7');
INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (7, 1.50, '2023-12-7');


INSERT INTO Books (isbn, title, genre, length, publish_date)
VALUES
    ('9780061120084', 'To Kill a Mockingbird', 'Fiction', 336, '1960-07-11'),
    ('9780735219090', 'Where the Crawdads Sing', 'Mystery', 384, '2018-08-14'),
    ('9780545091022', 'The Hunger Games', 'Science Fiction', 374, '2008-09-14'),
    ('9780307744432', 'The Night Circus', 'Romance', 387, '2011-09-13'),
    ('9780307588371', 'Gone Girl', 'Thriller', 419, '2012-05-24'),
    ('9780547928227', 'The Hobbit', 'Fantasy', 310, '1937-09-21'),
    ('9780425232200', 'The Help', 'Historical Fiction', 464, '2009-02-10'),
    ('9780385486804', 'Into the Wild', 'Biography', 224, '1996-01-13'),
    ('9780345806789', 'The Shining', 'Horror', 447, '1977-01-28'),
    ('9780062316110', 'Sapiens: A Brief History of Humankind', 'Non-Fiction', 464, '2011-04-11'),
    ('9780399590528', 'Educated', 'Memoir', 352, '2018-02-20'),
    ('9780061122415', 'The Alchemist', 'Fiction', 197, '1988-04-25'),
    ('9780345538987', 'Jurassic Park', 'Science Fiction', 448, '1990-11-20'),
    ('9783462015393', 'The Catcher in the Rye', 'Fiction', 277, '1951-07-16'),
    ('9780274808328', 'The Da Vinci Code', 'Mystery', 489, '2003-03-18'),
    ('9781594631931', 'The Kite Runner', 'Drama', 371, '2003-05-29'),
    ('9780156027328', 'Life of Pi', 'Adventure', 348, '2001-09-11'),
    ('9780063032378', 'Freakonomics', 'Non-Fiction', 315, '2005-04-12'),
    ('9780066620992', 'Good to Great', 'Business', 320, '2001-10-16'),
    ('9781594634024', 'The Girl on the Train', 'Mystery', 316, '2015-01-13'),
    ('9780316346627', 'The Tipping Point', 'Non-Fiction', 304, '2000-03-01');

INSERT INTO Books (isbn, title, genre, length, publish_date)
VALUES
    ('9780061120084', 'To Kill a Mockingbird 3', 'Fiction', 336, '1960-07-11'),
    ('9780061120085', 'To Kill a Mockingbird 1', 'Fiction', 336, '1960-07-11'),
    ('9780061120086', 'To Kill a Mockingbird 2', 'Fiction', 336, '1960-07-11');
    
INSERT INTO Users (name, phone_number)
VALUES
    ('John Doe', '1234567890'),
    ('Jane Smith', '2345678901'),
    ('Alice Johnson', '3456789012'),
    ('Bob Williams', '4567890123'),
    ('Eva Davis', '5678901234');

INSERT INTO Book_Authors (isbn, name) VALUES
                                          ('9780061120084', "Harper Lee"),
                                          ('9780735219090', "Delia Owens"),
                                          ('9780545091022', "Suzanne Collins"),
                                          ('9780307744432', "Erin Morgenstern"),
                                          ('9780307588371', "Gillian Flynn"),
                                          ('9780547928227', "J.R.R. Tolkien"),
                                          ('9780425232200', "Kathryn Stockett"),
                                          ('9780385486804', "Jon Krakauer"),
                                          ('9780345806789', "Stephen King"),
                                          ('9780062316110', "Yuval Noah Harari"),
                                          ('9780399590528', "Tara Westover"),
                                          ('9780061122415', "Paulo Coelho"),
                                          ('9780345538987', "Michael Crichton"),
                                          ('9783462015393', "J.D. Salinger"),
                                          ('9780274808328', "Dan Brown"),
                                          ('9781594631931', "Khaled Hosseini"),
                                          ('9780156027328', "Yann Martel"),
                                          ('9780063032378', "Steven D. Levitt"),
                                          ('9780063032378', "Stephen J. Dubner"),
                                          ('9780066620992', "Jim Collins"),
                                          ('9781594634024', "Paula Hawkins"),
                                          ('9780316346627', "Malcolm Gladwell");
                                          

INSERT INTO Copies (isbn) VALUES ('9780061120084'), ('9780061120084'), ('9780061120084'), ('9780061120084');

SELECT * From Copies;

INSERT INTO Checkouts (copy_id, checkout_date, due_date, card_number) VALUES (6, '2023-11-29', '2023-12-6', 2);

SELECT * FROM Checkouts;

INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (6, 1.50, '2023-12-7');
INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (5, 1.50, '2023-12-7');
INSERT INTO Fines (checkout_id, amount, assigned_date) VALUES (7, 1.50, '2023-12-7');

SELECT * From Fines;
                
INSERT INTO Checkouts (copy_id, checkout_date, card_number, due_date, return_date)
VALUES
    (3, '2023-03-01', 1, '2023-03-08', NULL),
    (1, '2023-04-01', 1, '2023-04-08', NULL),
    (2, '2023-05-01', 1, '2023-05-08', NULL),
    (3, '2023-06-01', 1, '2023-06-08', NULL),
    (4, '2023-07-01', 1, '2023-07-08', NULL); 
    
INSERT INTO Fines (checkout_id, amount, paid_date, assigned_date)
VALUES
    (11, 15.00, NULL, '2023-03-09'),
    (12, 20.00, NULL, '2023-04-09'),
    (13, 25.00, NULL, '2023-05-09'), 
    (14, 30.00, NULL, '2023-06-09'),
    (15, 35.00, NULL, '2023-07-09');  
                
                
-- Drop Fines table
DROP TABLE IF EXISTS Fines;

-- Drop Checkouts table
DROP TABLE IF EXISTS Checkouts;

-- Drop Users table
DROP TABLE IF EXISTS Users;

-- Drop Book_Authors table
DROP TABLE IF EXISTS Book_Authors;

-- Drop Copies table
DROP TABLE IF EXISTS Copies;

-- Drop Books table
DROP TABLE IF EXISTS Books;

-- A view to show the available books
CREATE VIEW available_books AS SELECT DISTINCT Books.isbn, Books.title, Books.genre, Books.length, Books.publish_date,
    Books.added_date from Books
    JOIN Copies on Books.isbn=Copies.isbn
    LEFT JOIN Checkouts on Copies.id=Checkouts.copy_id WHERE NOT EXISTS (
            SELECT 1
            FROM Checkouts AS C
            WHERE C.copy_id = Copies.id
            AND C.return_date IS NULL
)   ;

DELIMITER $$

CREATE TRIGGER prevent_duplicate_checkout
    BEFORE INSERT ON Checkouts
    FOR EACH ROW
BEGIN
    IF (SELECT COUNT(*) FROM Checkouts
        WHERE copy_id = NEW.copy_id AND return_date IS NULL) > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot insert new checkout. An active checkout already exists for the specified copy.';
    END IF;
END $$

DELIMITER ;

DELIMITER :) 
CREATE TRIGGER prevent_duplicate_users
	BEFORE INSERT ON Users
    FOR EACH ROW
BEGIN
	IF (SELECT COUNT(*) FROM Users
		WHERE Users.name = NEW.name AND Users.phone_number = NEW.phone_number) > 0 THEN
        SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Cannot insert new user. User already exists.';
	END IF;
END :) 

DELIMITER ;


