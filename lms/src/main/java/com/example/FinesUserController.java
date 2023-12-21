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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.math.BigDecimal;

public class FinesUserController implements Initializable {
    static DatabaseConnector databaseConnector = new DatabaseConnector();
    public static FXMLLoader fxmlLoader;

    @FXML
    public TableColumn<Fines, Integer> uid;
    @FXML
    public TableColumn<Fines, String> utitle;
    @FXML
    public TableColumn<Fines, String> uauthor;
    @FXML
    public TableColumn<Fines, Date> ucheckoutDate;
    @FXML
    public TableColumn<Fines, Date> udueDate;
    @FXML
    public TableColumn<Fines, Date> uassignedDate;
    @FXML
    public TableColumn<Fines, Double> uamount;

    @FXML
    public TableColumn<Fines, Integer> pid;
    @FXML
    public TableColumn<Fines, String> ptitle;
    @FXML
    public TableColumn<Fines, String> pauthor;
    @FXML
    public TableColumn<Fines, Date> pcheckoutDate;
    @FXML
    public TableColumn<Fines, Date> pdueDate;
    @FXML
    public TableColumn<Fines, Date> passignedDate;
    @FXML
    public TableColumn<Fines, Double> pamount;

    public TableView<Fines> unpaidFinesTable;
    public TableView<Fines> paidFinesTable;


    static ObservableList<Fines> unpaidObservableList;
    static ObservableList<Fines> paidObservableList;

    private static Fines selectedFine;

    public Button payFineButton;

    public Label usernameID;


    public static void loadFines(){
        loadUnpaidFines();
        loadPaidFines();
    }

