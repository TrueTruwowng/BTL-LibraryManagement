package com.library;

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

public class cardController {
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    @FXML
    private HBox box;

    private String[] colors = {"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

    private com.library.book book;

    public void setData(com.library.book book) {
        this.book = book;
        authorName.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());

        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(new ByteArrayInputStream(book.getBookImage())));
        } else {
            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }

        String color = colors[(int) (Math.random() * colors.length)];
        box.setStyle("-fx-background-color: #" + color + ";" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 10, 0, 0, 10);");

        // Thêm sự kiện click vào HBox
        box.setOnMouseClicked(this::onCardClick);
    }

    private void onCardClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookInfo-view.fxml"));
            Scene scene = new Scene(loader.load());
            bookInfo bookInfoController = loader.getController();
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
