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
        // Truy vấn SQL để lấy tất cả các thông tin sách
        String sql = "SELECT isbn, title, author, year, description, available, bookImage FROM book_info";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) { // Thực thi truy vấn

            while (resultSet.next()) {
                Book book = new Book(); // Tạo đối tượng Book mới

                // Lấy dữ liệu từ ResultSet và gán vào đối tượng Book
                book.setISBN(resultSet.getString("isbn"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));
                book.setDescription(resultSet.getString("description"));
                book.setAvailable(resultSet.getInt("available"));

                // Lấy bookImage dưới dạng byte array từ ResultSet
                byte[] imageBytes = resultSet.getBytes("bookImage");
                if (imageBytes != null) {
                    book.setImageSrc(imageBytes); // Gán giá trị bookImage cho đối tượng Book
                }

                books.add(book); // Thêm đối tượng Book vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            closeConnection();
        }
        return books; // Trả về danh sách sách
    }

    public static Book getBookByISBN(String isbn) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount(); // Đảm bảo kết nối với cơ sở dữ liệu
        }
        String sql = "SELECT title, author, bookImage FROM book_info"; // Truy vấn SQL để lấy title, author và bookImage
        List<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = con.prepareStatement(sql); // Kết nối đến cơ sở dữ liệu
             ResultSet resultSet = preparedStatement.executeQuery()) { // Thực thi truy vấn

            while (resultSet.next()) {
                Book book = new Book(); // Tạo đối tượng Book mới
                book.setTitle(resultSet.getString("title")); // Lấy title từ ResultSet
                book.setAuthor(resultSet.getString("author")); // Lấy author từ ResultSet

                // Lấy bookImage dưới dạng byte array từ ResultSet
                byte[] imageBytes = resultSet.getBytes("bookImage");
                if (imageBytes != null) {
                    book.setImageSrc(imageBytes); // Gán giá trị bookImage cho đối tượng Book
                }

                books.add(book); // Thêm đối tượng Book vào danh sách
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            closeConnection();
        }
        return books; // Trả về danh sách sách
    }


        String sql = "SELECT title, author, year, bookImage, available, isbn, description FROM book_info WHERE isbn = ?";
        Book book = null;

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn); // Đặt giá trị ISBN vào câu lệnh truy vấn
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setTitle(resultSet.getString("title")); // Lấy title
                    book.setAuthor(resultSet.getString("author")); // Lấy author
                    book.setYear(resultSet.getInt("year")); // Lấy year
                    book.setImageSrc(resultSet.getBytes("bookImage")); // Lấy bookImage
                    book.setAvailable(resultSet.getInt("available")); // Lấy available
                    book.setISBN(resultSet.getString("isbn")); // Lấy isbn
                    book.setDescription(resultSet.getString("description")); // Lấy description
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi nếu có
        } finally {
            closeConnection(); // Đóng kết nối sau khi hoàn thành
        }

        return book; // Trả về đối tượng Book (null nếu không tìm thấy)
    }

public static List<Book> searchBooks(String searchQuery) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String sql = "SELECT * FROM book_info WHERE title LIKE ? OR author LIKE ?";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));

                // Handle the image
                byte[] imageBytes = resultSet.getBytes("bookImage");
                if (imageBytes != null) {
                    book.setImageSrc(imageBytes);
                }
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>(); 
        }
        return books;
    }
}
