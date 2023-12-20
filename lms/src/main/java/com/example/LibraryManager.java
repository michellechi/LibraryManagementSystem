package com.example;


import java.util.*;

public class LibraryManager {
    private DatabaseConnector databaseConnector;

    public LibraryManager(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void addBook(String isbn, String title, String genre, int length, Date publishDate) {
        String insertQuery = "INSERT INTO Books (isbn, title, genre, length, publish_date, added_date) " +
                             "VALUES (?, ?, ?, ?, ?, CURDATE())";
        databaseConnector.runParametrizedQuery(insertQuery, isbn, title, genre, length, publishDate);
    }

    public void addBookCopy(String isbn) {
        String insertQuery = "INSERT INTO Copies (isbn) VALUES (?)";
        databaseConnector.runParametrizedQuery(insertQuery, isbn);
    }

    public void checkoutBook(int copyId, int cardNumber) {
        String insertQuery = "INSERT INTO Checkouts (copy_id, checkout_date, card_number, due_date) " +
                             "VALUES (?, NOW(), ?, NOW() + INTERVAL 1 WEEK)";
        databaseConnector.runParametrizedQuery(insertQuery, copyId, cardNumber);
    }


    public void returnBook(int checkoutId) {
        String updateQuery = "UPDATE Checkouts SET return_date = NOW() WHERE id = ?";
        databaseConnector.runParametrizedQuery(updateQuery, checkoutId);

        // Calculate fines and perform any additional logic (placeholder)
        calculateFines(checkoutId);
    }

    private void calculateFines(int checkoutId) {
        // Placeholder for calculating fines based on checkout details
        System.out.println("Calculating fines for checkout ID: " + checkoutId);
        // Add your fine calculation logic here
        // ...

        int calculatedFineAmount = 0;

        // Example: Update fines table with calculated fines
        String finesUpdateQuery = "UPDATE Fines SET amount = ? WHERE checkout_id = ?";
        databaseConnector.runParametrizedQuery(finesUpdateQuery, calculatedFineAmount, checkoutId);
    }

    public void payFine(int fineId) {
        String updateQuery = "UPDATE Fines SET paid_date = NOW() WHERE id = ?";
        databaseConnector.runParametrizedQuery(updateQuery, fineId);
    }


    /*
     * Params should be structured as follows:
     * title: String (will be compared using LIKE)
     * author: String (will be compared using LIKE)
     * genre: String (will be compared using LIKE)
     * length: Integer
     * lengthMethod: String (can be ">", "<", or "=")
     * publishDate: String as yyyy-mm-dd
     * publishDateMethod: String (can be ">", "<", or "=")
     * availableOnly: Boolean
     */
    public BookSearchRecord[] getBooksFromQuery(Map<String, Object> params) {

        boolean firstParam = true;
        int param = 1;
        String query = "SELECT Books.isbn, Books.title, Books.genre, Books.length, Books.publish_date, Books.added_date," +
                "GROUP_CONCAT(Book_Authors.name SEPARATOR ', ') AS authors FROM " +
                ((params.containsKey("availableOnly") && (boolean) params.get("availableOnly")) ? "available_books as Books" : "Books") +
                " JOIN Book_Authors ON Books.isbn=Book_Authors.isbn";
        String whereClause = "";
        List<Object> paramValues = new ArrayList<>();
        params.remove("availableOnly");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null && !entry.getKey().equals("lengthMethod") && !entry.getKey().equals("publishDateMethod")) {
                if (firstParam) {
                    whereClause += " WHERE ";
                    firstParam = false;
                } else {
                    whereClause += " AND ";
                }
                switch (key) {
                    case "title":
                        whereClause += "Books.title LIKE ?";
                        paramValues.add("%" + value + "%");
                        break;
                    case "author":
                        whereClause += "Book_Authors.name LIKE ?";
                        paramValues.add("%" + value + "%");
                        break;
                    case "genre":
                        whereClause += "Books.genre LIKE ?";
                        paramValues.add("%" + value + "%");
                        break;
                    case "length":
                        whereClause += "Books.length " + params.get("lengthMethod") + " ?";
                        paramValues.add(value);
                        break;
                    case "publishDate":
                        whereClause += "Books.publish_date " + params.get("publishDateMethod") + " ?";
                        paramValues.add(java.sql.Date.valueOf((String) value));
                        break;
                }
            }
        }
        query += whereClause;
        query += " GROUP BY Books.isbn";
        List<Map<String, Object>> result = databaseConnector.runParametrizedQuery(query, paramValues.toArray());
        // Storing data in the books datatype for now for simplicity. We should probably expand this data type out
        // or create a new one for the search results.
        BookSearchRecord[] books = result.stream().map(m -> new BookSearchRecord(
                (String) m.get("title"),
                (String) m.get("authors"),
                (String) m.get("genre"),
                (int) m.get("length"),
                (String) m.get("isbn"),
                (java.sql.Date)(m.get("publish_date")),
                (java.sql.Date)(m.get("added_date"))
        )).toArray(BookSearchRecord[]::new);
        return books;
    }

    public BookSearchRecord getBookRecord(String isbn) {
        String query = "SELECT Books.title, Books.genre, Books.length, Books.publish_date, Books.added_date," +
                "GROUP_CONCAT(Book_Authors.name SEPARATOR ', ') AS authors FROM Books JOIN Book_Authors ON Books.isbn=Book_Authors.isbn WHERE Books.isbn = ?";
        List<Map<String, Object>> result = databaseConnector.runParametrizedQuery(query, isbn);
        Map<String, Object> row = result.get(0);
        return new BookSearchRecord(
                (String) row.get("title"),
                (String) row.get("authors"),
                (String) row.get("genre"),
                (int) row.get("length"),
                isbn,
                (java.sql.Date) row.get("publish_date"),
                (java.sql.Date) row.get("added_date")
        );
    }

    public boolean isBookAvailable(String isbn) {
        String query = "SELECT COUNT(*) AS count FROM Copies WHERE isbn = ? " +
                "AND id NOT IN (SELECT copy_id FROM Checkouts WHERE return_date IS NULL)";
        List<Map<String, Object>> result = databaseConnector.runParametrizedQuery(query, isbn);
        Map<String, Object> row = result.get(0);
        return (long) row.get("count") > 0;
    }

    public boolean checkoutNextCopy(String isbn, int cardNumber) {
        String query = "SELECT id FROM Copies WHERE isbn = ? " +
                "AND id NOT IN (SELECT copy_id FROM Checkouts WHERE return_date IS NULL)";
        List<Map<String, Object>> result = databaseConnector.runParametrizedQuery(query, isbn);
        if (!result.isEmpty()) {
            checkoutBook((int) result.get(0).get("id"), cardNumber);
            return true;
        }
        return false;
    }

    public long numberOfAvailableCopies(String isbn) {
        String query = "SELECT COUNT(*) AS count FROM Copies WHERE isbn = ? " +
                "AND id NOT IN (SELECT copy_id FROM Checkouts WHERE return_date IS NULL)";
        List<Map<String, Object>> result = databaseConnector.runParametrizedQuery(query, isbn);
        Map<String, Object> row = result.get(0);
        return (long) row.get("count");
    }

}
