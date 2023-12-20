package com.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class AdminLoginController {
    private Properties properties;

    public TextField adminUsername;
    public TextField adminPassword;
    public Label signinError;
    public static Connection conn;

    boolean connected = false;
    public static FXMLLoader fxmlLoader;

    private void loadProperties() {
        this.properties = new Properties();
        System.out.println(System.getProperty("user.dir"));
        try (FileInputStream fis = new FileInputStream("secrets.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // reset button handler
    public void resetFields() {
        adminUsername.clear();
        adminPassword.clear();
        signinError.setText("");
    }

    // admin login button handler
    public void adminLoginButton() throws IOException {
        loadProperties();
        if (adminUsername.getText().equals(properties.getProperty("username").toString())
                && adminPassword.getText().equals(properties.getProperty("password").toString())) {
            signinError.setText("");
            fxmlLoader = new FXMLLoader(App.class.getResource("adminPanel.fxml"));
            App.getMainStage().setScene(new Scene(fxmlLoader.load()));
        } else {
            signinError.setText("Invalid Credentials.");
        }
    }

    public void homepage() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("lms.fxml"));
        App.getMainStage().setScene(new Scene(fxmlLoader.load()));
    }
}
