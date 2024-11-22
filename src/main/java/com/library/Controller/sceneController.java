package com.library.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class sceneController {
    public static Stage stage = null;
    public static void setPrimaryStage(Stage stage) {
        if (sceneController.stage == null) {
            sceneController.stage = stage;
            sceneController.stage.getIcons().add(
                    new Image(sceneController.class.getResourceAsStream("/ScreenUI/Picture/Avatar.png")));
        }
    }

    public static void loadScreen(String fxmlFile, Stage stage, String title) {
        try {
            if (stage == null) {
                if (sceneController.stage == null) {
                    sceneController.stage = new Stage();
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
        loadScreen("/com/library/login-view.fxml", sceneController.stage, "Login");
    }

    public static void loadDashboardView(Stage stage) {
        loadScreen("/com/library/Dashboard-view.fxml", sceneController.stage, "Dashboard");
    }

    public static void loadSettingView(Stage stage) {
        loadScreen("/com/library/Setting-view.fxml", sceneController.stage, "Settings");
    }

    public static void loadMyCollectionView(Stage stage) {
        loadScreen("/com/library/MyCollection-view.fxml", sceneController.stage, "My Collection");
    }

    public static <T> T loadScreenWithController(String fxmlFile, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(sceneController.class.getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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