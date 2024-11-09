package com.library;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.util.Duration;

import static com.library.DatabaseConnection.connectUserAccount;
public class LoginController {
    @FXML
    private Label LoginMessageLabelXmark;
    @FXML
    private Button LoginButton;
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


    public void initialize() {
        StatusIconCheckMark.setVisible(false);
        StatusIconXmark.setVisible(false);
        LoginMessageLabelXmark.setVisible(false);
        LoginMessageLabelCheckMark.setVisible(false);

    }

    /**
     * Use to login.
     * If username and password are blank, check validatelogin.
     */
    public void loginButtonAction() {
        connectUserAccount();
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            ValidateLogin();
        } else {
            LoginMessageLabelXmark.setText("Invalid login. Please try again.  ");
        }
    }

    public void showSuccessful() {
        LoginMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);
        UsernameField.setDisable(true);
        PasswordField.setDisable(true);
        UsernameField.clear();
        PasswordField.clear();

        FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5)
                , LoginMessageLabelCheckMark);
        fadeInLabel.setFromValue(0);
        fadeInLabel.setToValue(1);

        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5)
                , LoginMessageLabelCheckMark);
        fadeOutLabel.setFromValue(1);
        fadeOutLabel.setToValue(0);

        FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.0)
                , StatusIconCheckMark);
        fadeInIcon.setFromValue(0);
        fadeInIcon.setToValue(1);

        FadeTransition fadeOutIcon = new FadeTransition(Duration.seconds(1.0)
                , StatusIconCheckMark);
        fadeOutIcon.setFromValue(1);
        fadeOutIcon.setToValue(0);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0));

        fadeInLabel.setOnFinished(event -> pause.play());
        fadeInIcon.play();
        pause.setOnFinished(event -> {
            fadeOutLabel.play();
            fadeOutIcon.play();
        });
        fadeOutLabel.setOnFinished(event -> loadLibraryView());

        fadeInLabel.play();
    }

    public void showError() {
        LoginMessageLabelXmark.setVisible(true);
        StatusIconXmark.setVisible(true);

        UsernameField.clear();
        PasswordField.clear();

        if (!isAnimating) {
            isAnimating = true;

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
    public void ValidateLogin() {
        Connection con = DatabaseConnection.getConnection();
        if (con == null) {
            System.out.println("Không thể kết nối tới cơ sở dữ liệu.");
            showError();
            return;
        }

        String verifyLogin = "SELECT count(1) FROM user_account WHERE username = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(verifyLogin);
            preparedStatement.setString(1, UsernameField.getText());
            preparedStatement.setString(2, PasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

            // Kiểm tra kết quả từ câu truy vấn
            if (queryResult.next() && queryResult.getInt(1) == 1) {
                showSuccessful();  // Đăng nhập thành công
            } else {
                showError();  // Đăng nhập thất bại
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError(); // Hiển thị thông báo lỗi trong trường hợp xảy ra lỗi SQL
        }
    }


    private void loadLibraryView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"));
            Parent libraryView = loader.load();

            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(libraryView));
            stage.setTitle("Library View");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onHyperLinkClick(ActionEvent event) throws IOException {
        loadRegisterView(event);
    }

    public void loadRegisterView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/register-view.fxml"));
        Parent registerView = loader.load();
        Stage stage = (Stage) RegisterLink.getScene().getWindow();
        stage.setScene(new Scene(registerView));
        stage.setTitle("Register");
        stage.show();
    }

}