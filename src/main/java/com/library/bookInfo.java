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

import static com.library.userController.currentUser;

public class bookInfo {

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

    private com.library.book book;
    private void setBook(com.library.book book) {
        this.book = book;
    }

    public void setBookData(com.library.book book) {
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
        String accountId = currentUser.getUserID();

        if (isbn == null || isbn.isEmpty() || accountId == null || accountId.isEmpty()) {
            System.out.println("Vui lòng nhập thông tin hợp lệ!");
            return;
        }

        try {
            LocalDate returnDate = LocalDate.now(); // Ngày trả là ngày hiện tại
            databaseConnection.updateReturnDateByISBN(isbn, accountId, returnDate); // Gọi hàm cập nhật trong DatabaseConnection

            databaseConnection.updateAvailableBooks(isbn, 1); // Tăng số lượng sách có sẵn (1 cuốn)

            showAlert("Trả sách thành công!", Alert.AlertType.INFORMATION);

            // Cập nhật lại trạng thái sách trong giao diện
            book.setAvailable(book.getAvailable() + 1);
            setBookData(book);
        } catch (SQLException e) {
            System.out.println("Lỗi khi trả sách: " + e.getMessage());
            showAlert("Có lỗi xảy ra khi trả sách!", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void borrowBookByTitle() {
        try {
            String title = bookTitle.getText();
            String accountId = currentUser.getUserID();

            if (title == null || title.isEmpty() || accountId == null || accountId.isEmpty()) {
                System.out.println("Vui lòng nhập tiêu đề hợp lệ!");
                return;
            }

            databaseConnection.borrowBookByTitle(title, accountId);

            showAlert("Mượn sách thành công!", Alert.AlertType.INFORMATION);

            // Cập nhật lại trạng thái sách
            book.setAvailable(book.getAvailable() - 1);
            setBookData(book);
        } catch (SQLException e) {
            System.out.println("Lỗi khi mượn sách theo Title: " + e.getMessage());
            showAlert("Có lỗi xảy ra khi mượn sách!", Alert.AlertType.ERROR);
        }
    }

    private void updateButtonStates() {
        try {
            String accountId = currentUser.getUserID();
            String isbn = book.getIsbn();

            if (accountId == null || isbn == null || isbn.isEmpty()) {
                System.out.println("Dữ liệu không hợp lệ!");
                return;
            }

            boolean isBorrowed = databaseConnection.isBookBorrowedByUser(isbn, accountId);

            boolean isAvailable = book.getAvailable() > 0;

            borrowButton.setDisable(isBorrowed || !isAvailable);
            returnButton.setDisable(!isBorrowed);
        } catch (SQLException e) {
            System.out.println("Lỗi khi kiểm tra trạng thái sách: " + e.getMessage());
        }
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
