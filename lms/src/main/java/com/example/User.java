package com.example;

import java.sql.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.ObjectProperty;

public class User {
    private SimpleIntegerProperty cardNumber = new SimpleIntegerProperty(this, "cardNumber");
    private SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private SimpleStringProperty phoneNumber = new SimpleStringProperty(this, "phoneNumber");
    private SimpleObjectProperty<Integer> checkouts = new SimpleObjectProperty<Integer>(this, "checkouts");
    private SimpleObjectProperty<Integer> fines = new SimpleObjectProperty<Integer>(this, "fines");
    private SimpleObjectProperty<Date> memberSince = new SimpleObjectProperty<Date>(this, "memberSince");

    public User(int cardNumber, String name, String phoneNumber, Integer checkouts, Integer fines, Date memberSince) {
        setCardNumber(cardNumber);
        setName(name);
        setPhoneNumber(phoneNumber);
        setCheckouts(checkouts);
        setFines(fines);
        setMemberSince(memberSince);
    }

    public int getCardNumber() {
        return cardNumber.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public Integer getCheckouts() {
        return checkouts.get();
    }

    public Integer getFines() {
        return fines.get();
    }

    public Date getMemberSince() {
        return memberSince.get();
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber.set(cardNumber);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setCheckouts(Integer checkouts) {
        this.checkouts.set(checkouts);
    }

    public void setFines(Integer fines) {
        this.fines.set(fines);
    }

    public void setMemberSince(Date memberSince) {
        this.memberSince.set(memberSince);
    }

    public SimpleIntegerProperty cardNumberProperty() {
        return cardNumber;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public SimpleObjectProperty<Integer> checkoutsProperty() {
        return checkouts;
    }

    public SimpleObjectProperty<Integer> finesProperty() {
        return fines;
    }

    public SimpleObjectProperty<Date> memberSinceProperty() {
        return memberSince;
    }

   
}
