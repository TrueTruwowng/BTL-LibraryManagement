package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection con;

    public void connect() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/login"; // Create connection
            String user = "root"; // Tên người dùng của bạn
            String password = "Chuanhtruong2005"; // Mật khẩu của bạn

            con = DriverManager.getConnection(url, user, password); //start to connect
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
            con = null; // Đặt con thành null nếu không kết nối được
        }
    }

    public Connection getConnection() {
        return con;
    }




    private Connection connectBookDatabase;

    public void connectBookDatabase() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/searchbook";
            String user = "root";
            String password = "nongsontung@24";
            connectBookDatabase = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
            connectBookDatabase = null;
        }
    }

    public Connection getBookDatabase() {
        return connectBookDatabase;
    }
}
