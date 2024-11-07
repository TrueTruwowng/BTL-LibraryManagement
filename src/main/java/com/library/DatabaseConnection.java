package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection con;

    public static void connectUserAccount() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/loginschema"; // Create connection
            String user = "root"; // Tên người dùng của bạn
            String password = "Chuanhtruong2005"; // Mật khẩu của bạn

            con = DriverManager.getConnection(url, user, password); //start to connect
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
