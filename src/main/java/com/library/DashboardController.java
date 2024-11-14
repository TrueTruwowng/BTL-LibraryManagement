package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

import static com.library.SceneLoader.stage;


public class DashboardController {
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
    public void initialize() {
        User currentUser = UserController.getCurrentUser();
        if (currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }
        username.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
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
