package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import java.sql.SQLException;
import java.sql.Date;

public class BooksUserController implements Initializable {
    static DatabaseConnector databaseConnector = new DatabaseConnector();
    public static FXMLLoader fxmlLoader;
    @FXML
    public TableColumn<BookTableRecord, Integer> bcheckoutId;
    @FXML
    public TableColumn<BookTableRecord, String> btitle;
    @FXML
    public TableColumn<BookTableRecord, String> bauthor;
    @FXML
    public TableColumn<BookTableRecord, Date> bpublishDate;
    @FXML
    public TableColumn<BookTableRecord, Date> bcheckoutDate;
    @FXML
    public TableColumn<BookTableRecord, Date> bdueDate;
    public TableView<BookTableRecord> userAllTables;
    static ObservableList<BookTableRecord> booksObservableList;

    public Button renewButton;
    public Button returnButton;
    public Button goSearchButton;

    private static BookTableRecord selectedBook;
    public Label usernameID;

    // loads books and set to table to show available books
    public static void loadBooks() {
        System.out.println("Loading books...");
        String getCheckedOut = "SELECT Books.isbn, Books.title, Book_Authors.name, Books.publish_date, Checkouts.checkout_date, Checkouts.due_date, Checkouts.id AS checkout_id, Copies.id AS copy_id " +
                "FROM Checkouts " +
                "JOIN Copies ON Checkouts.copy_id = Copies.id " +
                "JOIN Books ON Copies.isbn = Books.isbn " +
                "JOIN Book_Authors ON Books.isbn = Book_Authors.isbn " +
                "WHERE Checkouts.card_number = ? " +
                "AND Checkouts.return_date IS NULL";
        booksObservableList = FXCollections.observableArrayList();
        databaseConnector.connect();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getCheckedOut,
                UserLoginSignupController.cardNumber);
        for (Map<String, Object> row : rows) {
            String isbn = (String) row.get("isbn");
            String title = (String) row.get("title");
            String author = (String) row.get("name");
            Date publishDate = (Date) row.get("publish_date");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Date returnDate = (Date) row.get("return_date");
            Integer checkoutId = (Integer) row.get("checkout_id");
            Integer copyId = (Integer) row.get("copy_id");
            BookTableRecord book = new BookTableRecord(isbn, title, author, publishDate, checkoutDate, dueDate, returnDate, checkoutId, copyId);
            booksObservableList.add(book);
        }
    }

    // closes connection and loads home scene
    public void logoutUser() {
        try {
            fxmlLoader = new FXMLLoader(App.class.getResource("lms.fxml"));
            App.getMainStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goSearch() {
        try {
            fxmlLoader = new FXMLLoader(App.class.getResource("bookSearch.fxml"));
            App.getMainStage().setScene(new Scene(fxmlLoader.load()));
            App.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renewBook() throws SQLException {
        String rewnewQuery = "UPDATE Checkouts " +
                "SET due_date = DATE_ADD(due_date, INTERVAL 7 DAY) " +
                "WHERE id = ? ";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(rewnewQuery, selectedBook.getCheckoutId());
        databaseConnector.close();

        loadBooks();
        selectedBook = null;
        renewButton.setDisable(true);
        returnButton.setDisable(true);
        userAllTables.setItems(booksObservableList);
    }

    public void returnBook() throws SQLException {
        String returnQuery = "UPDATE Checkouts " +
                "SET return_date = NOW() " +
                "WHERE id = ? ";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(returnQuery, selectedBook.getCheckoutId());
        databaseConnector.close();

        loadBooks();
        selectedBook = null;
        renewButton.setDisable(true);
        returnButton.setDisable(true);
        userAllTables.setItems(booksObservableList);
    }

    public void viewFines() {
        try {
            fxmlLoader = new FXMLLoader(App.class.getResource("userFines.fxml"));
            App.getMainStage().setScene(new Scene(fxmlLoader.load()));
            App.getMainStage().show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // supposed to initialize table to show available books
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks();
        userAllTables.setRowFactory(iv -> {
            TableRow<BookTableRecord> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedBook = row.getItem();
                returnButton.setDisable(selectedBook == null);
                renewButton.setDisable(selectedBook == null);
                loadBooks();
            });
            return row;
        });
        usernameID.setText(UserLoginSignupController.username);
        btitle.setCellValueFactory(new PropertyValueFactory<BookTableRecord, String>("title"));
        bauthor.setCellValueFactory(new PropertyValueFactory<BookTableRecord, String>("author"));
        bpublishDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("publishDate"));
        bcheckoutDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("checkoutDate"));
        bdueDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("dueDate"));
        userAllTables.setItems(booksObservableList);
    }
}
