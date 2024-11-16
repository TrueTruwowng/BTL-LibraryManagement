package com.library;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


import java.awt.*;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    @FXML
    private AnchorPane searchPane;
    @FXML
    private TextField searchTextField; // Trường văn bản để nhập từ khóa tìm kiếm
    @FXML
    private Button searchButton;     // Nút kích hoạt tìm kiếm

    @FXML
    private HBox cardLayout;

    @FXML
    private VBox searchLayout;

    @FXML
    private GridPane bookContainer;

    @FXML
    private boolean isSearchPaneVisible = false;

    private List<Book> recentlyAdded;
    private List<Book> recommended;
    private List<Book> searched;

    @FXML
    private void searchBooks(ActionEvent event) {
        if (!isSearchPaneVisible) {
            searchPane.setVisible(true);
            searchPane.setManaged(true);
            isSearchPaneVisible = true;
        } else {
            searchPane.setVisible(false);
            searchPane.setManaged(false);
            isSearchPaneVisible = false;
        }
        String searchQuery = searchTextField.getText();
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            searchLayout.getChildren().clear();
            return;
        }

        List<Book> searchResults;
        try {
            searchResults = DatabaseConnection.searchBooks(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        displaySearchResults(searchResults);
    }

    private void displaySearchResults(List<Book> books) {
        searchLayout.getChildren().clear(); // Xóa kết quả trước đó
        if (books.isEmpty()) {
            Label noResultsLabel = new Label("Không tìm thấy kết quả.");
            searchLayout.getChildren().add(noResultsLabel);
            return;
        }

        try {
            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("search-view.fxml"));
                HBox searchBox = fxmlLoader.load(); // Đảm bảo rằng đây là HBox
                SearchController searchController = fxmlLoader.getController();
                searchController.setData(book);
                searchLayout.getChildren().add(searchBox); // Thêm searchBox vào searchLayout
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Kết nối đến cơ sở dữ liệu
        DatabaseConnection.connectUserAccount(); // Nếu cần thiết

        List<Book> allBooks = null;
        try {
            allBooks = DatabaseConnection.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Lấy tất cả sách từ cơ sở dữ liệu
        recentlyAdded = new ArrayList<>(allBooks); // Giả sử bạn muốn hiển thị tất cả sách ở đây
        recommended = new ArrayList<>(allBooks); // Tương tự cho sách được đề xuất

        int column = 0;
        int row = 1;


        try {
            // Hiển thị sách mới thêm
            for (Book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card-view.fxml"));
                HBox cardBox = fxmlLoader.load();
                CardController cardController = fxmlLoader.getController();
                cardController.setData(value);
                cardLayout.getChildren().add(cardBox);
            }

            // Hiển thị sách được đề xuất
            for (Book book : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Book-view.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (column == 6) {
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

}
