package com.library.admin;

import com.jfoenix.controls.JFXButton;
import com.library.User;
import com.library.databaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.library.Controller.sceneController.loadAdminLibraryScene;
import static com.library.Controller.sceneController.loadLoginView;

public class adminController implements Initializable {
    @FXML
    public Hyperlink bookSceneHyperlink;
    @FXML
    public JFXButton updateButton;
    @FXML
    public ContextMenu selectUserContext;
    @FXML
    public MenuItem selectMenu;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Hyperlink logoutHyperLink;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField userEmailTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField userPhoneTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private ComboBox<String> searchComboBox;

    public TableView<User> user_tableView;
    public TableColumn<User, String> userId;
    public TableColumn<User, String> userFname;
    public TableColumn<User, String> userLname;
    public TableColumn<User, String> userName;
    public TableColumn<User, String> userPassword;
    public TableColumn<User, String> userEmail;
    public TableColumn<User, String> userPhone;

    ObservableList<User> user_data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initColumns();
        databaseConnection.connectUserAccount();
        loadUsers();
    }

    private void initColumns() {
        userId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userFname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        userLname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        userName.setCellValueFactory(new PropertyValueFactory<>("username"));
        userPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void loadUsers() {
        // Kết nối tới database
        Connection connection = databaseConnection.getConnection();
        if (connection == null) {
            System.out.println("Kết nối database thất bại");
            showAlert("Thông báo", "Kết nối thất bại", Alert.AlertType.ERROR);
            return;
        }

        //Lấy dữ liệu từ database
        String sqlite = "SELECT * FROM user_account";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlite);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("account_id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        null,
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                user_data.add(user);
            }
            user_tableView.setItems(user_data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveUser(ActionEvent event) {
        // Kiểm tra đầu vào và mật khẩu khớp
        if (validateInput() && validatePasswords()) {
            // Lấy giá trị từ các TextField
            String username = userNameTextField.getText();
            String email = userEmailTextField.getText();
            String phone = userPhoneTextField.getText();
            String password = newPasswordTextField.getText();

            // Tạo câu lệnh SQL để chèn dữ liệu vào bảng "users"
            String sql = "INSERT INTO user_account (account_id, firstname, lastname, username, password, userPicture, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String accountId = generateUserId();

                preparedStatement.setString(1, accountId);
                preparedStatement.setString(2, "");      // firstname
                preparedStatement.setString(3, "");         // lsatname
                preparedStatement.setString(4, username);      // username
                preparedStatement.setString(5, password);      // password
                preparedStatement.setString(6, null);       // userpictrue
                preparedStatement.setString(7, email);         // email
                preparedStatement.setString(8, phone);         // phone

                // Thực hiện câu lệnh chèn
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    // Thêm người dùng vào danh sách và cập nhật bảng
                    User newUser = new User(accountId, "", "", username, null, password, email, phone);
                    user_data.add(newUser);
                    user_tableView.setItems(user_data);
                    clearFields();
                    showAlert("Success", "User added successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to add user.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please ensure all fields are correctly filled.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void updateUser(ActionEvent event) {
        User selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && validateInput() && validatePasswords()) {
            // Lấy giá trị từ các TextField
            String username = userNameTextField.getText();
            String email = userEmailTextField.getText();
            String phone = userPhoneTextField.getText();
            String password = newPasswordTextField.getText();

            // Kiểm tra username
            String checkUsernameQuery = "SELECT account_id FROM user_account WHERE username = ? AND account_id != ?";
            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement checkStatement = connection.prepareStatement(checkUsernameQuery)) {

                checkStatement.setString(1, username);
                checkStatement.setString(2, selectedUser.getUserID());

                ResultSet rs = checkStatement.executeQuery();
                if (rs.next()) {
                    // Nếu trùng username
                    showAlert("Error", "Username already exists. Please choose another one.", Alert.AlertType.ERROR);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }

            String sqlite = "UPDATE user_account SET  username = ?, email = ?, phone = ?, password = ? WHERE account_id = ? OR username = ?";

            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlite)) {

                preparedStatement.setString(5, selectedUser.getUserID());
                preparedStatement.setString(1, username);      // username
                preparedStatement.setString(4, password);      // password
                preparedStatement.setString(2, email);         // email
                preparedStatement.setString(3, phone);         // phone

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    selectedUser.setUsername(username);
                    selectedUser.setEmail(email);
                    selectedUser.setPhone(phone);
                    selectedUser.setPassword(password);

                    user_tableView.refresh();
                    clearFields();
                    showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to update user.", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Please select a user and ensure all fields are correctly filled.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        User selectedUser = user_tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this user?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Xóa người dùng từ database
                String sql = "DELETE FROM user_account WHERE username = ?";
                try (Connection connection = databaseConnection.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                    preparedStatement.setString(1, selectedUser.getUsername());
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Xóa người dùng khỏi bảng
                        user_data.remove(selectedUser);
                        user_tableView.refresh();
                        showAlert("Success", "User deleted successfully!", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "Failed to delete user from database.", Alert.AlertType.ERROR);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Database error: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Warning", "Please select a user to delete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void cancelButton(ActionEvent event) {
        clearFields();
    }

    @FXML
    private void search(KeyEvent event) {
        String searchText = searchTextField.getText().toLowerCase();
        ObservableList<User> filteredList = FXCollections.observableArrayList();
        for (User user : user_data) {
            if (user.getUsername().toLowerCase().contains(searchText)) {
                filteredList.add(user);
            }
        }
        user_tableView.setItems(filteredList);
    }

    @FXML
    private void deleteSelectedUsers(ActionEvent event) {
        ObservableList<User> selectedUsers = user_tableView.getSelectionModel().getSelectedItems();

        if (!selectedUsers.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete selected users?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (Connection con = databaseConnection.getConnection()) {
                    String query = "DELETE FROM user_account WHERE account_id = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);

                    for (User user : selectedUsers) {
                        preparedStatement.setString(1, user.getUserID());
                        preparedStatement.executeUpdate();
                    }

                    // Xóa khỏi danh sách hiển thị
                    user_data.removeAll(selectedUsers);

                    showAlert("Success", "Selected users have been deleted.", Alert.AlertType.INFORMATION);
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to delete users.", Alert.AlertType.ERROR);
                }            }
        } else {
            showAlert("Warning", "No users selected.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void requestMenu(ContextMenuEvent event) {
        if (!user_tableView.getSelectionModel().isEmpty()) {
            selectUserContext.show(user_tableView, event.getScreenX(), event.getScreenY());
        }
    }

    private boolean validateInput() {
        return !userNameTextField.getText().isEmpty() &&
                !userEmailTextField.getText().isEmpty() &&
                !userPhoneTextField.getText().isEmpty() &&
                !newPasswordTextField.getText().isEmpty() &&
                !confirmPasswordTextField.getText().isEmpty();
    }

    private boolean validatePasswords() {
        if (!newPasswordTextField.getText().equals(confirmPasswordTextField.getText())) {
            showAlert("Password Mismatch", "Passwords do not match.", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private String generateUserId() {
        return UUID.randomUUID().toString(); // Simple user ID generator
    }

    private void clearFields() {
        userNameTextField.clear();
        userEmailTextField.clear();
        userPhoneTextField.clear();
        newPasswordTextField.clear();
        confirmPasswordTextField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onAdminLibraryHyperLinkClicked() {
        Stage stage = (Stage) bookSceneHyperlink.getScene().getWindow();
        loadAdminLibraryScene(stage);
    }

    public void backToLogin() {
        Stage stage = (Stage) logoutHyperLink.getScene().getWindow();
        loadLoginView(stage);
    }
}
