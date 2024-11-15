package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

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

    public void initialize() throws SQLException {
        DatabaseConnection.connectUserAccount();
        User user = UserController.currentUser;
        Book book = DatabaseConnection.getBookByISBN("9780521662208");
        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookYear.setText(Integer.toString(book.getYear()));
        bookISBN.setText(book.getISBN());
        bookDescription.setText(book.getDescription());
        bookAvailable.setText(Integer.toString(book.getAvailable()));
        String imagePath = book.getImageSrc();
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = "/ScreenUI/Picture/NULLimage.jpg";
        }
        bookImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
    }
}
