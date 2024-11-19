package com.library;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import static com.library.databaseConnection.connectUserAccount;

public class libraryApplication extends Application {
    @Override
        public void start (Stage stage) throws IOException {
        connectUserAccount();
        Connection con = databaseConnection.getConnection();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(libraryApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 900, 600);
            stage.setTitle("Library Application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws SQLException {
        // Đóng kết nối khi ứng dụng kết thúc
        databaseConnection.closeConnection();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
