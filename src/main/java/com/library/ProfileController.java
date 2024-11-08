package com.library;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    public TableView<User> user_tableView;
    public TableColumn<User, String> userId;
    public TableColumn<User, String> userFname;
    public TableColumn<User, String> userLname;
    public TableColumn<User, String> userName;
    public TableColumn<User, String> userPhonenumber;
    public TableColumn<User, String> userEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
