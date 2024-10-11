package com.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @FXML
    private Button LoginButton;
    @FXML
    private Label LoginMessageLabel; // Label to print login successful,etc
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField UsernameField;

    /**
     * Use to login.
     * @param event an event?
     */
    public void loginButtonAction(ActionEvent event) {
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            ValidateLogin();
        } else {
            LoginMessageLabel.setText("Invalid login. Please try again!!!");
        }
    }

    public void ValidateLogin() {
        databaseConnection.connect(); // Connect before call getConnection
        Connection con = databaseConnection.getConnection();

        if (con == null) {
            LoginMessageLabel.setText("Could not connect to the database.");
            return; // Exit if cannot connect
        }

        ///Check if username and password == data in mysql
        String verifyLogin = "SELECT count(1) FROM loginschema.user_account WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(verifyLogin);
            preparedStatement.setString(1, UsernameField.getText());
            preparedStatement.setString(2, PasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();
            if (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    LoginMessageLabel.setText("Login Successful");
                    // Load the new FXML view
                    loadLibraryView();
                } else {
                    LoginMessageLabel.setText("Login Failed! Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            LoginMessageLabel.setText("Database error: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close(); // close connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLibraryView() {
        try {
            // Load the library-view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library-view.fxml"));
            Parent root = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LoginMessageLabel.setText("Failed to load the library view: " + e.getMessage());
        }
    }

}
