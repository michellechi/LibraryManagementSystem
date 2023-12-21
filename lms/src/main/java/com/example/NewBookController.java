package com.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewBookController {
    DatabaseConnector databaseConnector = new DatabaseConnector();
    public Button newBookCancel;
    public Button newBookAdd;
    public Label newBookError;
    public TextField newBookIsbn;
    public TextField newBookTitle;
    public TextField newBookAuthor;
    public TextField newBookGenre;
    public TextField newBookLength;
    public DatePicker newBookPubDate;
    public TextField newBookCopies;
    public Scene panelScene;
    public static Connection connection;
    public static TableView<BookTableRecord> booksTable;

    // loads scene for adding book
    void addBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newBook.fxml"));
            panelScene = App.getMainStage().getScene();
            App.mainStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Book2DB() throws SQLException, IOException {
        databaseConnector.connect();
        String newBookQuery = "INSERT INTO Books (isbn, title, genre, length, publish_date) VALUES(?, ?, ?, ?, ?)";
        databaseConnector.runParametrizedQuery(newBookQuery,
                newBookIsbn.getText(),
                newBookTitle.getText(),
                newBookGenre.getText(),
                Integer.parseInt(newBookLength.getText()),
                newBookPubDate.getValue());

        for (int i = 0; i < Integer.parseInt(newBookCopies.getText()); i++) {
            String newBookCopyQuery = "INSERT INTO Copies VALUES(?)";
            databaseConnector.runParametrizedQuery(newBookCopyQuery, newBookIsbn.getText());
        }
        String newBookAuthorQuery = "INSERT INTO Book_Authors (isbn, name) VALUES(?, ?)";
        databaseConnector.runParametrizedQuery(newBookAuthorQuery, newBookIsbn.getText(), newBookAuthor.getText());
        databaseConnector.close();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("adminPanel.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    // cancel button back to admin panel
    public void cancelNewBook() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("adminPanel.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }
}
