package com.library;

public class Admin extends User {
    Admin(String userID, String firstname, String lastname, String username, byte[] userPicture, String password, String email, String phone) {
        super(userID, firstname, lastname, username, userPicture, password, email, phone);
    }
}
