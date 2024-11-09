package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.BookM;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Runnable {

    @FXML
    private HBox Layout;

    @FXML
    private GridPane bookContainer;

    private List<BookM> added;

    public void initialize(URL location, ResourceBundle resources)  {
        added = new ArrayList<>(added());
        try {
            for (int i = 0; i < added.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Card.fxml"));
                HBox cardBox = fxmlLoader.load();
                Card card = fxmlLoader.getController();
                card.setData(added.get(i));
                Layout.getChildren().add(cardBox);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<BookM> added() {
        List<BookM> books = new ArrayList<>();
        BookM book = new BookM();
        book.setName("ĐẮC NHÂN TÂM");
        book.setAuthor("Dale Carnegie");
        book.setImageSrc("/Image/dacnhanhtam.jpg");
        books.add(book);
        return books;
    }

    @Override
    public void run() {

    }
}
