package com.library;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {

    // To keep track of the currently opened stage
    public static Stage stage = null;

    public static void loadScreen(String fxmlFile, Stage stage, String title) {
        try {
            // Nếu stage không được truyền vào, sử dụng currentStage
            if (stage == null) {
                if (SceneLoader.stage == null) {
                    SceneLoader.stage = new Stage(); // Tạo mới Stage nếu chưa có
                }
                stage = SceneLoader.stage;
            }

            // Load FXML và tạo Scene mới
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlFile));
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

    public static void loadHistoryView(Stage stage) {
        loadScreen("/com/library/history-view.fxml", stage, "History");
    }

    public static void loadGameView(Stage stage) {
        loadScreen("/com/library/game-view.fxml", stage, "Game");
    }

    public static void loadSettingView(Stage stage) {
        loadScreen("/com/library/Setting-view.fxml", stage, "Settings");
    }

    public static void loadMyCollectionView(Stage stage) {
        loadScreen("/com/library/my-collection-view.fxml", stage, "My Collection");
    }
    public static <T> T loadScreenWithController(String fxmlFile, Stage stage, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlFile));
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
        SceneLoader.loadSettingView(SceneLoader.stage);
    }

    public static void handleLogoutButton(Stage stage) {
        SceneLoader.loadLoginView(SceneLoader.stage);
    }

    public static void handleMyCollectionButton(Stage stage) {
        SceneLoader.loadMyCollectionView(SceneLoader.stage);
    }
    public static void handleDashboardButton(Stage stage) {
        SceneLoader.loadDashboardView(SceneLoader.stage);
    }

}