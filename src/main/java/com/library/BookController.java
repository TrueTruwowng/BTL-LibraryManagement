package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.library.Book;

public class BookController {

    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    public void setData(Book book) {
        if (book.getImageSrc() != null) {
            bookImage.setImage(new Image(book.getImageSrc()));
        } else {

            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }
        bookTitle.setText(book.getTitle());
        authorName.setText(book.getAuthor());

    }


}
