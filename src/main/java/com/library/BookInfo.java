package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.Arrays;

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

    private Book book;
    public void setBook(Book book) {
        this.book = book;
    }

    public void setBookData(Book book) {
        this.book = book;

        if (book != null) {
            // Cập nhật thông tin sách trên UI
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookYear.setText(Integer.toString(book.getYear()));
            bookISBN.setText(book.getISBN());
            bookAvailable.setText(Integer.toString(book.getAvailable()));
            bookDescription.setText(book.getDescription());

            // Kiểm tra hình ảnh và hiển thị
            byte[] imageBytes = book.getBookImage();
            if (imageBytes == null || imageBytes.length == 0) {
                // Nếu không có hình ảnh, sử dụng hình ảnh mặc định
                bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
            } else {
                // Nếu có hình ảnh, chuyển mảng byte thành Image
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
                bookImage.setImage(new Image(byteArrayInputStream));
            }
        } else {
            System.out.println("Book data is null");
        }
    }
}
