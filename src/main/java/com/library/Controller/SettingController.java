package com.library.Controller;

import com.library.LibraryApplication;
import com.library.User;
import com.library.DatabaseConnection;
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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SettingController extends SceneController {
    @FXML
    private CheckBox musicControl;
    @FXML
    MusicController musicController = MusicController.getInstance();
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
    private final String imagesDirectory = "/ScreenUI/Picture/Avatar";

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
            protected List<Path> call() throws IOException, URISyntaxException {
                return Files.list(Path.of(getClass().getResource(imagesDirectory).toURI()))
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
                try {
                    updateUserPictureInDatabase(path);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ((Stage) gridPane.getScene().getWindow()).close();
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

        Scene scene = new Scene(scrollPane, 400, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Choose Profile Picture");
        stage.show();
    }

    private void updateUserPictureInDatabase(Path imagePath) throws SQLException {
        User currentUser = UserController.getCurrentUser();
        if (currentUser == null) return;

        try (InputStream inputStream = Files.newInputStream(imagePath)) {
            byte[] newImageBytes = inputStream.readAllBytes();
            currentUser.setUserPicture(newImageBytes);
            DatabaseConnection.updateUserPicture(currentUser.getUserID(), newImageBytes);

            Platform.runLater(() -> {
                refreshUserInfo();
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "User picture updated successfully.");
            });
        } catch (IOException | SQLException e) {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user picture."));
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection();
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
        EditController editController = loadScreenWithController("/com/library/EditInfo-view.fxml", "Edit Info");
        if (editController != null) {
            editController.setSettingController(this);
        }
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

    @FXML
    private void muteMusic() {
        if (musicController.isPlaying()) {
            musicController.setMute(true);
            musicController.pauseMusic();
        } else {
            musicController.setMute(false);
            musicController.resumeMusic();
        }
    }

    @FXML
    public void onDashboardBtnClick() {
        LibraryApplication.getSceneController().loadDashboardView();
    }

    @FXML
    public void onSettingsBtnClick() {
        LibraryApplication.getSceneController().loadSettingView();
    }

    @FXML
    public void onMyCollectionBtnClick() {
        LibraryApplication.getSceneController().loadMyCollectionView();
    }

    @FXML
    public void onLogOutBtnClick() {
        LibraryApplication.getSceneController().loadLoginView();
    }

    @FXML
    public void onGameBtnClick() {
        LibraryApplication.getSceneController().loadGameView();
    }
}
