package com.library.Controller;

import com.jfoenix.controls.JFXButton;
import com.library.LibraryApplication;
import com.library.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import static com.library.Controller.UserController.currentUser;

public class GameController extends SceneController {

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
    @FXML
    private Label username;
    @FXML
    private ImageView smallUserImageView;

    private List<Question> questions = new ArrayList<>();
    private int score = 0;
    private int questionCount = 0;
    private Question currentQuestion;

    @FXML
    public void initialize() {
        if (username != null && currentUser != null) {
            username.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        }

        if (smallUserImageView != null && currentUser != null && currentUser.getUserPicture() != null) {
            smallUserImageView.setImage(new Image(new ByteArrayInputStream(currentUser.getUserPicture())));
        }

        loadQuestion();
        showQuestion();
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
            Collections.shuffle(questions);
            if (questions.size() > 10) {
                questions = questions.subList(0, 10);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showQuestion() {
        if (questionLabel!=null) {
            if (questionCount < questions.size()) {
                currentQuestion = questions.get(questionCount);
                questionLabel.setText(currentQuestion.getQuestion());

                List<String> options = currentQuestion.getOptions();
                option1.setText(options.get(0));
                option2.setText(options.get(1));
                option3.setText(options.get(2));
                option4.setText(options.get(3));
            } else {
                showResult();
            }
        }
    }

    private void showResult() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/library/gameResult-view.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.setScore(score, questions.size());

            stage = (Stage) questionLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScore(int correctAnswers, int totalQuestions) {
        int wrongAnswers = totalQuestions - correctAnswers;
        double correctRatio = (double) correctAnswers / totalQuestions;
        double wrongRatio = (double) wrongAnswers / totalQuestions;

        scoreLabel.setText("Score: " + correctAnswers + " / " + totalQuestions);
        correctProgressIndicator.setProgress(correctRatio);
        wrongProgressIndicator.setProgress(wrongRatio);
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

        disableOptions();
    }

    private void disableOptions() {
        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
    }

    public void nextQuestion(ActionEvent actionEvent) {
        questionCount++;
        enableOptions();
        resetOptionColors();
        showQuestion();
    }

    private void enableOptions() {
        option1.setDisable(false);
        option2.setDisable(false);
        option3.setDisable(false);
        option4.setDisable(false);
    }

    private void resetOptionColors() {
        option1.setTextFill(Color.WHITE);
        option2.setTextFill(Color.WHITE);
        option3.setTextFill(Color.WHITE);
        option4.setTextFill(Color.WHITE);
    }

    public void onStartGameButtonClick(ActionEvent event) {
        LibraryApplication.getSceneController().loadGamePlayScene();
    }

    public void replayGame(ActionEvent event) {
        LibraryApplication.getSceneController().loadGamePlayScene();
    }

    public void quitGame(ActionEvent event) {
        LibraryApplication.getSceneController().loadDashboardView();
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
