package com.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import static com.library.DatabaseConnection.connectUserAccount;

public class LibraryApplication extends Application {
    @Override
        public void start (Stage stage) throws IOException {
        connectUserAccount();
        Connection con = DatabaseConnection.getConnection();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setTitle("Library Application");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}