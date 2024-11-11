package com.library;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.smartcardio.Card;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private HBox cardLayout;


    @FXML
    private GridPane bookContainer;

    private List<Book> recentlyAdded;
    private List<Book> recommended;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recentlyAdded = new ArrayList<>(recentlyAdded());
        recommended = new ArrayList<>(books());
        int column = 0;
        int row = 1;

        try {

        for (Book value : recentlyAdded) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("card-view.fxml"));
            HBox cardBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(value);
            cardLayout.getChildren().add(cardBox);
        }

        for (Book book : recommended) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Book-view.fxml"));
            VBox bookBox = fxmlLoader.load();
            BookController bookController = fxmlLoader.getController();
            bookController.setData(book);

            if(column == 6) {
                column = 0;
                row++;
            }

            bookContainer.add(bookBox, column++, row);
            GridPane.setMargin(bookBox, new Insets(10));


        }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Book> recentlyAdded() {
        List<Book> ls = new ArrayList<>();
        Book book = new Book();
        book.setTitle("Đắc Nhân Tâm");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/dacnhanhtam.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        return ls;


    };

    private List<Book> books() {
        List<Book> ls = new ArrayList<>();
        Book book = new Book();
        book.setTitle("Đắc Nhân Tâm");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/dacnhanhtam.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("CHA GIÀU CHA NGHÈO");
        book.setAuthor("Tác giả");
        book.setImageSrc("/ScreenUI/Picture/chagiauchangheo.jpg");
        ls.add(book);

        book = new Book();
        book.setTitle("NNKK");
        book.setAuthor("Victorrigo");
        book.setImageSrc("/ScreenUI/Picture/nhungnguoikhonkho.jpg");
        ls.add(book);

        return ls;
    }


}
