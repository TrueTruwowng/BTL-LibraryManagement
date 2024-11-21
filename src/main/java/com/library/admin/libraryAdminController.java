package com.library.admin;

import com.jfoenix.controls.JFXButton;
import com.library.API;
import com.library.Book;
import com.library.Controller.sceneController;
import com.library.databaseConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.google.api.services.books.Books;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;
import com.google.api.services.books.BooksRequestInitializer;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import static com.library.Controller.sceneController.*;

public class libraryAdminController implements Initializable {
    @FXML
    public JFXButton addBookButton;
    @FXML
    public Hyperlink deleteHyperlink;
    @FXML
    public Hyperlink saveHyperlink;
    @FXML
    public Hyperlink adminSceneHyperlink;
    @FXML
    public Hyperlink logoutHyperLink;
    @FXML
    private TableView<Book> tableBookView;
    @FXML
    private TableColumn<Book, String> bookIsbnColumn;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, Integer> bookYearColumn;
    @FXML
    private TableColumn<Book, byte[]> bookImageColumn;
    @FXML
    private TableColumn<Book, String> bookDescriptionColumn;
    @FXML
    private TableColumn<Book, Integer> bookAvailableColumn;

    @FXML
    private TextField bookSearchTextField;
    @FXML
    private FontAwesomeIcon searchIcon;

    ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    ObservableList<Book> suggestedBookObservableList = FXCollections.observableArrayList();

