package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.sceneController.stage;
import static com.library.userController.currentUser;

public class dashBoardController implements Initializable {
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

    private List<book> recentlyAdded;
    private List<book> recommended;
    private List<book> searched;

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
    private String oldSearchQuery = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                searchLayout.getChildren().clear();
                isSearchPaneVisible = false;
                searchPane.setVisible(false);
            } else {
                if (!newValue.equals(oldSearchQuery)) {
                    searchBooks(newValue);
                    oldSearchQuery = newValue;
                }
            }
        });

        username.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        if (currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }

        List<book> allBooks = null;
        try {
            // Lấy tất cả sách từ cơ sở dữ liệu
            allBooks = databaseConnection.getBooks();
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
            for (book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card-view.fxml"));
                HBox cardBox = fxmlLoader.load(); // Tạo HBox cho thẻ sách
                cardController cardController = fxmlLoader.getController();
                cardController.setData(value);
                cardLayout.getChildren().add(cardBox);
            }

            // Hiển thị sách được đề xuất
            for (com.library.book book : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Book-view.fxml"));
                VBox bookBox = fxmlLoader.load(); // Tạo VBox cho sách
                bookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (column == 6) {
                    column = 0;
                    row++;
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(10));
            }
            AnchorPane root = (AnchorPane) searchPane.getParent();
            root.setOnMouseClicked(event -> {
                if (isSearchPaneVisible && !searchPane.contains(event.getX(), event.getY())) {
                    searchPane.setVisible(false);
                    searchPane.setManaged(false);
                    isSearchPaneVisible = false;
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void searchBooks(String searchQuery) {
        // Kiểm tra nếu searchQuery trống
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            searchLayout.getChildren().clear();
            return;
        }

        if (!isSearchPaneVisible) {
            searchPane.setVisible(true);
            searchPane.setManaged(true);
            isSearchPaneVisible = true;
        }

        List<book> searchResults;
        try {
            searchResults = databaseConnection.searchBooks(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Hiển thị kết quả tìm kiếm
        displaySearchResults(searchResults);
    }

    private void displaySearchResults(List<book> books) {
        searchLayout.getChildren().clear();

        if (books.isEmpty()) {
            Label noResultsLabel = new Label("Không tìm thấy kết quả.");
            searchLayout.getChildren().add(noResultsLabel);
        } else {
            try {
                for (com.library.book book : books) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("search-view.fxml"));
                    HBox searchBox = fxmlLoader.load();
                    searchController searchController = fxmlLoader.getController();
                    searchController.setData(book);
                    searchLayout.getChildren().add(searchBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void onDashboardBtnClick() {
        sceneController.handleDashboardButton(stage);
    }
    public void onSettingsBtnClick() {
        sceneController.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        sceneController.handleMyCollectionButton(stage);
    }
    public void onLogOutBtnClk() {
        sceneController.handleLogoutButton(stage);
    }



}
