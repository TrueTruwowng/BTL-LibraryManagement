package com.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.library.SceneLoader.stage;

public class DashBoardController implements Initializable {
    @FXML
    private AnchorPane searchPane; // Bảng tìm kiếm chứa các thành phần tìm kiếm
    @FXML
    private TextField searchTextField; // Trường văn bản để nhập từ khóa tìm kiếm
    @FXML
    private Button searchButton; // Nút để kích hoạt hành động tìm kiếm

    @FXML
    private HBox cardLayout; // Bố cục cho các thẻ sách

    @FXML
    private VBox searchLayout; // Bố cục cho kết quả tìm kiếm

    @FXML
    private GridPane bookContainer; // Bố cục lưới để hiển thị sách

    @FXML
    private boolean isSearchPaneVisible = false; // Biến theo dõi trạng thái hiển thị của bảng tìm kiếm

    private List<Book> recentlyAdded; // Danh sách sách mới thêm
    private List<Book> recommended; // Danh sách sách được đề xuất
    private List<Book> searched; // Danh sách sách đã tìm kiếm

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button myCollectionBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private ImageView smallUserImageView;
    @FXML
    private Label username;

    @FXML
    // Phương thức xử lý hành động tìm kiếm
    private void searchBooks(ActionEvent event) {
        if (!isSearchPaneVisible) { // Kiểm tra nếu bảng tìm kiếm chưa hiển thị
            searchPane.setVisible(true); // Hiện bảng tìm kiếm
            searchPane.setManaged(true); // Quản lý bảng tìm kiếm
            isSearchPaneVisible = true; // Cập nhật trạng thái
        } else {
            searchPane.setVisible(false); // Ẩn bảng tìm kiếm
            searchPane.setManaged(false); // Ngừng quản lý bảng tìm kiếm
            isSearchPaneVisible = false; // Cập nhật trạng thái
        }

        // Lấy truy vấn tìm kiếm từ trường văn bản
        String searchQuery = searchTextField.getText();
        if (searchQuery == null || searchQuery.trim().isEmpty()) { // Kiểm tra truy vấn có hợp lệ không
            searchLayout.getChildren().clear(); // Xóa kết quả tìm kiếm trước đó
            return; // Kết thúc nếu không có truy vấn
        }

        List<Book> searchResults; // Danh sách kết quả tìm kiếm
        try {
            // Gọi phương thức tìm kiếm từ cơ sở dữ liệu
            searchResults = DatabaseConnection.searchBooks(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
            return; // Kết thúc nếu có lỗi
        }

        // Hiển thị kết quả tìm kiếm
        displaySearchResults(searchResults);
    }

    // Phương thức để hiển thị kết quả tìm kiếm
    private void displaySearchResults(List<Book> books) {
        searchLayout.getChildren().clear(); // Xóa kết quả trước đó
        if (books.isEmpty()) { // Kiểm tra nếu không có sách nào được tìm thấy
            Label noResultsLabel = new Label("Không tìm thấy kết quả."); // Tạo nhãn thông báo
            searchLayout.getChildren().add(noResultsLabel); // Thêm nhãn vào bố cục
            return; // Kết thúc nếu không có kết quả
        }

        try {
            // Duyệt qua danh sách sách và hiển thị từng sách
            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("search-view.fxml")); // Tải tệp FXML cho từng sách
                HBox searchBox = fxmlLoader.load(); // Tải HBox cho kết quả
                SearchController searchController = fxmlLoader.getController(); // Lấy controller cho HBox
                searchController.setData(book); // Thiết lập dữ liệu cho sách
                searchLayout.getChildren().add(searchBox); // Thêm HBox vào bố cục kết quả tìm kiếm
            }
        } catch (IOException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Kết nối đến cơ sở dữ liệu
        DatabaseConnection.connectUserAccount(); // Nếu cần thiết

        List<Book> allBooks = null;
        try {
            // Lấy tất cả sách từ cơ sở dữ liệu
            allBooks = DatabaseConnection.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e); // Kết thúc nếu có lỗi
        }

        // Khởi tạo danh sách sách mới thêm và sách được đề xuất
        recentlyAdded = new ArrayList<>(allBooks); // Giả sử bạn muốn hiển thị tất cả sách ở đây
        recommended = new ArrayList<>(allBooks); // Tương tự cho sách được đề xuất

        int column = 0; // Đếm số cột trong GridPane
        int row = 1; // Đếm số hàng trong GridPane

        try {
            // Hiển thị sách mới thêm
            for (Book value : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("card-view.fxml")); // Tải tệp FXML cho thẻ sách
                HBox cardBox = fxmlLoader.load(); // Tạo HBox cho thẻ sách
                CardController cardController = fxmlLoader.getController(); // Lấy controller cho thẻ sách
                cardController.setData(value); // Thiết lập dữ liệu cho thẻ sách
                cardLayout.getChildren().add(cardBox); // Thêm HBox vào cardLayout
            }

            // Hiển thị sách được đề xuất
            for (Book book : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Book-view.fxml")); // Tải tệp FXML cho sách
                VBox bookBox = fxmlLoader.load(); // Tạo VBox cho sách
                BookController bookController = fxmlLoader.getController(); // Lấy controller cho sách
                bookController.setData(book); // Thiết lập dữ liệu cho sách

                if (column == 6) { // Nếu đã đạt đến 6 cột
                    column = 0; // Reset lại cột
                    row++; // Chuyển sang hàng tiếp theo
                }

                bookContainer.add(bookBox, column++, row); // Thêm sách vào GridPane
                GridPane.setMargin(bookBox, new Insets(10)); // Thiết lập khoảng cách giữa các sách
            }

        } catch (IOException e) {
            throw new RuntimeException(e); // Kết thúc nếu có lỗi
        }
    }
    public void onDashboardBtnClick() {
        SceneLoader.handleDashboardButton(stage);
    }
    public void onSettingsBtnClick() {
        SceneLoader.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        SceneLoader.handleMyCollectionButton(stage);
    }
    public void onLogOutBtnClk() {
        SceneLoader.handleLogoutButton(stage);
    }
}
