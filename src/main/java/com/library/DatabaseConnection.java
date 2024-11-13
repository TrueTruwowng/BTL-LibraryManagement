package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection con;

    public static void connectUserAccount() {
        try {
            String url = "jdbc:sqlite:D:/OOP/BTL-LibraryManagement/src/main/resources/database/userInfo.db"; // Create connection

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
}
