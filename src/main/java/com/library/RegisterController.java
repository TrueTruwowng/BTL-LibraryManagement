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

public class RegisterController {
    @FXML
    private TextField txtNewUsername;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    public void handleRegister(ActionEvent event) {
        String newUsername = txtNewUsername.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Welcome " + newUsername);
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Passwords do not match");
        }
    }

    @FXML
    public void handleBackToLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) txtNewUsername.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
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
