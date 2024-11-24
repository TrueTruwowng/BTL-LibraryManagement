package com.library.Controller;

import com.library.Book;
import com.library.BookInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CardController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    @FXML
    private HBox box;

    private Book book;

    public void setData(Book book) {
        this.book = book;
        authorName.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());

        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(new ByteArrayInputStream(book.getBookImage())));
        } else {
            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }

        box.setStyle("-fx-background-color: #395f18" + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 10, 0, 0, 10);");

        // Thêm sự kiện click vào HBox
        box.setOnMouseClicked(this::onCardClick);
    }

    private void onCardClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/BookInfo-view.fxml"));
            Scene scene = new Scene(loader.load());
            BookInfo bookInfoController = loader.getController();
            bookInfoController.setBookData(this.book);  // Truyền đối tượng book vào BookInfo

            // Tạo một cửa sổ mới (Stage)
            Stage newStage = new Stage();
            newStage.setTitle("Book Info");
            newStage.setScene(scene);
            newStage.show();  // Hiển thị cửa sổ mới

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
