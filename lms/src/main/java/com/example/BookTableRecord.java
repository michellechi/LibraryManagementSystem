package com.example;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;

//Books data model class
public class BookTableRecord {
    private SimpleStringProperty isbn = new SimpleStringProperty(this, "isbn");
    private SimpleStringProperty title = new SimpleStringProperty(this, "title");
    private SimpleStringProperty author = new SimpleStringProperty(this, "author");
    private ObjectProperty<Date> publishDate = new SimpleObjectProperty<Date>(this, "publishDate");
    private ObjectProperty<Date> checkoutDate = new SimpleObjectProperty<Date>(this, "checkoutDate");
    private ObjectProperty<Date> dueDate = new SimpleObjectProperty<Date>(this, "dueDate");
    private ObjectProperty<Date> returnDate = new SimpleObjectProperty<Date>(this, "returnDate");
    private ObjectProperty<Integer> checkoutId = new SimpleObjectProperty<Integer>(this, "checkoutId");
    private ObjectProperty<Integer> copyId = new SimpleObjectProperty<Integer>(this, "copyId");

    public BookTableRecord(String isbn, String title, String author, Date year, Date checkoutDate, Date dueDate, Date returnDate, Integer checkoutId, Integer copyId) {
        setIsbn(isbn);
        setTitle(title);
        setAuthor(author);
        setPublishDate(year);
        setCheckoutDate(checkoutDate);
        setDueDate(dueDate);
        setReturnDate(returnDate);
        setCheckoutId(checkoutId);
        setCopyId(copyId);
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate.set(publishDate);
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate.set(checkoutDate);
    }

    public void setDueDate(Date dueDate) {
        this.dueDate.set(dueDate);
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate.set(returnDate);
    }

    public void setCheckoutId(Integer copy) {
        this.checkoutId.set(copy);
    }

    public void setCopyId(Integer copyId) {
        this.copyId.set(copyId);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public Date getPublishDate() {
        return publishDate.get();
    }

    public Date getCheckoutDate() {
        return checkoutDate.get();
    }

    public Date getDueDate() {
        return dueDate.get();
    }

    public Date getReturnDate() {
        return returnDate.get();
    }

    public Integer getCheckoutId() {
        return checkoutId.get();
    }

    public Integer getCopyId() {
        return copyId.get();
    }

    public final SimpleStringProperty isbnProperty() {
        return this.isbn;
    }

    public final SimpleStringProperty titleProperty() {
        return this.title;
    }

    public final SimpleStringProperty authorProperty() {
        return this.author;
    }

    public final ObjectProperty<Date> publishDateProperty() {
        return this.publishDate;
    }

    public final ObjectProperty<Date> checkoutDateProperty() {
        return this.checkoutDate;
    }

    public final ObjectProperty<Date> dueDateProperty() {
        return this.dueDate;
    }

    public final ObjectProperty<Date> returnDateProperty() {
        return this.returnDate;
    }

    public final ObjectProperty<Integer> checkoutIdProperty() {
        return this.checkoutId;
    }

    public final ObjectProperty<Integer> copyIdProperty() {
        return this.copyId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title.get() +
                ", author=" + author.get() +
                ", publishDate=" + publishDate.get() +
                ", checkoutDate=" + checkoutDate.get() +
                ", dueDate=" + dueDate.get() +
                ", returnDate=" + returnDate.get() +
                ", checkoutId=" + checkoutId.get() +
                ", copyId=" + copyId.get() +
                '}';
    }
}
