package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Arrays;

public class SearchController {

    @FXML
    private HBox HboxSearch;

    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    public void setData(Book book) {
        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(Arrays.toString(book.getBookImage())));
        } else {

            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }
        bookTitle.setText(book.getTitle());
        authorName.setText(book.getAuthor());

    }

}
