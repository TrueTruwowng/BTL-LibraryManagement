package com.library.Controller;
import com.library.User;
import com.library.DatabaseConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.util.Duration;

import static com.library.Controller.UserController.currentUser;
import static com.library.DatabaseConnection.connectUserAccount;
import static com.library.Controller.SceneController.stage;

public class LoginController {
    @FXML
    private Label LoginMessageLabelXmark;
    @FXML
    private Button LoginButton;
    @FXML
    private Button LoginButton1;
    @FXML
    private Label LoginMessageLabelCheckMark;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField UsernameField;
    @FXML
    private Hyperlink RegisterLink;
    @FXML
    private FontAwesomeIcon StatusIconCheckMark;
    @FXML
    private FontAwesomeIcon StatusIconXmark;

    private boolean isAnimating = false;
    @FXML
    private ImageView cutePic;


    public void initialize() {
        StatusIconCheckMark.setVisible(false);
        StatusIconXmark.setVisible(false);
        LoginMessageLabelXmark.setVisible(false);
        LoginMessageLabelCheckMark.setVisible(false);

    }


    public void showSuccessful() {
        // Hiển thị các thành phần trạng thái thành công
        LoginMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);

        // Disable các trường nhập
        UsernameField.setDisable(true);
        PasswordField.setDisable(true);
        RegisterLink.setDisable(true);
        UsernameField.clear();
        PasswordField.clear();

        // Hiệu ứng fade in và fade out cho label và icon
        FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelCheckMark);
        fadeInLabel.setFromValue(0);
        fadeInLabel.setToValue(1);

        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelCheckMark);
        fadeOutLabel.setFromValue(1);
        fadeOutLabel.setToValue(0);

        FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.0), StatusIconCheckMark);
        fadeInIcon.setFromValue(0);
        fadeInIcon.setToValue(1);

        FadeTransition fadeOutIcon = new FadeTransition(Duration.seconds(1.0), StatusIconCheckMark);
        fadeOutIcon.setFromValue(1);
        fadeOutIcon.setToValue(0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));

        fadeInLabel.setOnFinished(event -> pause.play());
        fadeInIcon.play();
        pause.setOnFinished(event -> {
            fadeOutLabel.play();
            fadeOutIcon.play();
        });

        fadeInLabel.play();
    }




    public void showError() {
        // Hiển thị các thành phần trạng thái lỗi
        LoginMessageLabelXmark.setVisible(true);
        StatusIconXmark.setVisible(true);
        cutePic.setImage(new Image(getClass().getResource("/ScreenUI/Picture/bongo-cat-smash.gif").toExternalForm()));

        // Clear các trường nhập
        UsernameField.clear();
        PasswordField.clear();

        if (!isAnimating) {
            isAnimating = true;

            // Hiệu ứng fade in và fade out cho label và icon
            FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelXmark);
            fadeInLabel.setFromValue(0);
            fadeInLabel.setToValue(1);

            FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.5), StatusIconXmark);
            fadeInIcon.setFromValue(0);
            fadeInIcon.setToValue(1);

            FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelXmark);
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

    /**
     * Connect to the database,
     * If there is only 1 username and password match in the database then login successful.
     */
    private boolean validateUserLogin() {
        String verifyLogin = "SELECT * FROM user_account WHERE username = ? AND password = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(verifyLogin)) {

            preparedStatement.setString(1, UsernameField.getText());
            preparedStatement.setString(2, PasswordField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Tạo đối tượng User
                User user = new User(
                        resultSet.getString("account_id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        resultSet.getBytes("userPicture"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                UserController.setCurrentUser(user);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean validateAdminLogin() {
        if (!UsernameField.getText().equals("admin")) {
            System.out.println("Only 'admin' can log in via this method.");
            return false;
        }

        String verifyAdminLogin = "SELECT * FROM user_account WHERE username = 'admin' AND password = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(verifyAdminLogin)) {

            preparedStatement.setString(1, PasswordField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User admin = new User(
                        resultSet.getString("account_id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        resultSet.getBytes("userPicture"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                UserController.setCurrentUser(admin);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void onHyperLinkClick() throws IOException {
        stage = (Stage) RegisterLink.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/register-view.fxml"));
        Parent registerView = loader.load();

        stage.setScene(new Scene(registerView));
        stage.setTitle("Register");
        stage.show();
    }

    public void loginButtonAction() {
        connectUserAccount();
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            if (validateUserLogin()) {
                User currentUser1 = currentUser;
                showSuccessful();
                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                pause.setOnFinished(event -> {
                    SceneController.loadDashboardView(stage);
                });
                pause.play();
            } else {
                showError(); // Show error animation if validation fails
            }
        } else {
            LoginMessageLabelXmark.setText("Invalid login. Please try again.");
            showError();
        }
    }


    public void adminLoginButtonAction() {
        connectUserAccount();
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            if (validateAdminLogin()) {
                User currentUser1 = currentUser;
                showSuccessful();

                PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
                pause.setOnFinished(event -> {
                    SceneController.loadAdminLibraryScene(stage);
                });
                pause.play();
            } else {
                LoginMessageLabelXmark.setText("Admin login failed. Please try again.");
                showError();
            }
        } else {
            LoginMessageLabelXmark.setText("Invalid login. Please try again.");
            showError();
        }
    }





}
