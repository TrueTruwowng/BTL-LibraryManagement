package com.library;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static com.library.SceneLoader.stage;
import static com.library.SceneLoader.loadLoginView;

public class RegisterController {
    @FXML
    private Label RegisterMessageLabelXMark;

    @FXML
    private Hyperlink BacktoLoginHyperlink;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField FirstnameField;
    @FXML
    private TextField LastnameField;
    @FXML
    private TextField UsernameField;
    @FXML
    private FontAwesomeIcon StatusIconXmark;
    @FXML
    private FontAwesomeIcon StatusIconCheckMark;
    @FXML
    private Label RegisterMessageLabelCheckMark;

    private boolean isAnimating = false; // Biến để kiểm tra trạng thái animation

    public void initialize() {
        DatabaseConnection.connectUserAccount();
        RegisterMessageLabelCheckMark.setVisible(false);
        RegisterMessageLabelXMark.setVisible(false);
        StatusIconCheckMark.setVisible(false);
        StatusIconXmark.setVisible(false);
    }

    public void registerButtonOnAction(ActionEvent event) {
        if (FirstnameField.getText().isEmpty() || LastnameField.getText().isEmpty() ||
                UsernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {

            RegisterMessageLabelXMark.setText("Please fill in all fields");
            showError();
        } else if (passwordField.getText().length() < 6 || passwordField.getText().length() > 8) {
            RegisterMessageLabelXMark.setText("Password must be 6-8 characters");
            showError();
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            RegisterMessageLabelXMark.setText("Passwords do not match");
            showError();
        } else {
            if (registerUser()) {
                RegisterMessageLabelCheckMark.setText("Registered Successfully");
                showSuccessful();
                PauseTransition pause = new PauseTransition(Duration.seconds(3));
                pause.setOnFinished(e -> onHyperLinkClick());
                pause.play();
            }
        }
    }


    public boolean registerUser() {
        Connection con = DatabaseConnection.getConnection();
        String firstName = FirstnameField.getText();
        String lastName = LastnameField.getText();
        String username = UsernameField.getText();
        String password = passwordField.getText();

        String checkUsernameQuery = "SELECT COUNT(*) FROM user_account WHERE username = ?";

        try {
            PreparedStatement checkStatement = con.prepareStatement(checkUsernameQuery);
            checkStatement.setString(1, username);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                RegisterMessageLabelXMark.setText("Username already exists");
                showError();
                return false;
            }

            String accountId = UUID.randomUUID().toString();

            // Lấy ảnh mặc định từ ImageUtils
            byte[] defaultImageBytes = UserController.getDefaultImageBytes("/ScreenUI/Picture/VectorLogo.png");

            String insertField = "INSERT INTO user_account(account_id, lastname, firstname, username, password, userPicture) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertField);
            insertStatement.setString(1, accountId);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, firstName);
            insertStatement.setString(4, username);
            insertStatement.setString(5, password);
            insertStatement.setBytes(6, defaultImageBytes); // Lưu ảnh mặc định vào cột userPicture

            insertStatement.executeUpdate();
            RegisterMessageLabelXMark.setText("Registered Successfully");
            showSuccessful();
            DatabaseConnection.closeConnection();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            RegisterMessageLabelXMark.setText("An error occurred during registration.");
            return false;
        }
    }

    public void onHyperLinkClick() {
        Stage stage = (Stage) BacktoLoginHyperlink.getScene().getWindow();
        loadLoginView(stage);  // Sử dụng SceneLoader thay vì tự mình load
    }


    public void showSuccessful() {
        RegisterMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);
        UsernameField.setDisable(true);
        passwordField.setDisable(true);
        LastnameField.setDisable(true);
        FirstnameField.setDisable(true);
        confirmPasswordField.setDisable(true);
        FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5)
                , RegisterMessageLabelCheckMark);
        fadeInLabel.setFromValue(0);
        fadeInLabel.setToValue(1);

        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5)
                , RegisterMessageLabelCheckMark);
        fadeOutLabel.setFromValue(1);
        fadeOutLabel.setToValue(0);

        FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.5)
                , StatusIconCheckMark);
        fadeInIcon.setFromValue(0);
        fadeInIcon.setToValue(1);

        FadeTransition fadeOutIcon = new FadeTransition(Duration.seconds(1.5)
                , StatusIconCheckMark);
        fadeOutIcon.setFromValue(1);
        fadeOutIcon.setToValue(0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        fadeInLabel.setOnFinished(event -> pause.play());
        fadeInIcon.play();
        pause.setOnFinished(event -> {
            fadeOutLabel.play();
            fadeOutIcon.play();
        });
        SceneLoader.stage = (Stage) BacktoLoginHyperlink.getScene().getWindow();
        fadeOutLabel.setOnFinished(event -> loadLoginView(stage));

        fadeInLabel.play();
    }
    public void showError() {
        RegisterMessageLabelXMark.setVisible(true);
        StatusIconXmark.setVisible(true);

        passwordField.clear();
        confirmPasswordField.clear();
        FirstnameField.clear();
        LastnameField.clear();
        UsernameField.clear();


        if (!isAnimating) {
            isAnimating = true;

            FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5), RegisterMessageLabelXMark);
            fadeInLabel.setFromValue(0);
            fadeInLabel.setToValue(1);

            FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.5), StatusIconXmark);
            fadeInIcon.setFromValue(0);
            fadeInIcon.setToValue(1);

            FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5), RegisterMessageLabelXMark);
            fadeOutLabel.setFromValue(1);
            fadeOutLabel.setToValue(0);

            FadeTransition fadeOutIcon = new FadeTransition(Duration.seconds(1.5), StatusIconXmark);
            fadeOutIcon.setFromValue(1);
            fadeOutIcon.setToValue(0);

            fadeInLabel.setOnFinished(event -> {
                fadeOutLabel.play();
                fadeOutIcon.play();
            });

            fadeOutLabel.setOnFinished(event -> isAnimating = false);

            fadeInLabel.play();
            fadeInIcon.play();
        }
    }

}