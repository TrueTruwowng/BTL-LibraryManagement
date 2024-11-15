package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.SceneLoader.stage;

public class DashBoardController implements Initializable {
    @FXML
    private HBox cardLayout;

    @FXML
    private GridPane bookContainer;

    private List<Book> recentlyAdded;
    private List<Book> recommended;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button historyBtn;
    @FXML
    private Button gameBtn;
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
        //DatabaseConnection.connectUserAccount(); // Nếu cần thiết
        User currentUser = com.library.UserController.getCurrentUser();
        if (currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }
        username.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
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
    public void onDashboardBtnClick() {
        SceneLoader.handleDashboardButton(stage);
    }

    public void onHistoryBtnClick() {
        SceneLoader.handleHistoryButton(stage);
    }
    public void onGameBtnClick() {
        SceneLoader.handleGameButton(stage);
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
