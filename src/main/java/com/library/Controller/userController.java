package com.library.Controller;

import com.library.User;
import com.library.databaseConnection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class userController {
    static User currentUser;

    // Getter và setter cho currentUser
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Cập nhật mật khẩu người dùng
    public static void updateUserInfo(String newFirstname, String newLastname, String newEmail, String newPhone, String newPassword) throws SQLException {
        if (currentUser != null) {
            currentUser.setFirstname(newFirstname);
            currentUser.setLastname(newLastname);
            currentUser.setEmail(newEmail);
            currentUser.setPhone(newPhone);
            currentUser.setPassword(newPassword);

            // Gọi phương thức updateUserInfo trong DatabaseConnection
            databaseConnection.updateUserInfo(currentUser.getUserID(), newFirstname, newLastname, newEmail, newPhone, newPassword);
        } else {
            throw new IllegalStateException("No user is currently logged in");
        }
    }


    // Cập nhật ảnh người dùng
    public static void updateUserPicture(byte[] newImageBytes) throws IOException, SQLException {
        if (currentUser != null) {
            currentUser.setUserPicture(newImageBytes);  // Cập nhật ảnh trong đối tượng User
            databaseConnection.updateUserPicture(currentUser.getUserID(), newImageBytes);  // Cập nhật ảnh trong DB
        } else {
            throw new IllegalStateException("No user is currently logged in");
        }
    }

    // Đọc ảnh từ thư mục resources và trả về dưới dạng byte array
    public static byte[] getDefaultImageBytes(String resourcePath) throws IOException {
        try (InputStream inputStream = userController.class.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                return inputStream.readAllBytes();
            } else {
                throw new IOException("Image not found in resources: " + resourcePath);
            }
        }
    }
}
