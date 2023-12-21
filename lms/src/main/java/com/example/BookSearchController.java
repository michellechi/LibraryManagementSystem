package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.sql.Date;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BookSearchController implements Initializable {
    static DatabaseConnector databaseConnector = new DatabaseConnector();

    static LibraryManager l = new LibraryManager(databaseConnector);

    public static FXMLLoader fxmlLoader;

    public Button backButton;
    public Button searchButton;

    public TextField title;
    public TextField author;
    public TextField genre;
    public TextField length;
    public ChoiceBox<String> lengthMethod;
    public DatePicker publishDate;
    public ChoiceBox<String> publishDateMethod;
    public CheckBox availableOnly;

    @FXML
    private TableView<BookSearchRecord> searchResults;
    static ObservableList<BookSearchRecord> searchResultsObservableList;

    @FXML
    private TableColumn<BookSearchRecord, String> stitle;
    @FXML
    private TableColumn<BookSearchRecord, String> sauthor;
    @FXML
    private TableColumn<BookSearchRecord, String> sgenre;
    @FXML
    private TableColumn<BookSearchRecord, Integer> slength;
    @FXML
    private TableColumn<BookSearchRecord, String> sisbn;
    @FXML
    private TableColumn<BookSearchRecord, Date> spublishDate;
    @FXML
    private TableColumn<BookSearchRecord, Date> saddedDate;
    

    // go back to userPanel
    @FXML
    private void goBack() {
        try {
            databaseConnector.close();
            fxmlLoader = new FXMLLoader(App.class.getResource("userPanel.fxml"));
            App.mainStage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void runSearch() {
        Map<String, Object> searchParams = new HashMap<>();
        if (!title.getText().equals("")) searchParams.put("title", title.getText());
        if (!author.getText().equals("")) searchParams.put("author", author.getText());
        if (!genre.getText().equals("")) searchParams.put("genre", genre.getText());
        if (!length.getText().equals("")) {
            int len = 0;
            try {
                len = Integer.parseInt(length.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            searchParams.put("length", len);
            searchParams.put("lengthMethod", lengthMethod.getValue());
        }
        if (publishDate.getValue() != null) {
            System.out.println(publishDate.getValue());
            searchParams.put("publishDate", publishDate.getValue().toString());
            searchParams.put("publishDateMethod", publishDateMethod.getValue());
        }
        searchParams.put("availableOnly", availableOnly.isSelected());
        BookSearchRecord[] results = l.getBooksFromQuery(searchParams);
        searchResultsObservableList = FXCollections.observableList(Arrays.asList(results));
        searchResults.setItems(searchResultsObservableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnector.connect();
        lengthMethod.getItems().addAll("=", "<", ">");
        lengthMethod.setValue("=");
        publishDateMethod.getItems().addAll("=", "<", ">");
        publishDateMethod.setValue("=");
        
        searchResults.setRowFactory(iv -> {
            TableRow<BookSearchRecord> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                BookSearchRecord selectedBook = row.getItem();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("bookDetails.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    BookDetailsController controller = fxmlLoader.getController();
                    controller.setIsbn(selectedBook.getIsbn());
                    App.mainStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return row;
        });
        stitle.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        sauthor.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        sgenre.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        slength.setCellValueFactory(cellData -> cellData.getValue().lengthProperty());
        sisbn.setCellValueFactory(cellData -> cellData.getValue().isbnProperty());
        spublishDate.setCellValueFactory(cellData -> cellData.getValue().publishDateProperty());
        saddedDate.setCellValueFactory(cellData -> cellData.getValue().addedDateProperty());
        searchResults.setItems(searchResultsObservableList);
    }

}
