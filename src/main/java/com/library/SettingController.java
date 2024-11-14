package com.library;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.library.SceneLoader.stage;

public class SettingController {
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView smallUserImageView;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userIDLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    private final String imagesDirectory = "D:/OOP/BTL-LibraryManagement/src/main/resources/ScreenUI/Picture/Avatar";
    private ListView<Path> imageListView;

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button historyBtn;
    @FXML
    private Button gameBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button myCollectionBtn;
    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        User currentUser = UserController.getCurrentUser();

        if (currentUser != null) {
            firstNameLabel.setText(currentUser.getFirstname());
            lastNameLabel.setText(currentUser.getLastname());
            usernameLabel.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
            userIDLabel.setText(currentUser.getUserID());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhone());

            if (currentUser.getUserPicture() != null) {
                userImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
                smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
            }
        }
    }
    @FXML
    public void handleUpdateUserPicture() {
        Task<List<Path>> loadImagesTask = new Task<>() {
            @Override
            protected List<Path> call() throws IOException {
                return Files.list(Path.of(imagesDirectory))
                        .filter(Files::isRegularFile)
                        .collect(Collectors.toList());
            }
        };

        loadImagesTask.setOnSucceeded(event -> {
            List<Path> imagePaths = loadImagesTask.getValue();
            if (imagePaths.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "No Images Found", "No images found in the resource folder.");
            } else {
                ObservableList<Path> imagesList = FXCollections.observableArrayList(imagePaths);
                showImageSelectionDialog(imagesList);
            }
        });

        loadImagesTask.setOnFailed(event -> {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load images.");
            loadImagesTask.getException().printStackTrace();
        });

        // Khởi chạy task trong một luồng riêng
        new Thread(loadImagesTask).start();
    }

    private void showImageSelectionDialog(ObservableList<Path> imagesList) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4;");

        int row = 0;
        int col = 0;

        for (Path path : imagesList) {
            ImageView imageView = new ImageView("file:" + path.toString());
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Button imageButton = new Button();
            imageButton.setGraphic(imageView);
            imageButton.setOnAction(event -> {
                updateUserPictureInDatabase(path);
                ((Stage) gridPane.getScene().getWindow()).close();  // Đóng cửa sổ chọn ảnh
            });

            gridPane.add(imageButton, col, row);

            col++;  // Tăng cột sau mỗi ảnh
            if (col == 3) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Tạo một Scene và Stage để hiển thị dialog
        Scene scene = new Scene(scrollPane, 500, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Choose Profile Picture");
        stage.show();
    }

    private void updateUserPictureInDatabase(Path imagePath) {
        User currentUser = UserController.getCurrentUser();
        if (currentUser == null) return;

        try (InputStream inputStream = Files.newInputStream(imagePath)) {
            byte[] newImageBytes = inputStream.readAllBytes();
            currentUser.setUserPicture(newImageBytes);
            DatabaseConnection.updateUserPicture(currentUser.getUserID(), newImageBytes);

            Platform.runLater(() -> {
                Image newImage = new Image(new ByteArrayInputStream(newImageBytes));
                userImageView.setImage(newImage);
                smallUserImageView.setImage(newImage); // Cập nhật cả ảnh nhỏ
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "User picture updated successfully.");
            });
        } catch (IOException e) {
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user picture.");
            });
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleEditInformationButton() {
        Stage editStage = new Stage();
        EditController editController = (EditController) SceneLoader.loadScreenWithController("EditInfo-view.fxml", editStage, "EditInfo.fxml");
        assert editController != null;
        editController.setSettingController(this);  // Truyền SettingController vào EditController
        editStage.show();
    }

    public void refreshUserInfo() {
        User currentUser = UserController.getCurrentUser();
        if (currentUser != null) {
            firstNameLabel.setText(currentUser.getFirstname());
            lastNameLabel.setText(currentUser.getLastname());
            usernameLabel.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
            userIDLabel.setText(currentUser.getUserID());
            emailLabel.setText(currentUser.getEmail());
            phoneLabel.setText(currentUser.getPhone());

            if (currentUser.getUserPicture() != null) {
                userImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
                smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
            }
        }
    }
    public void onDashboardBtnClick() {
        SceneLoader.handleDashboardButton(stage);
    }

    public void onHistoryBtnClick() {
        SceneLoader.handleHistoryButton(stage);
    }
    public void onGameBtnClick() {
        SceneLoader.handleGameButton(stage);
    }
    public void onSettingsBtnClick() {
        SceneLoader.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        SceneLoader.handleMyCollectionButton(stage);
    }

    public void onLogOutBtnClk() {
        SceneLoader.handleLogoutButton(stage);
    }
}