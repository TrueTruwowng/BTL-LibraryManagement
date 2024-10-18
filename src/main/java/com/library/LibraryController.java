package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LibraryController {
    @FXML
    private TableView<Book> tableBooks;

    @FXML
    private TableColumn<Book, String> colISBN;

    @FXML
    private TableColumn<Book, String> colTitle;

    @FXML
    private TableColumn<Book, String> colDescription;

    @FXML
    private TableColumn<Book, Integer> colCategory;

    @FXML
    private TableColumn<Book, String> colEdition;

    @FXML
    private TableColumn<Book, Integer> colPublisherId;

    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtCategory;

    @FXML
    private TextField txtEdition;

    @FXML
    private TextField txtPublisherId;

    @FXML
    public void initialize() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        colPublisherId.setCellValueFactory(new PropertyValueFactory<>("publisherId"));

        // Dữ liệu mẫu
        tableBooks.getItems().add(new Book("1000", "Clean Code", "Robert C. Martin", 1, "1st", 1));
        tableBooks.getItems().add(new Book("1001", "Head First Java", "Kathy Sierra", 2, "1st", 2));
    }

    @FXML
    public void handleAddBook() {
        String isbn = txtISBN.getText();
        String title = txtTitle.getText();
        String description = txtDescription.getText();
        int category;
        int publisherId;

        try {
            category = Integer.parseInt(txtCategory.getText());
            publisherId = Integer.parseInt(txtPublisherId.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Category and Publisher ID must be numbers");
            return;
        }

        if (isbn.isEmpty() || title.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields");
            return;
        }

        Book newBook = new Book(isbn, title, description, category, "", publisherId);
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
        txtISBN.clear();
        txtTitle.clear();
        txtDescription.clear();
        txtCategory.clear();
        txtEdition.clear();
        txtPublisherId.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
