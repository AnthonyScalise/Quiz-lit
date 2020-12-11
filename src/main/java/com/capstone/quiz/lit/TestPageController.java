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
    
    @FXML
    protected void initialize() {
        backButton.setVisible(false);
        loadQuestionPage();
    }
    
    @FXML
    private void goNext() throws IOException {
        if(pageIndex < test.getQuestionAmmount()-1) {
            pageIndex++;
            loadQuestionPage();
        } else {
            App.setRoot("scorePage");
        }
    }
    
    @FXML
    private void goBack() throws IOException {
        pageIndex--;
        loadQuestionPage();
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
        }
        
        questionLabel.setText(test.getQuestion(pageIndex));
        String[] answers = test.getAnswers(pageIndex);
        int answerAmmount = answers.length;
        for(int i=0; i<answerAmmount; i++) {
            AnchorPane questionPane = new AnchorPane();
            questionPane.setPrefSize(1279.0, 70.0);
            Circle ansBtn = new Circle();
            Label ansLabel = new Label();
            ansBtn.setFill(Color.GOLD);
            ansBtn.setRadius(18.0);
            ansBtn.setStroke(Color.BLACK);
            ansBtn.setStrokeType(StrokeType.INSIDE);
            ansBtn.setLayoutX(32.0);
            ansBtn.setLayoutY(35.0);
            ansBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                System.out.println(e);
            });
            ansLabel.setStyle("-fx-font: 48 System;");
            ansLabel.setPrefSize(1247.0, 70.0);
            ansLabel.setLayoutX(70.0);
            ansLabel.setTextFill(Color.WHITE);
            ansLabel.setText(answers[i]);
            questionPane.getChildren().addAll(ansBtn, ansLabel);
            answerList.getChildren().add(questionPane);
            
            ansElements.add(questionPane);
        }
    }
}
