package com.library.Controller;

import com.library.LibraryApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    protected Stage stage;

    public void setPrimaryStage(Stage stage) {
        if (this.stage == null) { // Chỉ gán nếu chưa được thiết lập
            this.stage = stage;
        }
    }
    public void loadScreen(String fxmlFile, String title) {
        if (stage == null) {
            throw new IllegalStateException("Stage is not initialized. Call setPrimaryStage() first.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Các phương thức điều hướng cụ thể
    public void loadLoginView() {
        loadScreen("/com/library/login-view.fxml", "Login");
    }

    public void loadDashboardView() {
        loadScreen("/com/library/Dashboard-view.fxml", "Dashboard");
    }

    public void loadSettingView() {
        loadScreen("/com/library/Setting-view.fxml", "Settings");
    }

    public void loadMyCollectionView() {
        loadScreen("/com/library/MyCollection-view.fxml", "My Collection");
    }

    public void loadGameView() {
        loadScreen("/com/library/gameAll-view.fxml", "Game");
    }

    public void loadGamePlayScene() {
        loadScreen("/com/library/gameplay-view.fxml", "Game Play");
    }

    public void loadAdminScene() {
        loadScreen("/com/library/admin-view.fxml", "Admin");
    }

    public void loadAdminLibraryScene() {
        loadScreen("/com/library/libraryadmin-view.fxml", "Admin Library");
    }

    public <T> T loadScreenWithController(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Tạo một Stage mới cho cửa sổ
            Stage newStage = new Stage();
            newStage.setTitle(title);
            newStage.setScene(new Scene(root));
            newStage.show();

            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
