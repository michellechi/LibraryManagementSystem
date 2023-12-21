package com.example;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
    After calling load, please run the following code before setting the stage to properly set the isbn param:
    BookDetailsController c = fxmlLoader.getController();
    c.setIsbn(isbn);
*/
public class BookDetailsController implements Initializable {
    static DatabaseConnector databaseConnector = new DatabaseConnector();

    static LibraryManager l = new LibraryManager(databaseConnector);

    public static FXMLLoader fxmlLoader;

    public Button backButton;
    public Button checkoutButton;

    public Label bookTitle;
    public Label bookAuthors;
    public Label bookLength;
    public Label bookGenre;
    public Label bookPublishDate;
    public Label bookCopies;

    public Label bookIsbn;

    private String isbn;

    // go back to userPanel (TODO: change to bookSearch)
    public void goBack() {
        try {
            databaseConnector.close();
            fxmlLoader = new FXMLLoader(App.class.getResource("bookSearch.fxml"));
            App.mainStage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goHome() {
        try {
            databaseConnector.close();
            fxmlLoader = new FXMLLoader(App.class.getResource("userPanel.fxml"));
            App.mainStage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // runLater allows us to call setIsbn on the Controller before grabbing the data.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            databaseConnector.connect();
            BookSearchRecord bookInfo = l.getBookRecord(isbn);
            long nCopies = l.numberOfAvailableCopies(isbn);
            bookTitle.setText(bookInfo.getTitle());
            bookAuthors.setText("By " + bookInfo.getAuthor());
            bookLength.setText("Length: " + bookInfo.getLength() + " pages");
            bookGenre.setText("Genre: " + bookInfo.getGenre());
            bookPublishDate.setText("Published on " + bookInfo.getPublishDate().toString());
            bookCopies.setText("Copies available: " + nCopies);
            bookIsbn.setText(isbn);
            checkoutButton.setDisable(nCopies == 0);
        });
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void checkoutBook() {
        try {
            l.checkoutNextCopy(isbn, UserLoginSignupController.cardNumber);
            goHome();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
