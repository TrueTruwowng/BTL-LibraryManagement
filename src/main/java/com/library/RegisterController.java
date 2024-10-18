package com.library;

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
import java.sql.Statement;

public class RegisterController {

    @FXML
    private Button registerButton;

    @FXML
    private Label MatchingLabel;

    @FXML
    private Label RegisterMessageLabel;

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

    private DatabaseConnection databaseConnection = new DatabaseConnection();

    public void registerButtonOnAction(ActionEvent event) {
        if(passwordField.getText().equals(confirmPasswordField.getText())) {
            RegisterUser();
            MatchingLabel.setText("You are set");
            /// wait for transition
            PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 2sec
            pause.setOnFinished(e -> onHyperLinkClick(event)); // change to login screen
            pause.play();
        } else {
            MatchingLabel.setText("Passwords do not match");
        }

    }

    public void RegisterUser() {
        databaseConnection.connect(); // Kết nối trước khi gọi getConnection
        Connection con = databaseConnection.getConnection();

        String firstName = FirstnameField.getText();
        String lastName = LastnameField.getText();
        String username = UsernameField.getText();
        String password = passwordField.getText();

        // Kiểm tra xem username đã tồn tại hay chưa
        String checkUsernameQuery = "SELECT COUNT(*) FROM login.user_account WHERE username = ?";

        try {
            // Sử dụng PreparedStatement để bảo vệ khỏi SQL Injection
            PreparedStatement checkStatement = con.prepareStatement(checkUsernameQuery);
            checkStatement.setString(1, username);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    // Username đã tồn tại
                    RegisterMessageLabel.setText("Account already exists");
                    return; // Thoát khỏi phương thức
                }
            }

            ///if username did not exit then insert new one
            String insertField = "INSERT INTO login.user_account(lastname, firstname, username, password)" +
                    " VALUES (?, ?, ?, ?)";

            PreparedStatement insertStatement = con.prepareStatement(insertField);
            insertStatement.setString(1, lastName);
            insertStatement.setString(2, firstName);
            insertStatement.setString(3, username);
            insertStatement.setString(4, password);

            insertStatement.executeUpdate();
            RegisterMessageLabel.setText("Create account successfully");

        } catch(Exception e) {
            e.printStackTrace();
            RegisterMessageLabel.setText("An error occurred during registration.");
        } finally {
            try {
                con.close(); // Đảm bảo đóng kết nối
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onHyperLinkClick(ActionEvent event) {
        // Call the method to load the registration view
        loadLoginView(event);
    }

    // Load the registration view
    private void loadLoginView(ActionEvent event) {
        try {
            // Load register-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/login-view.fxml"));
            Parent registerView = loader.load();

            // Get the current stage
            Stage stage = (Stage) BacktoLoginHyperlink.getScene().getWindow();
            stage.setScene(new Scene(registerView));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            RegisterMessageLabel.setText("Error loading login view: " + e.getMessage());
        }
    }
}