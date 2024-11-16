package com.library;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.SceneLoader.stage;
import static com.library.UserController.currentUser;

public class DashBoardController implements Initializable {
    @FXML
    private AnchorPane searchPane;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView searchButton;

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
    private Button dashboardBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button myCollectionBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ImageView smallUserImageView;
    @FXML
    private Label username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setText(currentUser.getUsername());
        if (currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }

        List<Book> allBooks = null;
        try {
            // Lấy tất cả sách từ cơ sở dữ liệu
            allBooks = DatabaseConnection.getBooks();
            System.out.println("Books retrieved: " + allBooks.size());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        recentlyAdded = new ArrayList<>(allBooks);
        recommended = new ArrayList<>(allBooks);

        int column = 0; // Đếm số cột trong GridPane
        int row = 1; // Đếm số hàng trong GridPane

        try {
            // Hiển thị sách mới thêm
            for (Book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card-view.fxml")); // Tải tệp FXML cho thẻ sách
                HBox cardBox = fxmlLoader.load(); // Tạo HBox cho thẻ sách
                CardController cardController = fxmlLoader.getController(); // Lấy controller cho thẻ sách
                cardController.setData(value); // Thiết lập dữ liệu cho thẻ sách
                cardLayout.getChildren().add(cardBox); // Thêm HBox vào cardLayout
            }

            // Hiển thị sách được đề xuất
            for (Book book : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Book-view.fxml")); // Tải tệp FXML cho sách
                VBox bookBox = fxmlLoader.load(); // Tạo VBox cho sách
                BookController bookController = fxmlLoader.getController(); // Lấy controller cho sách
                bookController.setData(book); // Thiết lập dữ liệu cho sách

                if (column == 6) { // Nếu đã đạt đến 6 cột
                    column = 0; // Reset lại cột
                    row++; // Chuyển sang hàng tiếp theo
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(10));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void searchBooks(Event event) {
        if (!isSearchPaneVisible) { // Kiểm tra nếu bảng tìm kiếm chưa hiển thị
            searchPane.setVisible(true); // Hiện bảng tìm kiếm
            searchPane.setManaged(true); // Quản lý bảng tìm kiếm
            isSearchPaneVisible = true; // Cập nhật trạng thái
        } else {
            searchPane.setVisible(false); // Ẩn bảng tìm kiếm
            searchPane.setManaged(false); // Ngừng quản lý bảng tìm kiếm
            isSearchPaneVisible = false; // Cập nhật trạng thái
        }

        // Lấy truy vấn tìm kiếm từ trường văn bản
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

        // Hiển thị kết quả tìm kiếm
        displaySearchResults(searchResults);
    }

    // Phương thức để hiển thị kết quả tìm kiếm
    private void displaySearchResults(List<Book> books) {
        searchLayout.getChildren().clear();
        if (books.isEmpty()) {
            Label noResultsLabel = new Label("Không tìm thấy kết quả.");
            searchLayout.getChildren().add(noResultsLabel);
            return;
        }

        try {
            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("search-view.fxml"));
                HBox searchBox = fxmlLoader.load(); // Tải HBox cho kết quả
                SearchController searchController = fxmlLoader.getController();
                searchController.setData(book);
                searchLayout.getChildren().add(searchBox);
            }
        } catch (IOException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }

    public void onDashboardBtnClick() {
        SceneLoader.handleDashboardButton(stage);
    }
    public void onSettingsBtnClick() {
        SceneLoader.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        SceneLoader.handleMyCollectionButton(stage);
    }
    public void onLogOutBtnClk() {
        SceneLoader.handleLogoutButton(stage);
    }



}
