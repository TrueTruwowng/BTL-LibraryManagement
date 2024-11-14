package com.library;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

public class LibraryAdminController implements Initializable {
    @FXML
    public JFXButton addBookButton;
    @FXML
    public Hyperlink deleteHyperlink;
    @FXML
    public Hyperlink saveHyperlink;
    @FXML
    private TableView<Book> tableBookView;
    @FXML
    private TableColumn<Book, CheckBox> checkBoxBookColumn;
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
    @FXML
    private CheckBox checkAllBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumn();
        initCheckAllBook();
        loadBook();
        DatabaseConnection.connectUserAccount();
    }

    public void initColumn() {
        checkBoxBookColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
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

    private void initCheckAllBook() {
        checkAllBook.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (Book book : bookObservableList) {
                book.getCheckBox().setSelected(newValue);
            }
        });
    }

    private void loadBook() {
        // Kết nối tới database
        //DatabaseConnection.connectUserAccount();

        //Lấy dữ liệu từ database
        String sqlite = "SELECT * FROM book_info";
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlite)) {;
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

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM book_info WHERE isbn = ?";
            PreparedStatement statement = con.prepareStatement(query);
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

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM book_info WHERE isbn = ?";
            PreparedStatement statement = con.prepareStatement(query);

            for (Book book : selectedBooks) {
                statement.setString(1, book.getIsbn());
                statement.addBatch();
            }

            int[] rowsDeleted = statement.executeBatch();
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

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + searchTerm + "%");
            preparedStatement.setString(2, "%" + searchTerm + "%");
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
        String urlStr = "https://www.googleapis.com/books/v1/volumes?q=" + searchTerm + "&key=" + API.getApiKey();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray items = jsonResponse.getAsJsonArray("items");

            if (items != null) {
                for (JsonElement item : items) {
                    JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");

                    String isbn = volumeInfo.has("industryIdentifiers")
                            ? volumeInfo.getAsJsonArray("industryIdentifiers").get(0).getAsJsonObject().get("identifier").getAsString()
                            : "N/A";
                    String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown";
                    String author = volumeInfo.has("authors")
                            ? volumeInfo.getAsJsonArray("authors").get(0).getAsString()
                            : "Unknown";
                    int year = volumeInfo.has("publishedDate")
                            ? Integer.parseInt(volumeInfo.get("publishedDate").getAsString().substring(0, 4))
                            : 0;
                    String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description";

                    byte[] image = null;
                    if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                        String imageUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
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
        try (Connection con = DatabaseConnection.getConnection();
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
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, book.getIsbn());
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Số lượng sách đã được cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Không được cập nhật", Alert.AlertType.ERROR);
        }
    }

    public void addBookToDatabase(Book book) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null || connection.isClosed()) {
            System.out.println("Kết nối cơ sở dữ liệu không hợp lệ. Không thể thêm sách.");
            return;
        }
        if (isBookExists(book)) {
            updateBookAvailable(book);
            return;
        } else {
            String insertQuery = "INSERT INTO book_info (isbn, title, author, year, available, description, bookImage) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                preparedStatement.setString(1, book.getIsbn());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setString(3, book.getAuthor());
                preparedStatement.setInt(4, book.getYear());
                preparedStatement.setInt(5, book.getAvailable());
                preparedStatement.setString(6, book.getDescription());
                preparedStatement.setBytes(7, book.getBookImage());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    Book newBook = new Book(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getYear(),
                                            book.getAvailable(), book.getDescription(), book.getBookImage());
                    bookObservableList.add(newBook);
                    tableBookView.setItems(bookObservableList);
                    System.out.println("Thêm thành công");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Lỗi", "Thêm thất bại", Alert.AlertType.ERROR);
            }
        }
    }


    public void searchBook(KeyEvent keyEvent) {
        String searchTerm = ((TextField) keyEvent.getSource()).getText().toLowerCase();
        ObservableList<Book> combinedResults = FXCollections.observableArrayList();

        // Kiểm tra nếu ô tìm kiếm trống, lấy tất cả sách từ database
        if (searchTerm.isEmpty()) {
            combinedResults.addAll(bookObservableList); // Lấy tất cả sách từ database
        } else {
            // Tìm sách từ database theo searchTerm
            List<Book> dbResults = findBooksInDatabase(searchTerm);
            combinedResults.addAll(dbResults);

            // Nếu không có kết quả từ database, tìm từ API
            if (combinedResults.isEmpty()) {
                List<Book> apiResults = findBooksFromAPI(searchTerm);
                combinedResults.addAll(apiResults);
            }
        }

        tableBookView.setItems(combinedResults);
    }


    public void addBook(ActionEvent actionEvent) {

    }

    @FXML
    public void saveSelectedBooks(ActionEvent actionEvent) throws SQLException {
        List<Book> selectedBooks = new ArrayList<>();
        for (Book book : tableBookView.getItems()) {
            if (book.getCheckBox().isSelected()) { // Kiểm tra nếu sách đã được chọn
                selectedBooks.add(book);
            }
        }

        // Thêm sách đã chọn vào database
        for (Book book : selectedBooks) {
            addBookToDatabase(book);
        }

        loadBook();
    }

}