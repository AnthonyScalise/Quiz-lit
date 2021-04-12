package com.capstone.quiz.lit;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;


public class TestSelectController {
    @FXML private AnchorPane mainAnchorPane;

    
    @FXML
    private void switchToHomePage() throws IOException {
        App.setRoot("homePage");
    }
    
    
    private ListView<String> list;
    
    @FXML
    protected void initialize() {
        list = new ListView();
        ObservableList<String> items = FXCollections.observableArrayList();
        for(int i=0; i<App.getTestAmmount(); i++) {
            items.add(App.getTest(i).getName());
        }
        list.setItems(items);
        list.setPrefWidth(962.0);
        list.setPrefHeight(607.0);
        list.setLayoutX(222.0);
        list.setLayoutY(191.0);
        mainAnchorPane.getChildren().add(list);
    }
    
    @FXML
    private void startSelectedTest() throws IOException {
        int index = list.getSelectionModel().getSelectedIndex();
        if(index > -1) { 
            mainAnchorPane.getChildren().remove(list);
//            App.getTest(index).printFirstQuestionDataTest();
            App.currentTest = index;
            App.setRoot("testPage");
        }
    }
}