package com.library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public static void updateUserPicture(String userID, byte[] newImageBytes) throws SQLException {
        // Kiểm tra kết nối và mở lại nếu cần
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String updateQuery = "UPDATE user_account SET userPicture = ? WHERE account_id = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
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

    public static void updateUserInfo(String userID, String newFirstname, String newLastname, String newEmail, String newPhone, String newPassword) throws SQLException {
        // Kiểm tra kết nối và mở lại nếu cần
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String updateSQL = "UPDATE user_account SET firstname = ?, lastname = ?, email = ?, phone = ?, password = ? WHERE account_id = ?";
        try (PreparedStatement statement = con.prepareStatement(updateSQL)) {
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

    public static void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    public static List<Book> getBooks() throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }
        String sql = "SELECT title, author, bookImage FROM book_info"; // Truy vấn SQL để lấy title, author và bookImage
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql); // Kết nối đến cơ sở dữ liệu
             ResultSet resultSet = preparedStatement.executeQuery()) { // Thực thi truy vấn

            while (resultSet.next()) {
                Book book = new Book(); // Tạo đối tượng Book mới
                book.setTitle(resultSet.getString("title")); // Lấy title từ ResultSet
                book.setAuthor(resultSet.getString("author")); // Lấy author từ ResultSet
                book.setImageSrc(resultSet.getString("bookImage")); // Lấy bookImage từ ResultSet
                books.add(book); // Thêm đối tượng Book vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            closeConnection();
        }
        return books; // Trả về danh sách sách
    }
}
