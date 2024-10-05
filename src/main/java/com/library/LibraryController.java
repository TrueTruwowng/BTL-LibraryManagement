package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
//import com.library.Book;

public class LibraryController {
    @FXML
    private TableView<Book> tableBooks;

    @FXML
    private TableColumn<Book, String> colTitle;

    @FXML
    private TableColumn<Book, String> colAuthor;

    @FXML
    private TableColumn<Book, String> colYear;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtYear;

    @FXML
    public void initialize() {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Dữ liệu mẫu
        tableBooks.getItems().add(new Book("Clean Code", "Robert C. Martin", "2008"));
        tableBooks.getItems().add(new Book("Head First Java", "Kathy Sierra", "2005"));
    }

    @FXML
    public void handleAddBook() {
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String year = txtYear.getText();

        if (title.isEmpty() || author.isEmpty() || year.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields");
            return;
        }

        Book newBook = new Book(title, author, year);
        tableBooks.getItems().add(newBook);

        clearFields();
    }

    @FXML
    public void handleDeleteBook() {
        Book selectedBook = tableBooks.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            tableBooks.getItems().remove(selectedBook);
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a book to delete");
        }
    }

    private void clearFields() {
        txtTitle.clear();
        txtAuthor.clear();
        txtYear.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}