package com.library;

import javafx.scene.control.CheckBox;

public class User {
    private CheckBox checkBox;
    private int userID;
    private String userFName;
    private String userLName;
    private String userName;
    private String password;
    private String email;
    private String phone;

    public User(CheckBox checkBox, int userID, String userFName, String userLName, String userName, String password, String email, String phone) {
        this.checkBox = checkBox;
        this.userID = userID;
        this.userFName = userFName;
        this.userLName = userLName;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public String getUserFName() {
        return userFName;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserLName() {
        return userLName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
