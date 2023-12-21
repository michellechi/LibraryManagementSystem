package com.example;

import java.sql.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/* A class to hold queried Book data as it is returned from the database. */
public class BookSearchRecord {
    private String title;
    private String author;
    private String genre;
    private int length;
    private String isbn;

    private Date publishDate;
    private Date addedDate;

    public BookSearchRecord(String title, String author, String genre, int length, String isbn, Date publishDate, Date addedDate) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.length = length;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.addedDate = addedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getLength() {
        return length;
    }

    public String getIsbn() {
        return isbn;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public final SimpleStringProperty titleProperty() {
        SimpleStringProperty simpleTitle = new SimpleStringProperty(this, "title");
        simpleTitle.set(this.title);
        return simpleTitle;
    }

    public final SimpleStringProperty authorProperty() {
        SimpleStringProperty simpleAuthor = new SimpleStringProperty(this, "author");
        simpleAuthor.set(this.author);
        return simpleAuthor;
    }

    public final SimpleStringProperty genreProperty() {
        SimpleStringProperty simpleGenre = new SimpleStringProperty(this, "genre");
        simpleGenre.set(this.genre);
        return simpleGenre;
    }

    public final SimpleObjectProperty<Integer> lengthProperty() {
        SimpleObjectProperty<Integer> simpleLength = new SimpleObjectProperty<Integer>(this, "length");
        simpleLength.set(this.length);
        return simpleLength;
    }

    public final SimpleStringProperty isbnProperty() {
        SimpleStringProperty simpleIsbn = new SimpleStringProperty(this, "isbn");
        simpleIsbn.set(this.isbn);
        return simpleIsbn;
    }

    public final SimpleObjectProperty<Date> publishDateProperty() {
        SimpleObjectProperty<Date> simplePublishDate = new SimpleObjectProperty<Date>(this, "publishDate");
        simplePublishDate.set(this.publishDate);
        return simplePublishDate;
    }

    public final SimpleObjectProperty<Date> addedDateProperty() {
        SimpleObjectProperty<Date> simpleAddedDate = new SimpleObjectProperty<Date>(this, "addedDate");
        simpleAddedDate.set(this.addedDate);
        return simpleAddedDate;
    }

    public String toString() {
        return "BookRecord(" + title + ", " + author + ", " + genre + ", " + length + ", " + isbn + ", " + publishDate + ", " + addedDate + ")";
    }
}
