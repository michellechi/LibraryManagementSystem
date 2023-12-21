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
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import java.sql.Date;

public class AdminPanelController implements Initializable {
    static DatabaseConnector databaseConnector = new DatabaseConnector();
    @FXML
    private TableView<BookTableRecord> adminBooksTable;
    static ObservableList<BookTableRecord> booksObservableList;
    @FXML
    public TableColumn<BookTableRecord, String> btitle;
    @FXML
    public TableColumn<BookTableRecord, String> bauthor;
    @FXML
    public TableColumn<BookTableRecord, Date> bpublishDate;
    @FXML
    public TableColumn<BookTableRecord, Integer> bcopyId;
    @FXML
    public TableColumn<BookTableRecord, Integer> bcheckoutId;
    @FXML
    public TableColumn<BookTableRecord, Date> bcheckoutDate;
    @FXML
    public TableColumn<BookTableRecord, Date> bdueDate;
    @FXML
    public TableColumn<BookTableRecord, Date> breturnDate;

    @FXML
    private TableView<User> adminUsersTable;
    static ObservableList<User> usersObservableList;
    @FXML
    public TableColumn<User, Integer> ucardNumber;
    @FXML
    public TableColumn<User, String> uname;
    @FXML
    public TableColumn<User, String> uphoneNumber;
    @FXML
    public TableColumn<User, Integer> ucheckouts;
    @FXML
    public TableColumn<User, Integer> ufines;
    @FXML
    public TableColumn<User, Date> umemberSince;


    public TableView<Fines> adminFinesTable;
    static ObservableList<Fines> finesObservableList;
    @FXML
    public TableColumn<Fines, Integer> fid;
    @FXML
    public TableColumn<Fines, String> fusername;
    @FXML
    public TableColumn<Fines, String> ftitle;
    @FXML
    public TableColumn<Fines, String> fauthor;
    @FXML
    public TableColumn<Fines, Date> fcheckoutDate;
    @FXML
    public TableColumn<Fines, Date> fdueDate;
    @FXML
    public TableColumn<Fines, Date> fassignedDate;
    @FXML
    public TableColumn<Fines, Date> fpaidDate;
    @FXML
    public TableColumn<Fines, Double> famount;


    public Button deleteUserButton;
    public Button deleteBookButton;
    public Button addCopyButton;
    public Button addBookButton;
    public Button deleteFineButton;

    private static User selectedUser;
    private static BookTableRecord selectedBook;
    private static Fines selectedFine;

    private static ObservableList<User> userList;
    private static ObservableList<BookTableRecord> bookList;
    private static ObservableList<Fines> fineList;
    public static FXMLLoader fxmlLoader;

    public AdminPanelController() {
        bookList = FXCollections.observableArrayList();
        userList = FXCollections.observableArrayList();
        fineList = FXCollections.observableArrayList();
    }

