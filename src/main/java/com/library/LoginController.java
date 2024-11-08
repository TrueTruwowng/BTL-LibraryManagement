package com.library;

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
    @FXML
    private Hyperlink RegisterLink;

    /**
     * Use to login.
     *If username and password is blank then check validatelogin.
     * @param event an event?
     */
    public void loginButtonAction(ActionEvent event) {
        if (!UsernameField.getText().isBlank() && !PasswordField.getText().isBlank()) {
            ValidateLogin();
        } else {
            LoginMessageLabel.setText("Invalid login. Please try again!!!");
        }
    }

    /**
     * connect to the database,
     * if there is only 1 username and password can count in the database then login successful.
     */
    public void ValidateLogin() {
        databaseConnection.connect(); // Connect before calling getConnection
        Connection con = databaseConnection.getConnection();

        if (con == null) {
            LoginMessageLabel.setText("Could not connect to the database.");
            return; // Exit if cannot connect
        }
        // Check if username and password match data in mysql
        String verifyLogin = "SELECT count(1) FROM login.user_account WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(verifyLogin);
            preparedStatement.setString(1, UsernameField.getText());
            preparedStatement.setString(2, PasswordField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();
            if (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    LoginMessageLabel.setText("Login Successful");
                    loadLibraryView(); // Call the method to load the new view
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
            // Load the FXML for library view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchbook-view.fxml"));
            Parent searchView = loader.load();

            // Get the current stage
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(searchView));
            stage.setTitle("Search Book View");
            stage.show(); // Display the new scene

        } catch (IOException e) {
            e.printStackTrace();
            LoginMessageLabel.setText("Error loading search view: " + e.getMessage());
        }
    }

    //when u clink the hyperlink
    public void onHyperLinkClick(ActionEvent event) {
        // Call the method to load the registration view
        loadRegisterView(event);
    }

    // Load the registration view
    private void loadRegisterView(ActionEvent event) {
        try {
            // Load register-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/register-view.fxml"));
            Parent registerView = loader.load();

            // Get the current stage
            Stage stage = (Stage) RegisterLink.getScene().getWindow();
            stage.setScene(new Scene(registerView));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LoginMessageLabel.setText("Error loading registration view: " + e.getMessage());
        }
    }
}
