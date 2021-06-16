package ru.storage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartClient extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ru.storage/authWindow.fxml"));
        primaryStage.setTitle("Storage");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