    @FXML
    private ProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumn();
        loadBook();
    }

    public void initColumn() {
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        bookAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        bookDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        bookImageColumn.setCellValueFactory(new PropertyValueFactory<>("bookImage"));
        bookImageColumn.setCellFactory(param -> new TableCell<Book, byte[]>() {
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(item)));
                    imageView.setFitHeight(60);
                    imageView.setFitWidth(60);
                    setGraphic(imageView);
                }
            }
        });
        tableBookView.setItems(suggestedBookObservableList);
    }

    private void loadBook() {
        //Lấy dữ liệu từ database
        String sqlite = "SELECT * FROM book_info";
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlite)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            bookObservableList.clear(); // Xóa danh sách hiện tại

            while (resultSet.next()) {
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                int available = resultSet.getInt("available");
                String description = resultSet.getString("description");
                byte[] image = resultSet.getBytes("bookImage");
                if (image == null) {
                    image = new byte[0];
                }

                Book book = new Book(isbn, title, author, year, available, description, image);
                bookObservableList.add(book);
            }
            tableBookView.setItems(bookObservableList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load books.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void DeleteBook(ActionEvent actionEvent) {
        Book selectedBook = tableBookView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Error", "No book selected for deletion.", Alert.AlertType.ERROR);
            return;
        }

        String query = "DELETE FROM book_info WHERE isbn = ?";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, selectedBook.getIsbn());
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                bookObservableList.remove(selectedBook);
                showAlert("Success", "Book deleted successfully.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to delete the book.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error deleting book.", Alert.AlertType.ERROR);
        }
    }

    public void deleteselectedBooks(ActionEvent actionEvent) {
        List<Book> selectedBooks = tableBookView.getSelectionModel().getSelectedItems();
        if (selectedBooks.isEmpty()) {
            showAlert("Error", "No books selected for deletion.", Alert.AlertType.ERROR);
            return;
        }

        String query = "DELETE FROM book_info WHERE isbn = ?";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            for (Book book : selectedBooks) {
                preparedStatement.setString(1, book.getIsbn());
                preparedStatement.addBatch();
            }

            int[] rowsDeleted = preparedStatement.executeBatch();
            if (rowsDeleted.length > 0) {
                bookObservableList.removeAll(selectedBooks);
                showAlert("Success", "Books deleted successfully.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Failed to delete selected books.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error deleting selected books.", Alert.AlertType.ERROR);
        }
    }

    // Tìm sách từ database
    public List<Book> findBooksInDatabase(String searchTerm) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM book_info WHERE title LIKE ? OR author LIKE ?";

        // Nếu từ khoá trống, tìm tất cả sách (%)
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchTerm = "%";
        }

        try (Connection con = databaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            // Chèn từ khoá vào ? trong truy vấn SQL
            preparedStatement.setString(1, "%" + searchTerm + "%");
            preparedStatement.setString(2, "%" + searchTerm + "%");
            // Thực thi câu lệnh truy vấn của SQL
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String description = rs.getString("description");
                int available = rs.getInt("available");
                byte[] image = rs.getBytes("bookImage");

                Book book = new Book(isbn, title, author, year, available, description, image);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Tìm sách từ API
    public List<Book> findBooksFromAPI(String searchTerm) {
        List<Book> books = new ArrayList<>();

        try {
            Books.Builder builder = new Books.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                                                      new com.google.api.client.json.jackson2.JacksonFactory(),
                                    null);
            builder.setApplicationName("Library Admin");
            builder.setGoogleClientRequestInitializer(new BooksRequestInitializer(API.getApiKey()));
            Books apiBooks = builder.build();

            Books.Volumes.List volumesList = apiBooks.volumes().list(searchTerm);
            volumesList.setMaxResults(40L);
            Volumes volumes = volumesList.execute();

            if (volumes.getItems() != null && volumes.getTotalItems() > 0) {
                for (Volume volume : volumes.getItems()) {
                    Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();

                    // Lấy thông tin sách
                    String isbn = volumeInfo.getIndustryIdentifiers() != null
                            ? volumeInfo.getIndustryIdentifiers().get(0).getIdentifier()
                            : "Unknown";

                    String title = volumeInfo.getTitle() != null ? volumeInfo.getTitle() : "Unknown";

                    String author = (volumeInfo.getAuthors() != null && !volumeInfo.getAuthors().isEmpty())
                            ? volumeInfo.getAuthors().get(0) : "Unknown";

                    int year = Integer.parseInt(volumeInfo.getPublishedDate() != null ? volumeInfo.getPublishedDate().split("-")[0] : "Unknown");

                    String description = volumeInfo.getDescription() != null ? volumeInfo.getDescription() : "No description available";

                    byte[] image = null;
                    if (volumeInfo.getImageLinks() != null && volumeInfo.getImageLinks().getThumbnail() != null) {
                        String imageUrl = volumeInfo.getImageLinks().getThumbnail();
                        image = downloadImage(imageUrl);
                    }

                    Book book = new Book(isbn, title, author, year, 1, description, image);
                    books.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private byte[] downloadImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            BufferedImage bufferedImage = ImageIO.read(url);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra xem database đã có sách chưa
    public boolean isBookExists(Book book) {
        String query = "SELECT COUNT(*) FROM book_info WHERE isbn = ?";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, book.getIsbn());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Nếu có sách
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tăng số sách nếu đã có trong database
    public void updateBookAvailable(Book book) {
        String query = "UPDATE book_info SET available = available + 1 WHERE isbn = ?";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, book.getIsbn());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Số lượng sách đã được cập nhật.");
                tableBookView.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Không được cập nhật", Alert.AlertType.ERROR);
        }
    }

    public void searchBook(ActionEvent event) {
        String searchTerm = bookSearchTextField.getText().trim().toLowerCase();
        ObservableList<Book> combinedResults = FXCollections.observableArrayList();

        if (searchTerm.isEmpty()) {
            loadBook();
            combinedResults.addAll(bookObservableList);
        } else {
            // Tạo task để tìm sách
            Task<List<Book>> task = new Task<>() {
                @Override
                protected List<Book> call() throws Exception {
                    // Tìm sách trong database
                    List<Book> dbResults = findBooksInDatabase(searchTerm);
                    if (dbResults.isEmpty()) {
                        // Nếu không có, tìm từ API
                        return findBooksFromAPI(searchTerm);
                    }
                    return dbResults;
                }
            };

            // Khi task hoàn thành, cập nhật UI
            task.setOnSucceeded(workerStateEvent -> {
                List<Book> results = task.getValue();
                combinedResults.addAll(results);
                tableBookView.setItems(combinedResults);
                tableBookView.refresh();
            });

            // Khi task thất bại, thông báo lỗi
            task.setOnFailed(workerStateEvent -> {
                Throwable exception = task.getException();
                exception.printStackTrace();
                showAlert("Error", "Failed to search books.", Alert.AlertType.ERROR);
            });


            // Thực thi task trong background thread
            new Thread(task).start();
        }
    }

    public void addBook(ActionEvent actionEvent) throws IOException {
        // Load file fxml khác
        sceneController.loadScreen("/com/library/addbookadmin-view.fxml",stage,"ADD BOOK");
    }

    @FXML
    public void saveSelectedBook(ActionEvent actionEvent) {
        Book selectedBook = tableBookView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Error", "No book selected for deletion.", Alert.AlertType.ERROR);
            return;
        }

        if (isBookExists(selectedBook)) {
            updateBookAvailable(selectedBook);
        } else {
            String insertQuery = "INSERT INTO book_info (isbn, title, author, year, available, description, bookImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection con = databaseConnection.getConnection();
                 PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {

                insertStmt.setString(1, selectedBook.getIsbn());
                insertStmt.setString(2, selectedBook.getTitle());
                insertStmt.setString(3, selectedBook.getAuthor());
                insertStmt.setInt(4, selectedBook.getYear());
                insertStmt.setInt(5, selectedBook.getAvailable());
                insertStmt.setString(6, selectedBook.getDescription());
                insertStmt.setBytes(7, selectedBook.getBookImage());

                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Success", "Book saved successfully.", Alert.AlertType.INFORMATION);
                    tableBookView.refresh();
                } else {
                    showAlert("Error", "Error saving book.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void findBooksFromAPIAsync(String searchTerm) {
        Task<List<Book>> task = new Task<>() {
            @Override
            protected List<Book> call() throws Exception {
                // Gọi phương thức tìm sách từ API trong thread riêng biệt
                return findBooksFromAPI(searchTerm);
            }

            @Override
            protected void succeeded() {
                // Khi công việc hoàn thành, cập nhật UI
                List<Book> books = getValue();
                ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);
                tableBookView.setItems(bookObservableList);
            }

            @Override
            protected void failed() {
                // Xử lý lỗi nếu công việc thất bại
                showAlert("Error", "Failed to load books from API.", Alert.AlertType.ERROR);
            }
        };

        // Chạy task trong một background thread
        new Thread(task).start();
    }

    public void onAdminHyperLinkClicked() {
        Stage stage = (Stage) adminSceneHyperlink.getScene().getWindow();
        loadAdminScene(stage);
    }

    public void backToLogin() {
        Stage stage = (Stage) logoutHyperLink.getScene().getWindow();
        loadLoginView(stage);
    }
}