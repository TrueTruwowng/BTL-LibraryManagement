package com.library;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private String correctUsername = "admin";
    private String correctPassword = "1234";

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals(correctUsername) && password.equals(correctPassword)) {
            // Switch to library scene
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("library-view.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) throws IOException {
        // Switch to register scene
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
