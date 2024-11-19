package com.library;
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

import static com.library.databaseConnection.connectUserAccount;
import static com.library.sceneController.stage;

public class loginController {
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
    @FXML
    private ImageView cutePic;


    public void initialize() {
        connectUserAccount();
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
        // Ẩn các phần tử không cần thiết
        LoginMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);
        UsernameField.setDisable(true);
        PasswordField.setDisable(true);
        RegisterLink.setDisable(true);
        UsernameField.clear();
        PasswordField.clear();

        // Tạo hiệu ứng fade in và fade out
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

        // Đảm bảo fadeOutLabel kết thúc trước khi chuyển scene
        fadeOutLabel.setOnFinished(event -> {
            // Lấy Stage hiện tại từ RegisterLink
            stage = (Stage) LoginButton.getScene().getWindow();

            // Kiểm tra tài khoản và chuyển tới màn hình phù hợp
            String username = userController.getCurrentUser().getUsername();
            if (username.equals("admin")) {
                sceneController.loadScreen("/com/library/libraryadmin-view.fxml", stage, "Admin Dashboard");
            } else {
                sceneController.loadScreen("/com/library/Dashboard-view.fxml", stage, "Library");
            }
        });

        fadeInLabel.play();
    }

    public void showError() {
        LoginMessageLabelXmark.setVisible(true);
        StatusIconXmark.setVisible(true);
        cutePic.setImage(new Image(getClass().getResource("/ScreenUI/Picture/bongo-cat-smash.gif").toExternalForm()));
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
        Connection con = databaseConnection.getConnection();
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

            if (queryResult.next() && queryResult.getInt(1) == 1) {
                String addUser = "SELECT account_id, username, firstname, lastname, password, userPicture, email, phone FROM user_account WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement2 = con.prepareStatement(addUser);
                preparedStatement2.setString(1, UsernameField.getText());
                preparedStatement2.setString(2, PasswordField.getText());
                ResultSet queryResult2 = preparedStatement2.executeQuery();

                String userID = queryResult2.getString("account_id");
                String username = queryResult2.getString("username");
                String firstname = queryResult2.getString("firstname");
                String lastname = queryResult2.getString("lastname");
                String password = queryResult2.getString("password");
                byte[] userPicture = queryResult2.getBytes("userPicture");

                // Kiểm tra email và phone nếu là null
                String email = queryResult2.getString("email");
                String phone = queryResult2.getString("phone");

                // Tạo đối tượng User, nếu email hoặc phone null thì giữ nguyên giá trị null
                User currentUser = new User(userID, username, firstname, lastname, userPicture,
                        email != null ? email : null,
                        phone != null ? phone : null,
                        password);

                // Lưu trữ user vào UserUtils
                userController.setCurrentUser(currentUser);
                System.out.println(currentUser);
                showSuccessful();
            } else {
                showError();  // Đăng nhập thất bại
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError(); // Hiển thị thông báo lỗi trong trường hợp xảy ra lỗi SQL
        }

    }


    public void onHyperLinkClick() throws IOException {
        // Lấy Stage hiện tại từ RegisterLink
        stage = (Stage) RegisterLink.getScene().getWindow();

        // Load giao diện đăng ký mới
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/register-view.fxml"));
        Parent registerView = loader.load();

        // Thay đổi scene trong cùng một cửa sổ
        stage.setScene(new Scene(registerView));
        stage.setTitle("Register");
        stage.show();  // Có thể không cần thiết nếu Stage không bị ẩn
    }




}