    public static void checkoutToFine(){
        String getFinesQuery = "SELECT Checkouts.id as checkout_id, Books.title, Book_Authors.name, Checkout.checkout_date, Checkout.due_date" +
        "FROM Checkouts " + 
        "JOIN Copies ON Checkouts.copy_id = Copies.id " +
        "JOIN Books ON Copies.isbn = Books.isbn " +
        "JOIN Book_Authors ON Books.isbn = Book_Authors.isbn " +
        "WHERE Checkouts.card_number = ? " +
        "AND Checkouts.due_date < NOW()";
        unpaidObservableList = FXCollections.observableArrayList();
        databaseConnector.connect();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getFinesQuery, UserLoginSignupController.cardNumber);
        databaseConnector.printResults(rows);
        for (Map<String, Object> row : rows) {
            Integer id = (Integer) row.get("id");
            Integer checkoutId = (Integer) row.get("checkout_id");
            String title = (String) row.get("title");
            String author = (String) row.get("name");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Double amount = ((BigDecimal) row.get("amount")).doubleValue();
            Date assignedDate = (Date) row.get("assigned_date");

            Fines fine = new Fines(id, checkoutId, title, author, checkoutDate, dueDate, assignedDate, amount);
            unpaidObservableList.add(fine);
        }
    }

    public static void loadUnpaidFines() {
        System.out.println("Loading unpaid fines...");
        String getFinesQuery = "SELECT Fines.id, Checkouts.id AS checkout_id, Books.title, Book_Authors.name, Checkouts.checkout_date, Checkouts.due_date, Fines.amount, Fines.assigned_date " +
                "FROM Fines " +
                "JOIN Checkouts ON Fines.checkout_id = Checkouts.id " +
                "JOIN Copies ON Checkouts.copy_id = Copies.id " +
                "JOIN Books ON Copies.isbn = Books.isbn " +
                "JOIN Book_Authors ON Books.isbn = Book_Authors.isbn " +
                "WHERE Checkouts.card_number = ? " +
                "AND Fines.paid_date IS NULL " +
                "AND Checkouts.due_date < NOW()";
        unpaidObservableList = FXCollections.observableArrayList();
        databaseConnector.connect();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getFinesQuery, UserLoginSignupController.cardNumber);
        for (Map<String, Object> row : rows) {
            Integer id = (Integer) row.get("id");
            Integer checkoutId = (Integer) row.get("checkout_id");
            String title = (String) row.get("title");
            String author = (String) row.get("name");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Double amount = ((BigDecimal) row.get("amount")).doubleValue();
            Date assignedDate = (Date) row.get("assigned_date");

            Fines fine = new Fines(id, checkoutId, title, author, checkoutDate, dueDate, assignedDate, amount);
            unpaidObservableList.add(fine);
        }
        databaseConnector.printResults(rows);
    }

    public static void loadPaidFines() {
        System.out.println("Loading paid fines...");
        String getFinesQuery = "SELECT Fines.id, Checkouts.id AS checkout_id, Books.title, Book_Authors.name, Checkouts.checkout_date, Checkouts.due_date, Fines.amount, Fines.assigned_date " +
                "FROM Fines " +
                "JOIN Checkouts ON Fines.checkout_id = Checkouts.id " +
                "JOIN Copies ON Checkouts.copy_id = Copies.id " +
                "JOIN Books ON Copies.isbn = Books.isbn " +
                "JOIN Book_Authors ON Books.isbn = Book_Authors.isbn " +
                "WHERE Checkouts.card_number = ? " +
                "AND Fines.paid_date IS NOT NULL";
        paidObservableList = FXCollections.observableArrayList();
        databaseConnector.connect();
        List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(getFinesQuery, UserLoginSignupController.cardNumber);
        for (Map<String, Object> row : rows) {
            Integer id = (Integer) row.get("id");
            Integer checkoutId = (Integer) row.get("checkout_id");
            String title = (String) row.get("title");
            String author = (String) row.get("name");
            Date checkoutDate = (Date) row.get("checkout_date");
            Date dueDate = (Date) row.get("due_date");
            Double amount = ((BigDecimal) row.get("amount")).doubleValue();
            Date assignedDate = (Date) row.get("assigned_date");
    
            Fines fine = new Fines(id, checkoutId, title, author, checkoutDate, dueDate, assignedDate, amount);
            paidObservableList.add(fine);
        }
        databaseConnector.printResults(rows);
    }
    
    public void payFineButton() throws SQLException {
        System.out.println("Pay Fines Button pressed...");
        String updateFinesQuery = "UPDATE Fines " +
                "SET paid_date = NOW() " +
                "WHERE id = ? ";
        databaseConnector.connect();
        databaseConnector.runParametrizedQuery(updateFinesQuery, selectedFine.getId());
        databaseConnector.close();

        loadFines();
        loadPaidFines();
        selectedFine = null;
        payFineButton.setDisable(true);
        unpaidFinesTable.setItems(unpaidObservableList);
        paidFinesTable.setItems(paidObservableList);
    }

    public void back() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("userPanel.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    // initializes the table to show fines
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFines();
        unpaidFinesTable.setRowFactory(iv -> {
            TableRow<Fines> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedFine = row.getItem();
                payFineButton.setDisable(selectedFine == null);
                loadFines();
            });
            return row;
        });

        paidFinesTable.setRowFactory(iv -> {
            TableRow<Fines> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                selectedFine = row.getItem();
                payFineButton.setDisable(selectedFine == null);
                loadFines();
            });
            return row;
        });

        // fcheckoutId.setCellValueFactory(new PropertyValueFactory<>("id"));
        uid.setCellValueFactory(new PropertyValueFactory<>("id"));
        utitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        uauthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        ucheckoutDate.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        udueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        uassignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        uamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        unpaidFinesTable.setItems(unpaidObservableList);

        pid.setCellValueFactory(new PropertyValueFactory<>("id"));
        ptitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        pauthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        pcheckoutDate.setCellValueFactory(new PropertyValueFactory<>("checkoutDate"));
        pdueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        passignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        pamount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paidFinesTable.setItems(paidObservableList);
    }

}
