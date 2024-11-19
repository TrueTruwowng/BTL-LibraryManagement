package com.library;

public class User {
    private  String userID;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private byte[] userPicture;
    private String email;
    private String phone;

    User(String userID, String firstname, String lastname, String username, byte[] userPicture, String password, String email, String phone) {
        this.userID = userID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.userPicture = userPicture;
        this.email = email;
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public byte[] getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(byte[] userPicture) {
        this.userPicture = userPicture;
    }
    public String toString(){
        return "User:\n" +"UserID: " + userID +
                "\nUsername: " + username
                + "\nFirstname: " + firstname +
                "\nLastname: " + lastname +
                "\nPictureID: " + userPicture
                + "\nEmail: " + email
                + "\nPhone: " + phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
