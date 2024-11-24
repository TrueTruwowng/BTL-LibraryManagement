package com.library.Controller;

import com.library.Book;
import com.library.LibraryApplication;
import com.library.User;
import com.library.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MyCollectionController extends SceneController {
    @FXML
    private VBox historyVbox;
    @FXML
    private ImageView smallUserImageView;
    @FXML
    private Label username;
    @FXML
    private Label bookRead;
    @FXML
    private Label bookBorrow;
    @FXML
    private ScrollPane borrowingScrollPane;
    @FXML
    private HBox borrowingContainer;

    @FXML
    public void initialize() throws SQLException {
        User currentUser = UserController.getCurrentUser();
        if (currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }
        username.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        bookRead.setText(DatabaseConnection.countBooksRead(currentUser.getUserID()));
        bookBorrow.setText(DatabaseConnection.countBooksBorrowing(currentUser.getUserID()));
        historyVbox.getStylesheets().add(getClass().getResource("/ScreenUI/css/tableStyle.css").toExternalForm());

        List<Book> borrowingBooks = DatabaseConnection.borrowingBookList(currentUser.getUserID());
        loadBorrowingBooks(borrowingBooks);
        displayBorrowHistory();
    }

    private void loadBorrowingBooks(List<Book> borrowingBooks) {
        borrowingContainer.getChildren().clear();

        for (Book book : borrowingBooks) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/library/Book-view.fxml"));
                VBox bookBox = fxmlLoader.load();

                BookController bookController = fxmlLoader.getController();
                bookController.setData(book);

                borrowingContainer.getChildren().add(bookBox);
                HBox.setMargin(bookBox, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        borrowingScrollPane.setContent(borrowingContainer);
    }

    public void displayBorrowHistory() throws SQLException {
        User currentUser = UserController.getCurrentUser();
        List<Map<String, Object>> historyList = DatabaseConnection.getBorrowHistory(currentUser.getUserID());

        historyVbox.getChildren().clear();

        HBox headerRow = createTableRow("STT", "Borrow ID", "Title", "Borrow Date", "Return Date", true);
        historyVbox.getChildren().add(headerRow);

        for (Map<String, Object> row : historyList) {
            HBox dataRow = createTableRow(
                    row.get("stt").toString(),
                    row.get("borrow_id").toString(),
                    row.get("title").toString(),
                    row.get("borrow_date").toString(),
                    row.get("return_date").toString(),
                    false
            );
            historyVbox.getChildren().add(dataRow);
        }
    }

    private HBox createTableRow(String stt, String borrowId, String title, String borrowDate, String returnDate, boolean isHeader) {
        HBox row = new HBox();
        row.setSpacing(10);
        row.setPadding(new Insets(5));
        row.getStylesheets().add(getClass().getResource("/ScreenUI/css/tableStyle.css").toExternalForm());

        row.getStyleClass().add(isHeader ? "table-header" : "table-row");

        Label sttLabel = createTableCell(stt, isHeader);
        Label borrowIdLabel = createTableCell(borrowId, isHeader);
        Label titleLabel = createTableCell(title, isHeader);
        Label borrowDateLabel = createTableCell(borrowDate, isHeader);
        Label returnDateLabel = createTableCell(returnDate, isHeader);

        sttLabel.setAlignment(Pos.CENTER);
        borrowIdLabel.setAlignment(Pos.CENTER);
        titleLabel.setAlignment(Pos.CENTER);
        borrowDateLabel.setAlignment(Pos.CENTER);
        returnDateLabel.setAlignment(Pos.CENTER);

        row.getChildren().addAll(sttLabel, borrowIdLabel, titleLabel, borrowDateLabel, returnDateLabel);
        return row;
    }

    private Label createTableCell(String text, boolean isHeader) {
        Label label = new Label(text);
        label.setPrefWidth(150);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add(isHeader ? "table-header-label" : "table-row-label");

        return label;
    }

    @FXML
    public void onDashboardBtnClick() {
        LibraryApplication.getSceneController().loadDashboardView();
    }

    @FXML
    public void onSettingsBtnClick() {
        LibraryApplication.getSceneController().loadSettingView();
    }

    @FXML
    public void onMyCollectionBtnClick() {
        LibraryApplication.getSceneController().loadMyCollectionView();
    }

    @FXML
    public void onLogOutBtnClick() {
        LibraryApplication.getSceneController().loadLoginView();
    }

    @FXML
    public void onGameBtnClick() {
        LibraryApplication.getSceneController().loadGameView();
    }
}
