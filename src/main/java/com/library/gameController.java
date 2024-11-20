package com.library;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class gameController implements Initializable {
    @FXML
    public Label questionLabel;
    @FXML
    public JFXButton option1;
    @FXML
    public JFXButton option2;
    @FXML
    public JFXButton option3;
    @FXML
    public JFXButton option4;
    @FXML
    public Hyperlink nextQuestionHyperlink;

    private List<Question> questions = new ArrayList<>();
    private int score = 0;
    private int questionCount = 0;
    private Question currentQuestion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadQuestion();
        showQuestion();
    }

    private void showQuestion() {
        if (questionCount < questions.size()) {
            currentQuestion = questions.get(questionCount);
            questionLabel.setText(currentQuestion.getQuestion());
            List<String> options = currentQuestion.getOptions();

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));
        } else {
            questionLabel.setText("Congratulations!");
            option1.setVisible(false);
            option2.setVisible(false);
            option3.setVisible(false);
            option4.setVisible(false);
            nextQuestionHyperlink.setVisible(false);
        }
    }

    private void loadQuestion() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String question = parts[0];
                    String answer = parts[1];
                    List<String> options = Arrays.asList(parts[2].split(";"));

                    questions.add(new Question(question, answer, options));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAnwser(ActionEvent actionEvent) {
        JFXButton selectedButton = (JFXButton) actionEvent.getSource();
        String selectedAnswer = selectedButton.getText();

        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            score++;
            selectedButton.setTextFill(Color.GREEN);
        } else {
            selectedButton.setTextFill(Color.RED);
        }

        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
    }

    public void nextQuestion(ActionEvent actionEvent) {
        questionCount++;

        option1.setDisable(false);
        option2.setDisable(false);
        option3.setDisable(false);
        option4.setDisable(false);

        option1.setTextFill(Color.BLACK);
        option2.setTextFill(Color.BLACK);
        option3.setTextFill(Color.BLACK);
        option4.setTextFill(Color.BLACK);

        showQuestion();
    }
}
