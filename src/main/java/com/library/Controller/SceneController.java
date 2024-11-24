package com.library.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneController {
    public static Stage stage = null;
    public static void setPrimaryStage(Stage stage) {
        if (SceneController.stage == null) {
            SceneController.stage = stage;
            SceneController.stage.getIcons().add(
                    new Image(SceneController.class.getResourceAsStream("/ScreenUI/Picture/Avatar.png")));
        }
    }

    public static void loadScreen(String fxmlFile, Stage stage, String title) {
        try {
            if (stage == null) {
                if (SceneController.stage == null) {
                    SceneController.stage = new Stage();
                }
                stage = SceneController.stage;
            }

            // Load FXML và tạo Scene mới
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlFile));
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
        loadScreen("/com/library/login-view.fxml", SceneController.stage, "Login");
    }

    public static void loadDashboardView(Stage stage) {
        loadScreen("/com/library/Dashboard-view.fxml", SceneController.stage, "Dashboard");
    }

    public static void loadSettingView(Stage stage) {
        loadScreen("/com/library/Setting-view.fxml", SceneController.stage, "Settings");
    }

    public static void loadMyCollectionView(Stage stage) {
        loadScreen("/com/library/MyCollection-view.fxml", SceneController.stage, "My Collection");
    }
    public static void loadGameView(Stage stage) {
        loadScreen("/com/library/gameAll-view.fxml", SceneController.stage, "Game");
    }

    public static <T> T loadScreenWithController(String fxmlFile, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlFile));
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
        SceneController.loadSettingView(SceneController.stage);
    }
    public static void hadnleGameButton(Stage stage) {
        SceneController.loadGameView(SceneController.stage);
    }

    public static void handleLogoutButton(Stage stage) {
        SceneController.loadLoginView(SceneController.stage);
    }

    public static void handleMyCollectionButton(Stage stage) {
        SceneController.loadMyCollectionView(SceneController.stage);
    }
    public static void handleDashboardButton(Stage stage) {
        SceneController.loadDashboardView(SceneController.stage);
    }

    public static void loadAdminScene(Stage stage) {
        loadScreen("/com/library/admin-view.fxml", stage, "Admin");
    }

    public static void loadAdminLibraryScene(Stage stage) {
        loadScreen("/com/library/libraryadmin-view.fxml", stage, "Admin Library");
    }

    public static void loadGamePlayScene(Stage stage) {
        loadScreen("/com/library/gameplay-view.fxml", stage, "Game Play");
    }
}