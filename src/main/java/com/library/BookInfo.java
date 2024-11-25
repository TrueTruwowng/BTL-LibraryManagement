package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.library.Controller.UserController.getCurrentUser;

public class BookInfo {

    @FXML
    private Label bookTitle;
    @FXML
    private Label bookAuthor;
    @FXML
    private Label bookYear;
    @FXML
    private Label bookISBN;
    @FXML
    private Label bookAvailable;
    @FXML
    private TextArea bookDescription;
    @FXML
    private ImageView bookImage;
    @FXML
    private Button borrowButton;
    @FXML
    private Button returnButton;

    private Book book;
    private void setBook(Book book) {
        this.book = book;
    }

    public void setBookData(Book book) {
        this.book = book;

        if (book != null) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookYear.setText(Integer.toString(book.getYear()));
            bookISBN.setText(book.getIsbn());
            bookAvailable.setText(Integer.toString(book.getAvailable()));
            bookDescription.setText(book.getDescription());

            byte[] imageBytes = book.getBookImage();
            if (imageBytes == null || imageBytes.length == 0) {
                bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                bookImage.setImage(new Image(byteArrayInputStream));
            }

            updateButtonStates();
        } else {
            System.out.println("Book data is null");
        }
    }

    @FXML
    private void returnBook() {
        String isbn = bookISBN.getText();
        String accountId = getCurrentUser().getUserID();

        if (isbn == null || isbn.isEmpty() || accountId == null || accountId.isEmpty()) {
            System.out.println("Please enter valid information");
            return;
        }

        try {
            LocalDate returnDate = LocalDate.now(); // Ngày trả là ngày hiện tại
            DatabaseConnection.updateReturnDateByISBN(isbn, accountId, returnDate); // Gọi hàm cập nhật trong DatabaseConnection

            DatabaseConnection.updateAvailableBooks(isbn, 1); // Tăng số lượng sách có sẵn (1 cuốn)

            showAlert("Return successfully", Alert.AlertType.INFORMATION);

            // Cập nhật lại trạng thái sách trong giao diện
            book.setAvailable(book.getAvailable() + 1);
            setBookData(book);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            showAlert("Return failed", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void borrowBookByTitle() {
        try {
            String title = bookTitle.getText();
            String accountId = getCurrentUser().getUserID();

            if (title == null || title.isEmpty() || accountId == null || accountId.isEmpty()) {
                System.out.println("Please enter valid information");
                return;
            }

            DatabaseConnection.borrowBookByTitle(title, accountId);

            showAlert("Borrow book successfully", Alert.AlertType.INFORMATION);

            // Cập nhật lại trạng thái sách
            book.setAvailable(book.getAvailable() - 1);
            setBookData(book);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            showAlert("Borrow failed", Alert.AlertType.ERROR);
        }
    }

    private void updateButtonStates() {
        try {
            String accountId = getCurrentUser().getUserID();
            String isbn = book.getIsbn();

            if (accountId == null || isbn == null || isbn.isEmpty()) {
                System.out.println("Invalid information");
                return;
            }

            boolean isBorrowed = DatabaseConnection.isBookBorrowedByUser(isbn, accountId);

            boolean isAvailable = book.getAvailable() > 0;

            borrowButton.setDisable(isBorrowed || !isAvailable);
            returnButton.setDisable(!isBorrowed);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
