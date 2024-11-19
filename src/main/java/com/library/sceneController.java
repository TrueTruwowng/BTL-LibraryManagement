package com.library;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class sceneController {

    // To keep track of the currently opened stage
    public static Stage stage = null;

    public static void loadScreen(String fxmlFile, Stage stage, String title) {
        try {
            if (stage == null) {
                if (sceneController.stage == null) {
                    sceneController.stage = new Stage(); // Tạo mới Stage nếu chưa có
                }
                stage = sceneController.stage;
            }

            // Load FXML và tạo Scene mới
            FXMLLoader loader = new FXMLLoader(sceneController.class.getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods for loading different views
    public static void loadLoginView(Stage stage) {
        loadScreen("/com/library/login-view.fxml", stage, "Login");
    }

    public static void loadDashboardView(Stage stage) {
        loadScreen("/com/library/Dashboard-view.fxml", stage, "Dashboard");
    }


    public static void loadSettingView(Stage stage) {
        loadScreen("/com/library/Setting-view.fxml", stage, "Settings");
    }

    public static void loadMyCollectionView(Stage stage) {
        loadScreen("/com/library/MyCollection-view.fxml", stage, "My Collection");
    }
    public static <T> T loadScreenWithController(String fxmlFile, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(sceneController.class.getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
            return loader.getController(); // Trả về controller sau khi load thành công
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    public static void handleSettingbutton(Stage stage) {
        sceneController.loadSettingView(sceneController.stage);
    }

    public static void handleLogoutButton(Stage stage) {
        sceneController.loadLoginView(sceneController.stage);
    }

    public static void handleMyCollectionButton(Stage stage) {
        sceneController.loadMyCollectionView(sceneController.stage);
    }
    public static void handleDashboardButton(Stage stage) {
        sceneController.loadDashboardView(sceneController.stage);
    }

    public static void loadAdminScene(Stage stage) {
        loadScreen("/com/library/admin-view.fxml", stage, "Admin");
    }

    public static void loadAdminLibraryScene(Stage stage) {
        loadScreen("/com/library/libraryadmin-view.fxml", stage, "Admin Library");
    }
}