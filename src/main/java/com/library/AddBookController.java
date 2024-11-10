package com.library;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    @FXML
    private TextField bookIsbnTextField;
    @FXML
    private TextField bookTitleTextField;
    @FXML
    private TextField bookAuthorTextField;
    @FXML
    private TextField bookYearTextField;
    @FXML
    private TextField bookPublisherIdTextField;
    @FXML
    private TextField bookQuantityTextField;

    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    //public boolean isEdit = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
