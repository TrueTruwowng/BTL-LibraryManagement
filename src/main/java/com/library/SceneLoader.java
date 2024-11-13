package com.library;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {

    // Phương thức để load màn hình với FXML từ đường dẫn và set Scene mới
    public static void loadScreen(String fxmlFile, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức để load login view từ màn hình đăng nhập
    public static void loadLoginView(Stage stage) {
        loadScreen("/com/library/login-view.fxml", stage, "Login");
    }

    // Phương thức để load màn hình đăng ký
    public static void loadRegisterView(Stage stage) {
        loadScreen("/com/library/register-view.fxml", stage, "Register");
    }

    // Phương thức để load màn hình thư viện
    public static void loadLibraryView(Stage stage) {
        loadScreen("library-view.fxml", stage, "Library View");
    }
}