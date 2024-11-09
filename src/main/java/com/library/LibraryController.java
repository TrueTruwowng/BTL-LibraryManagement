package com.library;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {
    @FXML
    public JFXButton addBookButton;
    @FXML
    public Hyperlink deleteHyperlink;
    @FXML
    private TableView<Book> tableBookView;
    @FXML
    private TableColumn<Book, CheckBox> checkBoxBookColumn;
    @FXML
    private TableColumn<Book, String> bookIsbnColumn;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, String> bookYearColumn;
    @FXML
    private TableColumn<Book, Integer> bookPublisherIdColumn;
    @FXML
    private TableColumn<Book, Integer> bookQuantityColumn;
    @FXML
    private TableColumn<Book, Integer> bookCategoryColumn;
    @FXML
    private TableColumn<Book, String> bookAvailableColumn;

    @FXML
    private TextField bookSearchTextField;
    @FXML
    private FontAwesomeIcon searchIcon;

    ObservableList<Book> bookObservableList = FXCollections.observableArrayList();
    ObservableList<Book> suggestedBookObservableList = FXCollections.observableArrayList();

    @FXML
    private ProgressBar progressBar;
    @FXML
    private CheckBox checkAllBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
