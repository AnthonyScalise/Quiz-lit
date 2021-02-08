package com.capstone.quiz.lit;

import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

public class ManagementPageController {
    @FXML private TextField questionField;
    @FXML private AnchorPane testSelectionAnchor;
    @FXML private AnchorPane questionSelectionAnchor;
    @FXML private AnchorPane answerEditorAnchor;
    @FXML private AnchorPane answerNumAnchor;
    @FXML private AnchorPane answerContentAnchor;
    
    @FXML
    private void switchToHomePage() throws IOException {
        App.setRoot("homePage");
    }
    
    private ListView<String> testList;
    private ListView<String> questionList;
    //private ListView<String> answerList;
    
    private VBox answerContentBox;
    private VBox answerNumberBox;
    
    
    @FXML
    protected void initialize() {
        testList = new ListView();
        questionList = new ListView();
        
        //answerList = new ListView();
        
        answerContentBox = new VBox();
        answerNumberBox = new VBox();
        
        ObservableList<String> testItems = FXCollections.observableArrayList();
        ObservableList<String> questionItems = FXCollections.observableArrayList();
        //ObservableList<String> answerItems = FXCollections.observableArrayList();

        ArrayList<TextField> answerContentItems = new ArrayList<TextField>();
        ArrayList<TextField> answerNumberItems = new ArrayList<TextField>();
        
        for(int i=0; i<App.getTestAmmount(); i++) {
            testItems.add(App.getTest(i).getName());
        }
        testList.setItems(testItems);  
        testList.setPrefWidth(263);
        testList.setPrefHeight(280);
        
        questionList.setItems(questionItems);
        questionList.setPrefWidth(263);
        questionList.setPrefHeight(487);
        
        //answerList.setItems(answerItems);
        //answerList.setPrefWidth(1057);
        //answerList.setPrefHeight(670);
        //answerList.setEditable(true);
        //answerList.setCellFactory(TextFieldListCell.forListView());
        
        answerContentBox.setPrefWidth(959);
        answerContentBox.setPrefHeight(646);
        answerContentBox.setSpacing(20);
        answerNumberBox.setPrefWidth(70);
        answerNumberBox.setPrefHeight(646);
        answerNumberBox.setSpacing(20);
        
        testSelectionAnchor.getChildren().add(testList);
        questionSelectionAnchor.getChildren().add(questionList);
        //answerEditorAnchor.getChildren().add(answerList);
        
        answerContentAnchor.getChildren().add(answerContentBox);
        answerNumAnchor.getChildren().add(answerNumberBox);
        
        
        testList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            questionItems.clear();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            for(int i=1; i<=App.getTest(testNum).getQuestionAmmount(); i++) {
                questionItems.add("Question "+i);
            }
        });
        
        
        questionList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {;
            //answerContentItems.clear();
            answerContentBox.getChildren().clear();
            answerNumberBox.getChildren().clear();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            int questionNum = questionList.getSelectionModel().getSelectedIndex();
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
            }
        });
        
        
        
        
//        questionList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            answerItems.clear();
//            int testNum = testList.getSelectionModel().getSelectedIndex();
//            int questionNum = questionList.getSelectionModel().getSelectedIndex();
//            for(int i=0; i<(App.getTest(testNum).getAnswers(questionNum).length * 3); i++) {
//                if(i%3 == 0) {
//                    answerItems.add("Answer "+((i/3)+1));
//                } else if(i%3 == 1) {
//                    answerItems.add(App.getTest(testNum).getAnswers(questionNum)[i/3]);
//                } else {
//                    answerItems.add(" ");
//                }
//            }
//        });
    }
}
