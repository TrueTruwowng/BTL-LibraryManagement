package com.library.Controller;

import com.jfoenix.controls.JFXButton;
import com.library.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.library.Controller.sceneController.stage;

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
    @FXML
    public ProgressIndicator correctProgressIndicator;
    @FXML
    public ProgressIndicator wrongProgressIndicator;
    @FXML
    public JFXButton replayButton;
    @FXML
    public JFXButton quitButton;
    @FXML
    public Label resultLabel;
    @FXML
    public Label scoreLabel;
    @FXML
    public Hyperlink quitGameHyperlink;
    public Button gameStart;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button myCollectionBtn;
    @FXML
    private Button logoutBtn;


    private List<Question> questions = new ArrayList<>();
    private int score = 0;
    private int questionCount = 0;
    private Question currentQuestion;

    // Game play
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadQuestion();
        showQuestion();
    }

    private void showQuestion() {
        if (questionCount < questions.size()) {
            currentQuestion = questions.get(questionCount);
            if (questionLabel != null) {
                questionLabel.setText(currentQuestion.getQuestion());
            }
            List<String> options = currentQuestion.getOptions();

            if (option1 != null) option1.setText(currentQuestion.getOptions().get(0));
            if (option2 != null) option2.setText(currentQuestion.getOptions().get(1));
            if (option3 != null) option3.setText(currentQuestion.getOptions().get(2));
            if (option4 != null) option4.setText(currentQuestion.getOptions().get(3));

        } else {
            showResult();
        }
    }

    private void loadQuestion() {
        //questions = new ArrayList<>();
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
            // Random 10 câu
            Collections.shuffle(questions);
            if (questions.size() > 10) {
                questions = questions.subList(0, 10);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAnswer(ActionEvent actionEvent) {
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

        option1.setTextFill(Color.WHITE);
        option2.setTextFill(Color.WHITE);
        option3.setTextFill(Color.WHITE);
        option4.setTextFill(Color.WHITE);

        showQuestion();
    }

    // Game result
    private void showResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/gameResult-view.fxml"));
            Parent root = loader.load();

            // Truyền dữ liệu kết quả
            gameController controller = loader.getController();
            controller.setScore(score, questions.size());

            stage = (Stage) questionLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScore(int correctAnswers, int totalQuestions) {
        int wrongAnswers = totalQuestions - correctAnswers;
        double correctRatio = (double) correctAnswers / totalQuestions;
        double wrongRatio = (double) wrongAnswers / totalQuestions;

        // Hiển thị điểm số
        scoreLabel.setText("Điểm của bạn: " + correctAnswers + " / " + totalQuestions);

        correctProgressIndicator.setProgress(correctRatio); // Tỷ lệ trả lời đúng
        wrongProgressIndicator.setProgress(wrongRatio); // Tỷ lệ trả lời sai
    }

    public void replayGame(ActionEvent event) {
        sceneController.loadGamePlayScene(stage);
    }

    public void quitGame(ActionEvent event) {
        sceneController.hadnleGameButton(stage);
    }

    // Game home
    public void onDashboardBtnClick() {
        sceneController.handleDashboardButton(stage);
    }
    public void onSettingsBtnClick() {
        sceneController.handleSettingbutton(stage);
    }
    public void onMyCollectionBtnClick() {
        sceneController.handleMyCollectionButton(stage);
    }
    public void onLogOutBtnClk() {
        sceneController.handleLogoutButton(stage);
    }
    public void onGameBtnClick() {
        sceneController.hadnleGameButton(stage);
    }
    public void onStartGameButtonClick(ActionEvent event) {
        sceneController.loadGamePlayScene(stage);
    }
}