    public static void loadBooks() {
        String getDataQuery = "SELECT " +
                "    Books.isbn, " +
                "    Books.title, " +
                "    Book_Authors.name, " +
                "    Books.publish_date, " +
                "    Checkouts.checkout_date, " +
                "    Checkouts.due_date, " +
                "    Checkouts.id AS checkout_id, " +
                "    Copies.id AS copy_id " +
                "FROM Books " +
                "LEFT JOIN Copies ON Copies.isbn = Books.isbn " +
                "LEFT JOIN ( " +
                "    SELECT copy_id, MAX(id) AS max_checkout_id " +
                "    FROM Checkouts " +
                "    GROUP BY copy_id " +
                ") MaxCheckouts ON Copies.id = MaxCheckouts.copy_id " +
                "LEFT JOIN Checkouts ON MaxCheckouts.max_checkout_id = Checkouts.id " +
                "LEFT JOIN Book_Authors ON Books.isbn = Book_Authors.isbn;";
        databaseConnector.connect();
        booksObservableList = FXCollections.observableArrayList();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getDataQuery);
        for (Map<String, Object> row : rows) {
            String isbn = (String) row.get("isbn");
            String title = Objects.toString(row.get("title"), "");
            String author = Objects.toString(row.get("name"), "");
            Date publishDate = (Date) row.get("publish_date");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Date returnDate = (Date) row.get("return_date");
            Integer checkoutId = (Integer) row.get("checkout_id");
            Integer copyId = (Integer) row.get("copy_id");
            BookTableRecord book = new BookTableRecord(isbn, title, author, publishDate, checkoutDate, dueDate, returnDate,
                    checkoutId, copyId);
            booksObservableList.add(book);
        }
    }

    public static void loadUsers() {
        String getDataQuery = "SELECT " +
                "u.name, " +
                "u.card_number, " +
                "u.phone_number, " +
                "COUNT(c.id) AS checkouts, " +
                "COUNT(f.id) AS fines, " +
                "DATE(u.created_at) AS member_since " +
                "FROM " +
                "Users u " +
                "LEFT JOIN " +
                "Checkouts c ON u.card_number = c.card_number AND c.return_date IS NULL " +
                "LEFT JOIN " +
                "Fines f ON c.id = f.checkout_id AND f.paid_date IS NULL " +
                "GROUP BY " +
                "u.card_number, u.name, u.phone_number, u.created_at;";
        databaseConnector.connect();
        usersObservableList = FXCollections.observableArrayList();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getDataQuery);
        databaseConnector.close();
        for (Map<String, Object> row : rows) {
            int cardNumber = (int) row.get("card_number");
            String name = Objects.toString(row.get("name"), "");
            String phoneNumber = Objects.toString(row.get("phone_number"), "");
            Integer checkouts = Integer.valueOf(((Long) row.get("checkouts")).intValue());
            Integer fines = Integer.valueOf(((Long) row.get("fines")).intValue());
            Date memberSince = (Date) row.get("member_since");
            User user = new User(cardNumber, name, phoneNumber, checkouts, fines, memberSince);
            usersObservableList.add(user);
        }
    }

    public static void loadFines() {
        System.out.println("Loading fines...");
        String getFinesQuery = "SELECT Fines.id, Checkouts.id AS checkout_id, Users.name as user_name, Books.title, Book_Authors.name, Checkouts.checkout_date, Checkouts.due_date, Fines.paid_date, Fines.amount, Fines.assigned_date " +
                "FROM Fines " +
                "JOIN Checkouts ON Fines.checkout_id = Checkouts.id " +
                "JOIN Copies ON Checkouts.copy_id = Copies.id " +
                "JOIN Books ON Copies.isbn = Books.isbn " +
                "JOIN Book_Authors ON Books.isbn = Book_Authors.isbn " +
                "JOIN Users ON Users.card_number = Checkouts.card_number " +
                "AND Checkouts.due_date < NOW()";
        databaseConnector.connect();
        finesObservableList = FXCollections.observableArrayList();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getFinesQuery);
        databaseConnector.close();
        databaseConnector.printResults(rows);
        for (Map<String, Object> row : rows) {
            Integer id = (Integer) row.get("id");
            Integer checkoutId = (Integer) row.get("checkout_id");
            String username = (String) row.get("user_name");
            String title = (String) row.get("title");
            String author = (String) row.get("name");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Date paidDate = (Date) row.get("paid_date");
            Double amount = ((BigDecimal) row.get("amount")).doubleValue();
            Date assignedDate = (Date) row.get("assigned_date");
    
            Fines fine = new Fines(id, checkoutId, title, author, username, checkoutDate, dueDate, assignedDate, paidDate, amount);
            finesObservableList.add(fine);
        }
    }

    // Closes the connection and returns to home
    public void logoutAdmin() {
        try {
            fxmlLoader = new FXMLLoader(App.class.getResource("lms.fxml"));
            App.getMainStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void insertBook() {
        NewBookController nbc = new NewBookController();
        nbc.addBook();
        loadBooks();
    }

    @FXML
    void addCopy() {
        String addCopyQuery = "INSERT INTO Copies (isbn) VALUES (?)";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(addCopyQuery, selectedBook.getIsbn());
        databaseConnector.close();
        selectedBook = null;
        addCopyButton.setDisable(true);
        loadBooks();
        adminBooksTable.setItems(booksObservableList);
    }

    @FXML
    void deleteUser() {
        String deleteQuery = "DELETE FROM Users WHERE card_number = ?";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(deleteQuery, selectedUser.getCardNumber());
        databaseConnector.close();
        selectedBook = null;
        deleteUserButton.setDisable(true);
        loadUsers();
        adminUsersTable.setItems(usersObservableList);
    }

    @FXML
    void deleteBook() {
        String deleteCopyQuery = "DELETE FROM Copies WHERE id = ?";
        String deleteBookQuery = "DELETE FROM Books WHERE isbn = (CASE WHEN (SELECT COUNT(*) FROM Copies WHERE isbn = ?) = 0 THEN ? END);";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(deleteCopyQuery, selectedBook.getCopyId());
        databaseConnector.runParametrizedQuery(deleteBookQuery, selectedBook.getIsbn(), selectedBook.getIsbn());
        databaseConnector.close();
        deleteBookButton.setDisable(true);
        loadBooks();
        adminBooksTable.setItems(booksObservableList);
    }

    @FXML
    void deleteFine() {
        String deleteFineQuery = "DELETE FROM Fines WHERE id = ?";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery((deleteFineQuery), selectedFine.getCheckoutId());
        databaseConnector.close();
        selectedFine = null;
        deleteFineButton.setDisable(true);
        loadFines();
        adminFinesTable.setItems(finesObservableList);
    }

    // initializes the table
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadBooks();
        adminBooksTable.setRowFactory(iv -> {
            TableRow<BookTableRecord> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedBook = row.getItem();
                addCopyButton.setDisable(selectedBook == null);
                deleteBookButton.setDisable(selectedBook == null);
                loadBooks();
            });
            return row;
        });
        btitle.setCellValueFactory(new PropertyValueFactory<BookTableRecord, String>("title"));
        bauthor.setCellValueFactory(new PropertyValueFactory<BookTableRecord, String>("author"));
        bpublishDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("publishDate"));
        bcheckoutDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("checkoutDate"));
        bdueDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("dueDate"));
        bcheckoutId.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Integer>("checkoutId"));
        bcopyId.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Integer>("copyId"));
        breturnDate.setCellValueFactory(new PropertyValueFactory<BookTableRecord, Date>("returnDate"));
        adminBooksTable.setItems(booksObservableList);

        loadUsers();
        adminUsersTable.setRowFactory(iv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedUser = row.getItem();
                deleteUserButton.setDisable(selectedUser == null);
                loadUsers();
            });
            return row;
        });
        ucardNumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
        uname.setCellValueFactory(new PropertyValueFactory<>("name"));
        uphoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        ucheckouts.setCellValueFactory(new PropertyValueFactory<>("checkouts"));
        ufines.setCellValueFactory(new PropertyValueFactory<>("fines"));
        umemberSince.setCellValueFactory(new PropertyValueFactory<>("memberSince"));
        adminUsersTable.setItems(usersObservableList);


        loadFines();
        adminFinesTable.setRowFactory(iv -> {
            TableRow<Fines> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedFine = row.getItem();
                deleteFineButton.setDisable(selectedFine == null);
                loadFines();
            });
            return row;
        });
        fid.setCellValueFactory(new PropertyValueFactory<>("id"));
        ftitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        fauthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        fusername.setCellValueFactory(new PropertyValueFactory<>("username"));
        fcheckoutDate.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        fdueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        fassignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        fpaidDate.setCellValueFactory(new PropertyValueFactory<>("paidDate"));
        famount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        adminFinesTable.setItems(finesObservableList);
    }
}
