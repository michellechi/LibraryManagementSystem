package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

//Main UI
public class App extends Application {
    public static FXMLLoader fxmlLoader;
    static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        fxmlLoader = new FXMLLoader(App.class.getResource("lms.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    // admin login button handler
    public void admin() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("adminLogin.fxml"));
        mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    // user login button handler
    public void user() throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource("userLogin.fxml"));
        mainStage.setScene(new Scene(fxmlLoader.load()));
    }
}