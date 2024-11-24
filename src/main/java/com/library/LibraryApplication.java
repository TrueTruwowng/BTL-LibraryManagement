package com.library;

import java.io.IOException;

import com.library.Controller.MusicController;
import com.library.Controller.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

import static com.library.DatabaseConnection.connectUserAccount;

public class LibraryApplication extends Application {
    @Override
        public void start (Stage stage) throws IOException {
        connectUserAccount();

            MusicController.getInstance().playMusic("src/main/resources/ScreenUI/music/background_music.mp3");
            MusicController.getInstance().setVolume(0.2);
        Connection con = DatabaseConnection.getConnection();
        SceneController.setPrimaryStage(stage);
        SceneController.loadLoginView(stage);


    }

    @Override
    public void stop() throws SQLException {
        // Đóng kết nối khi ứng dụng kết thúc
        DatabaseConnection.closeConnection();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
