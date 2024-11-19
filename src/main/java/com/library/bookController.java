package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class bookController {

    @FXML
    private VBox bookContainer;
    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;

    private Book book;

    public void setData(Book book) {
        this.book = book;

        // Nếu có hình ảnh sách, hiển thị, nếu không thì hiển thị hình ảnh mặc định
        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(new ByteArrayInputStream(book.getBookImage())));
        } else {
            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }

        bookTitle.setText(book.getTitle());
        authorName.setText(book.getAuthor());

        // Thêm sự kiện click vào bookImage
        bookContainer.setOnMouseClicked(this::onCardClick);
    }

    // Phương thức xử lý sự kiện click
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
