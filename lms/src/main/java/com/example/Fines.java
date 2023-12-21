package com.example;

import java.sql.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Fines {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id");
    private SimpleIntegerProperty checkoutId = new SimpleIntegerProperty(this, "checkoutId");
    private SimpleStringProperty title = new SimpleStringProperty(this, "title");
    private SimpleStringProperty author = new SimpleStringProperty(this, "author");
    private SimpleStringProperty username = new SimpleStringProperty(this, "username");
    private ObjectProperty<Date> checkoutDate = new SimpleObjectProperty<Date>(this, "checkoutDate");
    private ObjectProperty<Date> assignedDate = new SimpleObjectProperty<Date>(this, "assignedDate");
    private ObjectProperty<Date> dueDate = new SimpleObjectProperty<Date>(this, "dueDate");
    private ObjectProperty<Date> paidDate = new SimpleObjectProperty<Date>(this, "paidDate");
    private SimpleDoubleProperty amount = new SimpleDoubleProperty(this, "amount");

    public Fines(int id, int checkoutId, String title, String author, Date checkoutDate, Date dueDate, Date assignedDate, double amount) {
        this.id.set(id);
        this.checkoutId.set(checkoutId);
        this.title.set(title);
        this.author.set(author);
        this.checkoutDate.set(checkoutDate);
        this.dueDate.set(dueDate);
        this.assignedDate.set(assignedDate);
        this.amount.set(amount);
        this.paidDate.set(null);
    }

    public Fines(int id, int checkoutId, String title, String author, String username, Date checkoutDate, Date dueDate, Date assignedDate, Date paidDate, double amount) {
        this.id.set(id);
        this.checkoutId.set(checkoutId);
        this.title.set(title);
        this.author.set(author);
        this.username.set(username);
        this.checkoutDate.set(checkoutDate);
        this.dueDate.set(dueDate);
        this.assignedDate.set(assignedDate);
        this.amount.set(amount);
    }

    public int getId() {
        return id.get();
    }

    public int getCheckoutId() {
        return checkoutId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getUsername() {
        return username.get();
    }

    public Date getCheckoutDate() {
        return checkoutDate.get();
    }

    public Date getDueDate() {
        return dueDate.get();
    }

    public Date getAssignedDate() {
        return assignedDate.get();
    }

    public Date getPaidDate() {
        return paidDate.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public final SimpleIntegerProperty idProperty() {
        return this.id;
    }

    public final SimpleIntegerProperty checkoutIdProperty() {
        return this.checkoutId;
    }

    public final SimpleStringProperty titleProperty() {
        return this.title;
    }

    public final SimpleStringProperty authorProperty() {
        return this.author;
    }

    public final SimpleStringProperty usernameProperty() {
        return this.username;
    }

    public final ObjectProperty<Date> checkoutDateProperty() {
        return this.checkoutDate;
    }

    public final ObjectProperty<Date> dueDateProperty() {
        return this.dueDate;
    }

    public final ObjectProperty<Date> assignedDateProperty() {
        return this.assignedDate;
    }

    public final ObjectProperty<Date> paidDateProperty() {
        return this.paidDate;
    }

    public final SimpleDoubleProperty amountProperty() {
        return this.amount;
    }
}
