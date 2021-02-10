package com.capstone.quiz.lit;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ManagementPageController {
    @FXML private TextField questionField;
    @FXML private AnchorPane testSelectionAnchor;
    @FXML private AnchorPane questionSelectionAnchor;
    @FXML private AnchorPane answerNumAnchor;
    @FXML private AnchorPane answerContentAnchor;
    @FXML private Button saveButton;
    
    @FXML
    private void switchToHomePage() throws IOException {
        App.setRoot("homePage");
        App.reloadLocalData();
    }
    
    private boolean allChangesSaved;
    
    private ListView<String> testList;
    private ListView<String> questionList;
    
    private ObservableList<String> testItems;
    private ObservableList<String> questionItems;
    
    private VBox answerContentBox;
    private VBox answerNumberBox;
    
    private ArrayList<TextField> answerContentItems;
    private ArrayList<TextField> answerNumberItems;
    
    private ChangeListener<String> questionChange;
    
    @FXML
    protected void initialize() {
        allChangesSaved = false;
        saveButton.setDisable(true);
        
        testList = new ListView();
        questionList = new ListView();
        
        answerContentBox = new VBox();
        answerNumberBox = new VBox();
        
        testItems = FXCollections.observableArrayList();
        questionItems = FXCollections.observableArrayList();

        answerContentItems = new ArrayList<>();
        answerNumberItems = new ArrayList<>();
        
        for(int i=0; i<App.getTestAmmount(); i++) {
            testItems.add(App.getTest(i).getName());
        }
        testList.setItems(testItems);  
        testList.setPrefWidth(263);
        testList.setPrefHeight(280);
        
        questionList.setItems(questionItems);
        questionList.setPrefWidth(263);
        questionList.setPrefHeight(487);
        
        answerContentBox.setPrefWidth(959);
        answerContentBox.setPrefHeight(646);
        answerContentBox.setSpacing(20);
        answerNumberBox.setPrefWidth(70);
        answerNumberBox.setPrefHeight(646);
        answerNumberBox.setSpacing(20);
        
        testSelectionAnchor.getChildren().add(testList);
        questionSelectionAnchor.getChildren().add(questionList);
        
        answerContentAnchor.getChildren().add(answerContentBox);
        answerNumAnchor.getChildren().add(answerNumberBox);
        
        
        testList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            questionItems.clear();
            removeQuestionListener();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            for(int i=1; i<=App.getTest(testNum).getQuestionAmmount(); i++) {
                questionItems.add("Question "+i);
            }
        });
        
        
        questionList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            answerContentBox.getChildren().clear();
            answerNumberBox.getChildren().clear();
            answerContentItems.clear();
            answerNumberItems.clear();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            int questionNum = questionList.getSelectionModel().getSelectedIndex();
            if(testNum >= 0 && questionNum >= 0) {
                removeQuestionListener();
                bindQuestionChageListener(testNum, questionNum);
                for(int i=0; i<App.getTest(testNum).getAnswers(questionNum).length; i++) {
                    String answerText = App.getTest(testNum).getAnswers(questionNum)[i];
                    answerContentItems.add(new TextField());
                    answerContentItems.get(i).setText(answerText);
                    answerContentItems.get(i).setEditable(true);
                    answerNumberItems.add(new TextField());
                    answerNumberItems.get(i).setText("Answer "+(i+1));
                    answerNumberItems.get(i).setEditable(false);
                    answerNumberItems.get(i).setStyle("-fx-background-color: grey; -fx-highlight-fill: null; -fx-highlight-text-fill: null;");
                    answerContentBox.getChildren().add(answerContentItems.get(i));
                    answerNumberBox.getChildren().add(answerNumberItems.get(i));
                    bindAnswerChangeListener(testNum, questionNum, i);
                }
            }
        });
    }
    
    private void bindQuestionChageListener(int testNum, int questionNum) {
        questionField.setText(App.getTest(testNum).getQuestion(questionNum));
        ChangeListener<String> listener = new ChangeListener<String>(){ 
            @Override
                public void changed(ObservableValue<? extends String> observableQuestion, String oldValueQuestion, String newValueQuestion) {
                    if(!newValueQuestion.equals(App.getTest(testNum).getQuestion(questionNum))) {
                        App.getTest(testNum).setQuestion(questionNum, newValueQuestion);
                        changes(true);
                    }
                }
        };
        questionChange = listener;
        questionField.textProperty().addListener(questionChange);
    }
    
    private void removeQuestionListener() {
        if(questionChange != null) {
            questionField.textProperty().removeListener(questionChange);
            questionField.clear();
        }
    }
    
    private void bindAnswerChangeListener(int testNum, int questionNum, int answerNum) {
        answerContentItems.get(answerNum).textProperty().addListener((ObservableValue<? extends String> observableAns, String oldValueAns, String newValueAns) -> {
            App.getTest(testNum).setAnswer(questionNum, answerNum, newValueAns);
            changes(true);
        });
    }
    
    private void changes(boolean status) {
        allChangesSaved = !status;
        saveButton.setDisable(!status);
    }
    
    @FXML
    private void addAQuestion() throws IOException {
        int testNum = testList.getSelectionModel().getSelectedIndex();
        int questionNum = questionList.getSelectionModel().getSelectedIndex();
        changes(true);
    }
    
    @FXML
    private void addAnAnswer() throws IOException {
        int testNum = testList.getSelectionModel().getSelectedIndex();
        int questionNum = questionList.getSelectionModel().getSelectedIndex();
        changes(true);
    }
    
    @FXML
    private void saveData() throws IOException {
        if(!allChangesSaved) {
            App.saveLocalData();
            changes(false);
        }
    }
}