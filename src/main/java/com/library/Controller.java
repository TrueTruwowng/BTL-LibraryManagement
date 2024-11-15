package com.library;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;


import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    @FXML
    private javafx.scene.control.TextField searchTextField; // Trường văn bản để nhập từ khóa tìm kiếm
    @FXML
    private javafx.scene.control.Button searchButton;     // Nút kích hoạt tìm kiếm

    @FXML
    private HBox cardLayout;

    @FXML
    private GridPane bookContainer;

    private List<Book> recentlyAdded;
    private List<Book> recommended;

    @FXML
    private void searchBooks() throws SQLException {

        String searchQuery = searchTextField.getText();
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            // Xử lý trường hợp tìm kiếm trống (ví dụ: hiển thị tất cả sách)
            displayBooks(DatabaseConnection.getBooks()); // Hoặc phương thức tương tự
            return;
        }

        List<Book> searchResults = null;
        try {
            searchResults = DatabaseConnection.searchBooks(searchQuery); // Phương thức mới
        } catch (SQLException e) {
            // Xử lý lỗi cơ sở dữ liệu
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho người dùng
            return;
        }

        displayBooks(searchResults); // Phương thức để hiển thị kết quả
    }

    private void displayBooks(List<Book> books) {
        bookContainer.getChildren().clear(); // Xóa kết quả trước đó
        int column = 0;
        int row = 1;
        try {
            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Book-view.fxml"));
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
