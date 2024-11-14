package com.library;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class EditController {
    private static User currentUser;
    @FXML
    private TextField newFirstname;
    @FXML
    private TextField newLastname;
    @FXML
    private TextField newEmail;
    @FXML
    private TextField newPhone;
    @FXML
    private TextField newPassword;
    @FXML
    private Button saveButton;
    public void initialize() {
        currentUser = UserController.getCurrentUser();
        newFirstname.setText(currentUser.getFirstname());
        newLastname.setText(currentUser.getLastname());
        newEmail.setText(currentUser.getEmail());
        newPhone.setText(currentUser.getPhone());

    }

    public void handleUserUpdateInfo() throws SQLException {
        String firstname = newFirstname.getText();
        String lastname = newLastname.getText();
        String email = newEmail.getText();
        String phone = newPhone.getText();
        String password = newPassword.getText();
        if (currentUser != null) {
            UserController.updateUserInfo(firstname,lastname,email,phone,password);
            System.out.println("Button clicked"); // Để kiểm tra sự kiện onAction hoạt động

            Platform.runLater(() -> {
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "User info updated successfully.");
                Platform.exit();
            });
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleCancelButton() {
        Platform.exit();
    }
}
