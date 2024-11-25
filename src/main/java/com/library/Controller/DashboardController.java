package com.library.Controller;

import com.library.Book;
import com.library.DatabaseConnection;
import com.library.LibraryApplication;
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

import static com.library.Controller.UserController.currentUser;

public class DashboardController extends SceneController implements Initializable {
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
    private Button gameBtn;
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
        List <Book> trending = new ArrayList<>();
        try {
            trending = DatabaseConnection.getMostBorrowedBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Book> allBooks = null;
        try {
            // Lấy tất cả sách từ cơ sở dữ liệu
            allBooks = DatabaseConnection.getBooks();
            System.out.println("Books retrieved: " + allBooks.size());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        recentlyAdded = new ArrayList<>(trending);
        recommended = new ArrayList<>(allBooks);

        int column = 0; // Đếm số cột trong GridPane
        int row = 1; // Đếm số hàng trong GridPane

        try {
            // Hiển thị sách mới thêm
            for (Book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/library/Card-view.fxml"));
                HBox cardBox = fxmlLoader.load(); // Tạo HBox cho thẻ sách
                CardController cardController = fxmlLoader.getController();
                cardController.setData(value);
                cardLayout.getChildren().add(cardBox);
            }

            // Hiển thị sách được đề xuất
            for (Book book : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/library/Book-View.fxml"));
                VBox bookBox = fxmlLoader.load(); // Tạo VBox cho sách
                BookController bookController = fxmlLoader.getController();
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

    private void displaySearchResults(List<Book> books) {
        searchLayout.getChildren().clear();

        if (books.isEmpty()) {
            Label noResultsLabel = new Label("No results found");
            searchLayout.getChildren().add(noResultsLabel);
        } else {
            try {
                for (Book book : books) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/com/library/search-view.fxml"));
                    HBox searchBox = fxmlLoader.load();
                    SearchController searchController = fxmlLoader.getController();
                    searchController.setData(book);
                    searchLayout.getChildren().add(searchBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    public void onDashboardBtnClick() {
        LibraryApplication.getSceneController().loadDashboardView();
    }

    @FXML
    public void onSettingsBtnClick() {
        LibraryApplication.getSceneController().loadSettingView();
    }

    @FXML
    public void onMyCollectionBtnClick() {
        LibraryApplication.getSceneController().loadMyCollectionView();
    }

    @FXML
    public void onLogOutBtnClick() {
        LibraryApplication.getSceneController().loadLoginView();
    }

    @FXML
    public void onGameBtnClick() {
        LibraryApplication.getSceneController().loadGameView();
    }



}
