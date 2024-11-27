package com.library.admin;

import com.jfoenix.controls.JFXButton;
import com.library.Book;
import com.library.Controller.SceneController;
import com.library.DatabaseConnection;
import com.library.LibraryApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddBookAdminController extends SceneController implements Initializable {
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
        LibraryApplication.getSceneController().loadAdminLibraryScene();
    }

    public void cancelSaveBookToLibrary(ActionEvent actionEvent) {
        clearFields();
    }

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

    public void updateBookAvailable(Book book) {
        String query = "UPDATE book_info SET available = available + ? WHERE isbn = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, book.getAvailable()); // Số lượng cần cộng
            preparedStatement.setString(2, book.getIsbn());   // ISBN của sách
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book quantity has been updated");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Update failed", Alert.AlertType.ERROR);
        }

    }

    public void saveBookToLibrary(ActionEvent actionEvent) {
        String bookIsbn = bookIsbnTextField.getText();
        String bookTitle = bookTitleTextField.getText();
        String bookAuthor = bookAuthorTextField.getText();
        String bookYear = bookYearTextField.getText();
        String bookQuantity = bookQuantityTextField.getText();
        String bookDescription = bookDescriptionTextField.getText();

        // Kiểm tra các trường không được để trống
        if (bookIsbn.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookYear.isEmpty() || bookQuantity.isEmpty()) {
            showAlert("Error", "Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra bookYear có phải là số hay không
        if (!bookYear.matches("\\d{4}")) { // Chỉ chấp nhận năm với 4 chữ số
            showAlert("Error", "Year must be a 4-digit number.", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra bookQuantity có phải là số hay không
        if (!bookQuantity.matches("\\d+")) { // Chỉ chấp nhận số nguyên dương
            showAlert("Error", "Quantity must be a positive number.", Alert.AlertType.ERROR);
            return;
        }

        String year = bookYear;
        int quantity = Integer.parseInt(bookQuantity);

        Book book = new Book(bookIsbn, bookTitle, bookAuthor, year, quantity, bookDescription, null);

        if (isBookExists(book)) {
            updateBookAvailable(book);
        } else {
            try (Connection con = DatabaseConnection.getConnection()) {
                String insertQuery = "INSERT INTO book_info (isbn, title, author, year, available, description, bookImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, book.getIsbn());
                insertStmt.setString(2, book.getTitle());
                insertStmt.setString(3, book.getAuthor());
                insertStmt.setString(4, book.getYear());
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
