package com.example;

import java.util.Map;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

public class UserLoginSignupController {

    public TextField signupName;
    public TextField signupPhoneNumber;
    public Label signupError;
    public Button signupButton;
    public TextField loginCardNumber;
    public Label loginError;
    FXMLLoader fxmlLoader;
    static DatabaseConnector databaseConnector = new DatabaseConnector();
    static String username;
    static int cardNumber;

    // leads back to home
    public void homepage() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("lms.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    public void resetSignupFields() {
        signupName.setText("");
        signupPhoneNumber.setText("");
        signupError.setText("");
    }

    public void resetLoginFields() {
        loginCardNumber.setText("");
        loginError.setText("");
    }

    public void userLoginButton() {
        databaseConnector.connect();
        try {
            databaseConnector.connect();
            List<Map<String, Object>> rows = databaseConnector
                    .runParametrizedQuery("SELECT * FROM Users WHERE card_number = ?",
                            Integer.parseInt(loginCardNumber.getText()));
            if (rows.size() == 1) {
                cardNumber = Integer.parseInt(loginCardNumber.getText());
                username = rows.get(0).get("name").toString();
                fxmlLoader = new FXMLLoader(App.class.getResource("userPanel.fxml"));
                App.mainStage.setScene(new Scene(fxmlLoader.load()));
            } else {
                loginError.setText("Invalid card number");
            }
            databaseConnector.close();
        } catch (IOException e) {
            loginError.setText("Could not connect to database. Please try again later.");
            e.printStackTrace();
        }
    }

    public void signUp() {
        String cardNumber = "";
        if (validate()) {
            databaseConnector.connect();
            String query = "INSERT INTO Users (name, phone_number) VALUES (?, ?)";
            databaseConnector.runParametrizedQuery(query, signupName.getText(),
                    signupPhoneNumber.getText());
            String cardNumberQuery = "SELECT LAST_INSERT_ID()";
            List<Map<String, Object>> rows = databaseConnector.runParametrizedQuery(cardNumberQuery);
            if (rows.size() > 0) {
                Map<String, Object> resultSet = rows.get(0);
                cardNumber = resultSet.get("LAST_INSERT_ID()").toString();
                signupError
                        .setText("Signup Successful! Your card number is " + cardNumber);
            } else {
                signupError.setText("Signup failed");
            }
            databaseConnector.close();
        }
    }

    public boolean validate() {
        String existingUser = "SELECT * FROM Users WHERE (name = ? AND phone_number = ?)";
        databaseConnector.connect();
        if (signupPhoneNumber.getText().contains(" ")) {
            signupError.setText("Fields can't contain spaces");
            databaseConnector.close();
            return false;
        } else if (signupPhoneNumber.getLength() != 10) {
            signupError.setText("Phone number should be 10 digits");
            databaseConnector.close();
            return false;
        } else if (signupName.getText().isEmpty()
                || signupPhoneNumber.getText().isEmpty()) {
            signupError.setText("All fields are mandatory");
            databaseConnector.close();
            return false;
        } else if (databaseConnector.runParametrizedQuery(existingUser, signupName.getText(),
                signupPhoneNumber.getText()).size() > 0) {
            signupError.setText("User already exists. Please login or reset your account.");
            databaseConnector.close();
            return false;
        } else {
            return true;
        }
    }

    // leads to signup panel
    public void goToSignup() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("userSignup.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    // leads to signin pane;
    public void goToSignin() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("userLogin.fxml"));
        App.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

}
