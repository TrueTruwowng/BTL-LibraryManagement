package com.library;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void initialize() {
        DatabaseConnection.connectUserAccount();
        RegisterMessageLabelCheckMark.setVisible(false);
        RegisterMessageLabelXMark.setVisible(false);
        StatusIconCheckMark.setVisible(false);
        StatusIconXmark.setVisible(false);
    }

    public void registerButtonOnAction(ActionEvent event) {
        // Check if is the textfield is blank

        if (FirstnameField.getText().isEmpty() || LastnameField.getText().isEmpty() ||
                UsernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {

            RegisterMessageLabelXMark.setText("Please fill in all fields");
            showError();
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            RegisterMessageLabelXMark.setText("Passwords do not match");
            showError();
        } else {
            if (RegisterUser()) {  // Điều chỉnh phương thức RegisterUser trả về boolean
                RegisterMessageLabelCheckMark.setText("Registered Successfully");
                showSuccessful();
                // Chỉ thực hiện chuyển đổi sau khi tài khoản được tạo thành công
                PauseTransition pause = new PauseTransition(Duration.seconds(3)); // 2sec
                pause.setOnFinished(e -> onHyperLinkClick(event)); // change to login screen
                pause.play();

            }
        }
    }

    public boolean RegisterUser() {
        Connection con = DatabaseConnection.getConnection();
        String firstName = FirstnameField.getText();
        String lastName = LastnameField.getText();
        String username = UsernameField.getText();
        String password = passwordField.getText();

        String checkUsernameQuery = "SELECT COUNT(*) FROM loginschema.user_account WHERE username = ?";

        try {
            PreparedStatement checkStatement = con.prepareStatement(checkUsernameQuery);
            checkStatement.setString(1, username);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    RegisterMessageLabelXMark.setText("Username already exists");
                    showError();
                    return false;
                }
            }

            String insertField = "INSERT INTO loginschema.user_account(lastname, firstname, username, password)" +
                    " VALUES (?, ?, ?, ?)";

            PreparedStatement insertStatement = con.prepareStatement(insertField);
            insertStatement.setString(1, lastName);
            insertStatement.setString(2, firstName);
            insertStatement.setString(3, username);
            insertStatement.setString(4, password);

            insertStatement.executeUpdate();
            RegisterMessageLabelXMark.setText("Registered Successfully");
            showSuccessful();
            return true;  // Register successfully

        } catch(Exception e) {
            e.printStackTrace();
            RegisterMessageLabelXMark.setText("An error occurred during registration.");
            return false;  // ERROR OCCURS
        }
    }
    public void onHyperLinkClick(ActionEvent event) {
        loadLoginView();
    }

    // Load the registration view
    public void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/login-view.fxml"));
            Parent loginView = loader.load();

            // Get the current stage (window) from any component in the current scene
            Stage stage = (Stage) RegisterMessageLabelCheckMark.getScene().getWindow(); // Use another UI component if needed

            if (stage != null) {
                stage.setScene(new Scene(loginView));
                stage.setTitle("Login");
                stage.show();
            } else {
                RegisterMessageLabelXMark.setText("Stage is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            RegisterMessageLabelXMark.setText("Error loading login view: " + e.getMessage());
        }
    }
    
    public void showSuccessful() {
        // Thiết lập nội dung và hiển thị label, icon
        RegisterMessageLabelCheckMark.setVisible(true);
        StatusIconCheckMark.setVisible(true);

        // Tạo hiệu ứng fade in cho label
        FadeTransition fadeInLabel = new FadeTransition(Duration.seconds(1.5), RegisterMessageLabelCheckMark);
        fadeInLabel.setFromValue(0);
        fadeInLabel.setToValue(1);

        // Tạo hiệu ứng fade out cho label
        FadeTransition fadeOutLabel = new FadeTransition(Duration.seconds(1.5), RegisterMessageLabelCheckMark);
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
        fadeOutLabel.setOnFinished(event -> loadLoginView());  // Chuyển scene sau khi fade out

        // Bắt đầu hiệu ứng fade in cho label và icon
        fadeInLabel.play();
    }
    public void showError() {
        // Đặt thông báo lỗi

        RegisterMessageLabelXMark.setVisible(true);
        StatusIconXmark.setVisible(true);  // Đảm bảo StatusIcon cũng hiển thị

        // Fade transition để làm label hiện dần ra
        FadeTransition fadeInLabel = new FadeTransition();
        fadeInLabel.setDuration(Duration.seconds(1.5)); // Thời gian làm rõ dần
        fadeInLabel.setNode(RegisterMessageLabelXMark);
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
        fadeOutLabel.setNode(RegisterMessageLabelXMark);
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

}