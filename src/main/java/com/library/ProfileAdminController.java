package com.library;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileAdminController implements Initializable {
    int select = 0;
    @FXML
    public JFXButton updateButton;
    @FXML
    public ContextMenu selectUserContext;
    @FXML
    public CheckBox checkAllUser;
    @FXML
    public MenuItem selectMenu;
    @FXML
    public ProgressBar progressBar;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private ComboBox<String> searchComboBox;

    public TableView<User> user_tableView;
    public TableColumn<User, CheckBox> checkUserColumn;
    public TableColumn<User, String> userId;
    public TableColumn<User, String> userFname;
    public TableColumn<User, String> userLname;
    public TableColumn<User, String> userName;
    public TableColumn<User, String> userPassword;

    ObservableList<User> user_data = FXCollections.observableArrayList();
    ObservableList<User> users_ = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
