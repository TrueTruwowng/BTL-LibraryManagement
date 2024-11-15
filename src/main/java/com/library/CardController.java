package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import com.library.Book;

import java.util.Arrays;
import java.util.Objects;

public class CardController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    @FXML
    private HBox box;

    private String[] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

    public void setData(Book book) {
        bookTitle.setText(book.getTitle());
        authorName.setText(book.getAuthor());

        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(Arrays.toString(book.getBookImage())));
        } else {

            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }

        String color = colors[(int) (Math.random() * colors.length)];
        box.setStyle("-fx-background-color: #" + color + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 10, 0, 0, 10);");
    }
}
