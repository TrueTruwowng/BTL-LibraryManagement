package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.BookM;

public class Card {
    @FXML
    private Label authorName;

    @FXML
    private Label bookName;

    @FXML
    private HBox box;

    @FXML
    private ImageView bookImage;

    private String [] colors = {"B9E5FF", "BDB2FE", "FB9AAB", "FF5056"};

    public void setData(BookM book) {
        Image image = new Image(getClass().getResourceAsStream(book.getImageSrc()));
        bookImage.setImage(image);
        bookName.setText(book.getName());
        authorName.setText(book.getAuthor());
        box.getStyle();
    }

}
