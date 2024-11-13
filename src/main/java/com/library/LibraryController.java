package com.library;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

//import com.library.Book;

public class LibraryController {
    @FXML
    public ImageView userImageView;
    @FXML
    private TableColumn<Book, String> colISBN;

    @FXML
    private TableColumn<Book, String> colTitle;

    @FXML
    private Label lastNameLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField newPasswordField;
    private final String imagesDirectory = "D:/OOP/BTL-LibraryManagement/src/main/resources/ScreenUI/Picture/Avatar";
    private ListView<Path> imageListView;

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Dữ liệu mẫu
        tableBooks.getItems().add(new Book("1000","Clean Code", "Robert C. Martin", "2008"));
        tableBooks.getItems().add(new Book("1001","Head First Java", "Kathy Sierra", "2005"));
    }

    @FXML
    public void handleAddBook() {
        String isbn = txtISBN.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String year = txtYear.getText();

        if (title.isEmpty() || author.isEmpty() || year.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields");
            return;
        }

        Book newBook = new Book(isbn, title, author, year);
        tableBooks.getItems().add(newBook);

        clearFields();
    }

    @FXML
    public void handleDeleteBook() {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            tableBooks.getItems().remove(selectedBook);
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a book to delete");
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
