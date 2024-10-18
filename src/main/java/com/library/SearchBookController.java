package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchBookController implements Initializable {
    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, String> bookISBNtableColumn;

    @FXML
    private TableColumn<Book, String> titleTableColumn;

    @FXML
    private TableColumn<Book, String> descriptionTableColumn;

    @FXML
    private TableColumn<Book, Integer> categoryTableColumn;

    @FXML
    private TableColumn<Book, String> editionTableColumn;

    @FXML
    private TableColumn<Book, Integer> publisherIdTableColumn;

    @FXML
    private TextField keywordTextFeild;

    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    void search(ActionEvent event) {
        suggestionsListView.getItems().clear();
        suggestionsListView.getItems().addAll(handleSearch(keywordTextFeild.getText()));
    }

    private ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    private ObservableList<String> suggestionList = FXCollections.observableArrayList();
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        databaseConnection.connectBookDatabase();
        Connection connectDB = databaseConnection.getBookDatabase();

        if (connectDB == null) {
            System.out.println("Database connection failed.");
            return;
        }

        String bookViewQuery = "SELECT ISBN, Title, Description, Category, Edition, PublisherID FROM demobook";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(bookViewQuery);

            while (queryOutput.next()) {
                String queryISBN = queryOutput.getString("ISBN");
                String queryTitle = queryOutput.getString("Title");
                String queryDescription = queryOutput.getString("Description");
                Integer queryCategory = queryOutput.getInt("Category");
                String queryEdition = queryOutput.getString("Edition");
                Integer queryPublisherID = queryOutput.getInt("PublisherID");

                bookObservableList.add(new Book(queryISBN, queryTitle, queryDescription, queryCategory, queryEdition, queryPublisherID));
            }

            //Tạo cột
            bookISBNtableColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            editionTableColumn.setCellValueFactory(new PropertyValueFactory<>("edition"));
            publisherIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("publisherId"));

            bookTableView.setItems(bookObservableList);

            // Add listener for search bar input
            keywordTextFeild.textProperty().addListener((observable, oldValue, newValue) -> handleSearch(newValue));

            // Hide suggestion list by default
            suggestionsListView.setVisible(false);

        } catch (SQLException e) {
            Logger.getLogger(SearchBookController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    private String handleSearch(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            suggestionsListView.setVisible(false); // Hide suggestions if search is empty
            return keyword;
        }

        // Filter based on the keyword and update suggestion list
        suggestionList.clear();
        for (Book book : bookObservableList) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                suggestionList.add(book.getTitle());
            }
        }

        // If there are suggestions, show the ListView, otherwise hide it
        if (suggestionList.isEmpty()) {
            suggestionsListView.setVisible(false);
        } else {
            suggestionsListView.setItems(suggestionList);
            suggestionsListView.setVisible(true);
        }

        // Add click listener for selecting a suggestion
        suggestionsListView.setOnMouseClicked(event -> {
            String selectedTitle = suggestionsListView.getSelectionModel().getSelectedItem();
            keywordTextFeild.setText(selectedTitle);
            suggestionsListView.setVisible(false);
        });
        return keyword;
    }
}
