package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
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

            // Tạo cột
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

        // If no suggestions are found locally, fetch from Google Books API
        if (suggestionList.isEmpty()) {
            suggestionList.addAll(fetchBooksFromAPI(keyword));
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

    private ObservableList<String> fetchBooksFromAPI(String keyword) {
        ObservableList<String> apiSuggestions = FXCollections.observableArrayList();
        try {
            String apiKey = API.getApiKey(); // Assume Config is a class that stores API key
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + keyword + "&key=" + apiKey;
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Parse JSON response
            JSONObject json = new JSONObject(content.toString());
            JSONArray items = json.optJSONArray("items");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String title = volumeInfo.optString("title", "No Title");
                    String description = volumeInfo.optString("description", "No Description");
                    String ISBN = volumeInfo.optJSONArray("industryIdentifiers") != null ?
                            volumeInfo.getJSONArray("industryIdentifiers").getJSONObject(0).getString("identifier") : "No ISBN";

                    // Add book title to suggestion list
                    apiSuggestions.add(title);

                    // Optionally add the book to TableView for display
                    bookObservableList.add(new Book(ISBN, title, description, 0, "", 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiSuggestions;
    }
}
