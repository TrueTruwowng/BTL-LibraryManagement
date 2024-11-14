package com.library;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryController {
    @FXML
    public ImageView userImageView;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userIDLabel;
    @FXML
    private TextField newPasswordField;
    private final String imagesDirectory = "D:/OOP/BTL-LibraryManagement/src/main/resources/ScreenUI/Picture/Avatar";
    private ListView<Path> imageListView;

    @FXML
    public void initialize() {
        User currentUser = UserController.getCurrentUser();

        if (currentUser != null) {
            firstNameLabel.setText(currentUser.getFirstname());
            lastNameLabel.setText(currentUser.getLastname());
            usernameLabel.setText(currentUser.getUsername());
            userIDLabel.setText(currentUser.getUserID());

            if (currentUser.getUserPicture() != null) {
                userImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
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
        GridPane gridPane = new GridPane();  // Sử dụng GridPane thay vì HBox
        gridPane.setHgap(10);  // Khoảng cách ngang giữa các ảnh
        gridPane.setVgap(10);  // Khoảng cách dọc giữa các ảnh
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
                userImageView.setImage(new Image(new ByteArrayInputStream(newImageBytes)));
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


}