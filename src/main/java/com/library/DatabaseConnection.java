package com.library;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class DatabaseConnection {
    private static Connection con;

    public static void connectUserAccount() {
        try {
            String relativePath = "src/main/resources/database/userInfo.db";
            String url = "jdbc:sqlite:" + new File(relativePath).getAbsolutePath();//chỉnh de k phai lay duong dan tai cac may khac

            con = DriverManager.getConnection(url); //start to connect
            System.out.println("Connected to database");
        } catch (SQLException e) {
            e.printStackTrace();
            con = null;
        }
    }

    public static Connection getConnection() {
        try {
            // Nếu kết nối bị đóng, kết nối bằng url thay vì gọi phương thức connectUserAccount
            // (hạn chế hiện "Connected to database" ở connectUserAccount mỗi khi muốn kết nối)
            if (con == null || con.isClosed()) {
                String relativePath = "src/main/resources/database/userInfo.db";
                String url = "jdbc:sqlite:" + new File(relativePath).getAbsolutePath();//chỉnh de k phai lay duong dan tai cac may khac
                con = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void updateUserPicture(String userID, byte[] newImageBytes) throws SQLException {
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
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String updateSQL = "UPDATE user_account SET firstname = ?, lastname = ?, email = ?, phone = ?, password = ? WHERE account_id = ?";
        try (PreparedStatement statement = con.prepareStatement(updateSQL)) {
            statement.setString(1, newFirstname);
            statement.setString(2, newLastname);
            statement.setString(3, newEmail);
            statement.setString(4, newPhone);
            statement.setString(5, newPassword);
            statement.setString(6, userID);

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
        String sql = "SELECT isbn, title, author, year, description, available, bookImage FROM book_info";
        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();

                book.setIsbn(resultSet.getString("isbn"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));
                book.setDescription(resultSet.getString("description"));
                book.setAvailable(resultSet.getInt("available"));

                byte[] imageBytes = resultSet.getBytes("bookImage");
                if (imageBytes != null) {
                    book.setBookImage(imageBytes);
                }

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    static Book getBookByISBN(String isbn) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String sql = "SELECT title, author, year, bookImage, available, isbn, description FROM book_info WHERE isbn = ?";
        Book book = null;

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, isbn); // Đặt giá trị ISBN vào câu lệnh truy vấn
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = new Book();
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setYear(resultSet.getInt("year"));
                    book.setBookImage(resultSet.getBytes("bookImage"));
                    book.setAvailable(resultSet.getInt("available"));
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setDescription(resultSet.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }


    static void borrowBookByTitle(String title, String accountId) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String getISBNQuery = "SELECT isbn FROM book_info WHERE title = ? AND available > 0";
        String updateBookInfo = "UPDATE book_info SET available = available - 1 WHERE isbn = ?";
        String insertBorrowedBooks = "INSERT INTO borrowed_books (borrow_id, account_id, isbn, borrow_date, return_date) VALUES (?, ?, ?, ?, NULL)";

        try (PreparedStatement getISBNStmt = con.prepareStatement(getISBNQuery);
             PreparedStatement updateStmt = con.prepareStatement(updateBookInfo);
             PreparedStatement insertStmt = con.prepareStatement(insertBorrowedBooks)) {

            getISBNStmt.setString(1, title);
            ResultSet rs = getISBNStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("No books found");
                return;
            }

            String isbn = rs.getString("isbn");

            // Cập nhật số lượng sách
            updateStmt.setString(1, isbn);
            updateStmt.executeUpdate();

            // Thêm bản ghi vào bảng borrowed_books
            String borrowId = UUID.randomUUID().toString();
            LocalDate borrowDate = LocalDate.now();

            insertStmt.setString(1, borrowId);
            insertStmt.setString(2, accountId);
            insertStmt.setString(3, isbn);
            insertStmt.setString(4, borrowDate.toString());
            insertStmt.executeUpdate();

            System.out.println("Borrow successfully");
        }
    }

    static void updateReturnDateByISBN(String isbn, String accountId, LocalDate returnDate) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String sql = "UPDATE borrowed_books " +
                "SET return_date = ? " +
                "WHERE isbn = ? AND account_id = ? AND return_date IS NULL";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, returnDate.toString());
            pstmt.setString(2, isbn);
            pstmt.setString(3, accountId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Successfully updated the return date");
            } else {
                System.out.println("Found no books to return");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean isBookBorrowedByUser(String isbn, String accountId) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String query = "SELECT COUNT(*) FROM borrowed_books WHERE isbn = ? AND account_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, isbn);
            stmt.setString(2, accountId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    static void updateAvailableBooks(String isbn, int quantityChange) throws SQLException {
        String query = "UPDATE book_info SET available = available + ? WHERE isbn = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantityChange);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    public static List<Book> searchBooks(String searchQuery) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        List<Book> books = new ArrayList<>();

        String sql = "SELECT isbn, title, author, year, description, available, bookImage FROM book_info " +
                "WHERE title LIKE ? OR author LIKE ? OR description LIKE ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            String searchTerm = "%" + searchQuery + "%";
            preparedStatement.setString(1, searchTerm);
            preparedStatement.setString(2, searchTerm);
            preparedStatement.setString(3, searchTerm);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();
                    book.setIsbn(resultSet.getString("isbn"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setYear(resultSet.getInt("year"));
                    book.setDescription(resultSet.getString("description"));
                    book.setAvailable(resultSet.getInt("available"));

                    byte[] imageBytes = resultSet.getBytes("bookImage");
                    if (imageBytes != null) {
                        book.setBookImage(imageBytes);
                    }

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public static String countBooksBorrowing(String accountId) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String query = "SELECT COUNT(*) FROM borrowed_books WHERE account_id = ? AND return_date IS NULL";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, accountId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    // Đếm số sách đã mượn và đã trả của người dùng
    public static String countBooksRead(String accountId) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String query = "SELECT COUNT(*) FROM borrowed_books WHERE account_id = ? AND return_date IS NOT NULL";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, accountId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
            return null;
        }
    }

    public static List<Book> borrowingBookList(String accountID) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String sql = "SELECT bi.isbn, bi.title, bi.author, bi.year, bi.description, bi.available, bi.bookImage " +
                "FROM book_info bi " +
                "JOIN borrowed_books bb ON bi.isbn = bb.isbn " +
                "WHERE bb.account_id = ? AND bb.return_date IS NULL";

        List<Book> books = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Book book = new Book();

                    book.setIsbn(resultSet.getString("isbn"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setYear(resultSet.getInt("year"));
                    book.setDescription(resultSet.getString("description"));
                    book.setAvailable(resultSet.getInt("available"));

                    byte[] imageBytes = resultSet.getBytes("bookImage");
                    if (imageBytes != null) {
                        book.setBookImage(imageBytes);
                    }

                    books.add(book);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while fetching borrowing book list", e);
        }

        return books;
    }
    public static List<Map<String, Object>> getBorrowHistory(String accountID) throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        String sql = "SELECT bb.borrow_id, bi.title, bb.borrow_date, bb.return_date " +
                "FROM borrowed_books bb " +
                "JOIN book_info bi ON bb.isbn = bi.isbn " +
                "WHERE bb.account_id = ?";

        List<Map<String, Object>> historyList = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, accountID);
            ResultSet resultSet = preparedStatement.executeQuery();

            int stt = 1; // Số thứ tự
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("stt", stt++);

                // Lấy borrow_id và title
                row.put("borrow_id", resultSet.getString("borrow_id"));
                row.put("title", resultSet.getString("title"));

                // Lấy ngày mượn (borrow_date) và chuyển đổi thành LocalDate
                String borrowDateStr = resultSet.getString("borrow_date");
                LocalDate borrowDate = null;
                if (borrowDateStr != null && !borrowDateStr.isEmpty()) {
                    borrowDate = LocalDate.parse(borrowDateStr);
                }
                row.put("borrow_date", borrowDate);

                // Lấy ngày trả (return_date) và chuyển đổi thành LocalDate (nếu có)
                String returnDateStr = resultSet.getString("return_date");
                LocalDate returnDate = null;
                if (returnDateStr != null && !returnDateStr.isEmpty()) {
                    returnDate = LocalDate.parse(returnDateStr);
                }
                row.put("return_date", returnDate != null ? returnDate : "");

                historyList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }

    public static List<Book> getMostBorrowedBooks() throws SQLException {
        if (con == null || con.isClosed()) {
            connectUserAccount();
        }

        List<Book> books = new ArrayList<>();

        // SQL query để lấy top 5 sách được mượn nhiều nhất
        String sql = "SELECT bi.isbn, bi.title, bi.author, bi.year, bi.description, bi.available, bi.bookImage, COUNT(bb.isbn) AS borrow_count " +
                "FROM borrowed_books bb " +
                "JOIN book_info bi ON bb.isbn = bi.isbn " +
                "GROUP BY bb.isbn " +
                "ORDER BY borrow_count DESC " +
                "LIMIT 5";

        try (PreparedStatement preparedStatement = con.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setIsbn(resultSet.getString("isbn"));
                book.setTitle(resultSet.getString("title"));
                book.setAuthor(resultSet.getString("author"));
                book.setYear(resultSet.getInt("year"));
                book.setDescription(resultSet.getString("description"));
                book.setAvailable(resultSet.getInt("available"));

                byte[] imageBytes = resultSet.getBytes("bookImage");
                if (imageBytes != null) {
                    book.setBookImage(imageBytes);
                }

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}