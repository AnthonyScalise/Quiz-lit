package com.capstone.quiz.lit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;


public class TestPageController {
    @FXML private Label questionLabel;
    @FXML private Button backButton;
    @FXML private Button nextButton;
    @FXML private VBox answerList;
    
    private int pageIndex = 0;
    private final TestProfile test = App.getTest(App.currentTest);
    private List<AnchorPane> ansElements = new ArrayList();
    private List<Circle> ansCircles = new ArrayList();
    private ArrayList<Integer> givenAnswers = new ArrayList<Integer>();
    
    @FXML
    protected void initialize() {
        backButton.setVisible(false);
        for(int i=0; i<test.getQuestionAmmount(); i++) {
            givenAnswers.add(-1);
        }
        loadQuestionPage();
    }
    
    private void checkAnswers() {
        ArrayList<Boolean> score = new ArrayList<Boolean>();
        for(int i=0; i<test.getQuestionAmmount(); i++) {
            score.add(((givenAnswers.get(i) == test.getCorrectAnswerId(i))? Boolean.TRUE : Boolean.FALSE));
        }
        App.currentTestScore = score;
    }
    
    private void selectAnswer(int ansIndex) {
        String[] answers = test.getAnswers(pageIndex);
        for(int i=0; i<answers.length; i++) {
            if(i == ansIndex) {
                ansCircles.get(ansIndex).setFill(Color.BLACK);
                ansCircles.get(ansIndex).setStroke(Color.GOLD);
                givenAnswers.set(pageIndex, ansIndex);
            } else {
                ansCircles.get(i).setFill(Color.GOLD);
                ansCircles.get(i).setStroke(Color.BLACK);
            }
        }
    }
    
    private void selectPreviousAnswer(int pageIndex) {
        if(givenAnswers.get(pageIndex) != -1) {
            ansCircles.get(givenAnswers.get(pageIndex)).setFill(Color.BLACK);
            ansCircles.get(givenAnswers.get(pageIndex)).setStroke(Color.GOLD);
        }
    }
    
    @FXML
    private void goNext() throws IOException {
        if(pageIndex < test.getQuestionAmmount()-1) {
            pageIndex++;
            loadQuestionPage();
            selectPreviousAnswer(pageIndex);
        } else {
            checkAnswers();
            App.setRoot("scorePage");
        }
    }
    
    @FXML
    private void goBack() throws IOException {
        pageIndex--;
        loadQuestionPage();
        selectPreviousAnswer(pageIndex);
    }
    
    @FXML
    private void loadQuestionPage() {
        backButton.setVisible((pageIndex > 0));
        if(pageIndex >= test.getQuestionAmmount()-1) {
            nextButton.setText("Finish");
            nextButton.setLayoutX(1189.0);
            nextButton.setPrefWidth(203.0);
        } else {
            nextButton.setText("Next");
            nextButton.setLayoutX(1218.0);
            nextButton.setPrefWidth(174.0);
        }
        for(int i=ansElements.size()-1; i>=0; i--) {
            answerList.getChildren().remove(ansElements.get(i));
            ansElements.remove(i);
            ansCircles.remove(i);
        }
        questionLabel.setText(test.getQuestion(pageIndex));
        String[] answers = test.getAnswers(pageIndex);
        int answerAmmount = answers.length;
        for(int i=0; i<answerAmmount; i++) {
            AnchorPane questionPane = new AnchorPane();
            questionPane.setPrefSize(1279.0, 70.0);
            Circle ansBtn = new Circle();
            Label ansLabel = new Label();
            ansCircles.add(ansBtn);
            ansBtn.setFill(Color.GOLD);
            ansBtn.setRadius(18.0);
            ansBtn.setStroke(Color.BLACK);
            ansBtn.setStrokeType(StrokeType.INSIDE);
            ansBtn.setLayoutX(32.0);
            ansBtn.setLayoutY(35.0);
            ansLabel.setStyle("-fx-font: 48 System;");
            ansLabel.setPrefSize(1247.0, 70.0);
            ansLabel.setLayoutX(70.0);
            ansLabel.setTextFill(Color.WHITE);
            ansLabel.setText(answers[i]);
            questionPane.getChildren().addAll(ansBtn, ansLabel);
            answerList.getChildren().add(questionPane);
            questionPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                selectAnswer(ansElements.indexOf(e.getSource()));
            });
            ansElements.add(questionPane);
        }
    }
}
