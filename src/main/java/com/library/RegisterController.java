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
        // Check if is the textfield is blank
        if (FirstnameField.getText().isEmpty() || LastnameField.getText().isEmpty() ||
                UsernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                confirmPasswordField.getText().isEmpty()) {

            RegisterMessageLabel.setText("Please fill in all fields");
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            MatchingLabel.setText("Passwords do not match");
        } else {
            if (RegisterUser()) {  // Điều chỉnh phương thức RegisterUser trả về boolean
                MatchingLabel.setText("You are set");
                // Chỉ thực hiện chuyển đổi sau khi tài khoản được tạo thành công
                PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 2sec
                pause.setOnFinished(e -> onHyperLinkClick(event)); // change to login screen
                pause.play();
            }
        }
    }

    public boolean RegisterUser() {
        databaseConnection.connect(); // Kết nối trước khi gọi getConnection
        Connection con = databaseConnection.getConnection();

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
                    RegisterMessageLabel.setText("Account already exists");
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
            RegisterMessageLabel.setText("Create account successfully");
            return true;  // Register successfully

        } catch(Exception e) {
            e.printStackTrace();
            RegisterMessageLabel.setText("An error occurred during registration.");
            return false;  // ERROR OCCURS
        } finally {
            try {
                con.close(); // Close connection
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