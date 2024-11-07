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
    private Label LoginMessageLabelCheckMark; // Label to print login successful,etc
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


    public void initialize() {
        StatusIconCheckMark.setVisible(false);
        StatusIconXmark.setVisible(false);
        LoginMessageLabelXmark.setVisible(false);
        LoginMessageLabelCheckMark.setVisible(false);

    }





    /**
     * Use to login.
     * If username and password are blank, check validatelogin.
     * @param event an event?
     */
    public void loginButtonAction(ActionEvent event) {
        connectUserAccount();
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            ValidateLogin();
        } else {
            LoginMessageLabelXmark.setText("Invalid login. Please try again.  ");
        }
    }

    public void showSuccessful() {
        // Thiết lập nội dung và hiển thị label, icon
        LoginMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);

        // Tạo hiệu ứng fade in cho label
        FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelCheckMark);
        fadeInLabel.setFromValue(0);
        fadeInLabel.setToValue(1);

        // Tạo hiệu ứng fade out cho label
        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5), LoginMessageLabelCheckMark);
        fadeOutLabel.setFromValue(1);
        fadeOutLabel.setToValue(0);

        // Tạo hiệu ứng fade in cho StatusIcon
        FadeTransition fadeInIcon = new FadeTransition(Duration.seconds(1.5), StatusIconCheckMark);
        fadeInIcon.setFromValue(0);
        fadeInIcon.setToValue(1);

        // Tạo hiệu ứng fade out cho StatusIcon
        FadeTransition fadeOutIcon = new FadeTransition(Duration.seconds(1.5), StatusIconCheckMark);
        fadeOutIcon.setFromValue(1);
        fadeOutIcon.setToValue(0);

        // Tạo thời gian chờ trước khi chuyển scene
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5)); // Thời gian chờ 1.5s

        // Kết hợp các hiệu ứng
        fadeInLabel.setOnFinished(event -> pause.play());        // Chạy pause sau khi fade in label
        fadeInIcon.play();                                       // Chạy fade in cho icon cùng lúc với label
        pause.setOnFinished(event -> {
            fadeOutLabel.play();
            fadeOutIcon.play();    // Chạy fade out cho icon cùng lúc với label
        });
        fadeOutLabel.setOnFinished(event -> loadLibraryView());  // Chuyển scene sau khi fade out

        // Bắt đầu hiệu ứng fade in cho label và icon
        fadeInLabel.play();
    }




    public void showError() {
        // Đặt thông báo lỗi

        LoginMessageLabelXmark.setVisible(true);
        StatusIconXmark.setVisible(true);  // Đảm bảo StatusIcon cũng hiển thị

        // Fade transition để làm label hiện dần ra
        FadeTransition fadeInLabel = new FadeTransition();
        fadeInLabel.setDuration(Duration.seconds(1.5)); // Thời gian làm rõ dần
        fadeInLabel.setNode(LoginMessageLabelXmark);
        fadeInLabel.setFromValue(0); // Bắt đầu với độ mờ 0 (ẩn)
        fadeInLabel.setToValue(1);   // Kết thúc với độ mờ 1 (hiện rõ)

        // Fade transition để làm StatusIcon hiện dần ra
        FadeTransition fadeInIcon = new FadeTransition();
        fadeInIcon.setDuration(Duration.seconds(1.5)); // Thời gian làm rõ dần
        fadeInIcon.setNode(StatusIconXmark);
        fadeInIcon.setFromValue(0); // Bắt đầu với độ mờ 0 (ẩn)
        fadeInIcon.setToValue(1);   // Kết thúc với độ mờ 1 (hiện rõ)

        // Fade transition để làm label mờ dần đi
        FadeTransition fadeOutLabel = new FadeTransition();
        fadeOutLabel.setDuration(Duration.seconds(1.5)); // Thời gian làm mờ dần
        fadeOutLabel.setNode(LoginMessageLabelXmark);
        fadeOutLabel.setFromValue(1); // Bắt đầu với độ mờ 1 (hiện rõ)
        fadeOutLabel.setToValue(0);   // Kết thúc với độ mờ 0 (ẩn)

        // Fade transition để làm StatusIcon mờ dần đi
        FadeTransition fadeOutIcon = new FadeTransition();
        fadeOutIcon.setDuration(Duration.seconds(1.5)); // Thời gian làm mờ dần
        fadeOutIcon.setNode(StatusIconXmark);
        fadeOutIcon.setFromValue(1); // Bắt đầu với độ mờ 1 (hiện rõ)
        fadeOutIcon.setToValue(0);   // Kết thúc với độ mờ 0 (ẩn)

        // Kết hợp các hiệu ứng
        fadeInLabel.setOnFinished(event -> {
            // Sau khi làm rõ dần, thực hiện mờ dần
            fadeOutLabel.play();
            fadeOutIcon.play();  // Mờ dần StatusIcon sau khi fadeIn hoàn tất
        });

        // Chạy hiệu ứng fadeIn đầu tiên
        fadeInLabel.play();
        fadeInIcon.play();  // Chạy fadeIn cho StatusIcon cùng lúc
    }


    /**
     * Connect to the database,
     * If there is only 1 username and password match in the database then login successful.
     */
    public void ValidateLogin() {
        Connection con = DatabaseConnection.getConnection();
        String verifyLogin = "SELECT count(1) FROM loginschema.user_account WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(verifyLogin);
            preparedStatement.setString(1, UsernameField.getText());
            preparedStatement.setString(2, PasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    showSuccessful();  // Gọi showSuccessful() mà không gọi loadLibraryView()
                } else {
                    showError();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadLibraryView() {
        try {
            // Load the FXML for library view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"));
            Parent libraryView = loader.load();

            // Get the current stage
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(libraryView));
            stage.setTitle("Library View");
            stage.show(); // Display the new scene

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // When you click the hyperlink
    public void onHyperLinkClick(ActionEvent event) throws IOException {
        // Call the method to load the registration view
        loadRegisterView(event);
    }

    // Load the registration view
    public void loadRegisterView(ActionEvent event) throws IOException {
            // Load register-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/register-view.fxml"));
            Parent registerView = loader.load();

            // Get the current stage
            Stage stage = (Stage) RegisterLink.getScene().getWindow();
            stage.setScene(new Scene(registerView));
            stage.setTitle("Register");
            stage.show();
    }

}
