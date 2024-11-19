package com.library;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addBookAdminController implements Initializable {
    @FXML
    private TextField bookIsbnTextField;
    @FXML
    private TextField bookTitleTextField;
    @FXML
    private TextField bookAuthorTextField;
    @FXML
    private TextField bookYearTextField;
    @FXML
    private TextField bookDescriptionTextField;
    @FXML
    private TextField bookQuantityTextField;

    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    //public boolean isEdit = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void backToLibrary(ActionEvent actionEvent) throws IOException {
        // Load file fxml khác
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("libraryadmin-view.fxml"));
        Parent root = fxmlLoader.load();

        // Tạo cửa sổ mới
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();

        // Đóng cửa sổ hiện tại khi mở cửa sổ mới
        Stage curStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        curStage.close();
    }

    public void cancelSaveBookToLibrary(ActionEvent actionEvent) {
        clearFields();
    }

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

    public void updateBookAvailable(Book book) {
        String query = "UPDATE book_info SET available = available + ? WHERE isbn = ?";
        try (Connection con = databaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, book.getAvailable()); // Số lượng cần cộng
            preparedStatement.setString(2, book.getIsbn());   // ISBN của sách
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Số lượng sách đã được cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Không được cập nhật", Alert.AlertType.ERROR);
        }

    }

    public void saveBookToLibrary(ActionEvent actionEvent) {
        String bookIsbn = bookIsbnTextField.getText();
        String bookTitle = bookTitleTextField.getText();
        String bookAuthor = bookAuthorTextField.getText();
        String bookYear = bookYearTextField.getText();
        String bookQuantity = bookQuantityTextField.getText();
        String bookDescription = bookDescriptionTextField.getText();

        if (bookIsbn.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookYear.isEmpty() || bookQuantity.isEmpty()) {
            showAlert("Lỗi", "Hãy điền đầy đủ", Alert.AlertType.ERROR);
            return;
        }

        int year = Integer.parseInt(bookYear);
        int quantity = Integer.parseInt(bookQuantity);
        Book book = new Book(bookIsbn, bookTitle, bookAuthor, year, quantity, bookDescription, null);

        if (isBookExists(book)) {
            updateBookAvailable(book);
        } else {
            try (Connection con = databaseConnection.getConnection()) {
                String insertQuery = "INSERT INTO book_info (isbn, title, author, year, available, description, bookImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, book.getIsbn());
                insertStmt.setString(2, book.getTitle());
                insertStmt.setString(3, book.getAuthor());
                insertStmt.setInt(4, book.getYear());
                insertStmt.setInt(5, book.getAvailable());
                insertStmt.setString(6, book.getDescription());
                insertStmt.setBytes(7, book.getBookImage());

                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert("Success", "Book saved successfully.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Error saving book.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        bookIsbnTextField.clear();
        bookTitleTextField.clear();
        bookAuthorTextField.clear();
        bookYearTextField.clear();
        bookDescriptionTextField.clear();
        bookQuantityTextField.clear();
    }
}
