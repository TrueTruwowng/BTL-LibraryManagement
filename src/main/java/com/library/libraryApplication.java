package com.library;

import java.io.IOException;

import com.library.Controller.sceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import static com.library.Controller.sceneController.stage;
import static com.library.databaseConnection.connectUserAccount;

public class libraryApplication extends Application {
    @Override
        public void start (Stage stage) throws IOException {
        connectUserAccount();
        Connection con = databaseConnection.getConnection();
        sceneController.setPrimaryStage(stage);
        sceneController.loadLoginView(stage);


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
