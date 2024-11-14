package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection con;

    public static void connectUserAccount() {
        try {
            String url = "jdbc:sqlite:/Users/sontung/Documents/BTL-LibraryManagement/src/main/resources/database/userInfo.db"; // Create connection

            con = DriverManager.getConnection(url); //start to connect
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
            con = null; // Đặt con thành null nếu không kết nối được
        }
    }

    public static Connection getConnection() {
        return con;
    }
    public static void updateUserPicture(String userID, byte[] newImageBytes) throws SQLException {
        // Kiểm tra kết nối và mở lại nếu cần
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String updateQuery = "UPDATE user_account SET userPicture = ? WHERE account_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setBytes(1, newImageBytes); // Đặt ảnh mới vào cột userPicture
            preparedStatement.setString(2, userID); // Đặt userID để tìm đúng người dùng

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User picture updated successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUserInfo(String userID, String newFirstname, String newLastname, String newEmail, String newPhone, String newPassword) {
        String updateSQL = "UPDATE user_account SET firstname = ?, lastname = ?, email = ?, phone = ?, password = ? WHERE account_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(updateSQL)) {
            statement.setString(1, newFirstname);
            statement.setString(2, newLastname);
            statement.setString(3, newEmail);
            statement.setString(4, newPhone);
            statement.setString(5, newPassword);  // Đặt newPassword vào vị trí thứ 5
            statement.setString(6, userID);       // Đặt userID vào vị trí thứ 6

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully!");
            } else {
                System.out.println("User not found or no update performed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}