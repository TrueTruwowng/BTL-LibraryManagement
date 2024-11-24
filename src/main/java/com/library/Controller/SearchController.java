package com.library.Controller;

import com.library.Book;
import com.library.BookInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SearchController {

    @FXML
    private HBox HboxSearch;

    @FXML
    private Label authorName;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label bookTitle;
    private Book book;

    public void setData(Book book) {
        if (book.getBookImage() != null) {
            bookImage.setImage(new Image(new ByteArrayInputStream(book.getBookImage())));
        } else {

            bookImage.setImage(new Image(getClass().getResourceAsStream("/ScreenUI/Picture/NULLimage.jpg")));
        }
        bookTitle.setText(book.getTitle());
        authorName.setText(book.getAuthor());
        HboxSearch.setOnMouseClicked(this::onBookClick);


    }

    private void onBookClick(MouseEvent event) {
        try {
            // Tải FXML từ đường dẫn tuyệt đối
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/BookInfo-view.fxml"));
            Scene scene = new Scene(loader.load());

            // Lấy controller và truyền dữ liệu cho nó
            BookInfo bookInfoController = loader.getController();
            bookInfoController.setBookData(this.book);  // Truyền đối tượng book vào BookInfo

            // Tạo và hiển thị cửa sổ mới (Stage)
            Stage newStage = new Stage();
            newStage.setTitle("Book Info");
            newStage.setScene(scene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
