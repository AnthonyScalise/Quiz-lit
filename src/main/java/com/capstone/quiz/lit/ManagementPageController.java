package com.capstone.quiz.lit;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;



public class ManagementPageController {
    @FXML private TextField testNameField;
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
    
    private ListView<String> testList = new ListView();
    private ListView<String> questionList = new ListView();
    
    private ObservableList<String> testItems = FXCollections.observableArrayList();
    private ObservableList<String> questionItems = FXCollections.observableArrayList();
    
    private VBox answerContentBox = new VBox();;
    private VBox answerNumberBox = new VBox();;
    
    private ArrayList<TextField> answerContentItems = new ArrayList<>();
    private ArrayList<TextField> answerNumberItems = new ArrayList<>();
    private ArrayList<TextField> selectedAnswers = new ArrayList<>();
    private ArrayList<AnswerInteractionEventHandler> selectedAnswerEventHandlers = new ArrayList<>();
    
    private ChangeListener<String> questionChange;
    private ChangeListener<String> testNameChange;
    
    
    
    public abstract class AnswerInteractionEventHandler implements EventHandler<Event> {
        private String text;
        private int num;
        
        public AnswerInteractionEventHandler(String text, int num) {
            this.text = text;
            this.num = num;
        }
        
        public String getText() {
            return text;
        }
        
        public int getNum() {
            return num;
        }
    }
    
    @FXML
    protected void initialize() {
        allChangesSaved = false;
        saveButton.setDisable(true);
        startInterface();
    }
    
    
    private void startInterface() {
        for(int i=0; i<App.getTestAmmount(); i++)
            testItems.add(App.getTest(i).getName());
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
        
        refreshQuestionList();
        refreshAnswerList(); 
    }
    
    
    private void refreshInterface(int testSelectionNum, int questionSelectionNum) {
        clearAnswerSelectionListeners();
        removeQuestionListener();
        removeTestNameListener();
        testItems = FXCollections.observableArrayList();
        questionItems = FXCollections.observableArrayList();
        testList = new ListView();
        questionList = new ListView();
        testSelectionAnchor.getChildren().clear();
        questionSelectionAnchor.getChildren().clear();
        answerContentAnchor.getChildren().clear();
        answerNumAnchor.getChildren().clear();
        startInterface();
        testList.getSelectionModel().select(testSelectionNum);
        testList.scrollTo(testSelectionNum);
        questionList.getSelectionModel().select(questionSelectionNum);
        questionList.scrollTo(questionSelectionNum);
    }
    

    // TODO: Might depreciate soon if still not being used
    // private void refreshInterface() {
    //     int testSelectionNum = testList.getSelectionModel().getSelectedIndex();
    //     int questionSelectionNum = questionList.getSelectionModel().getSelectedIndex();
    //     refreshInterface(testSelectionNum, questionSelectionNum);
    // }
    
    
    private void refreshQuestionList() {
        testList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            clearAnswerSelectionListeners();
            removeQuestionListener();
            removeTestNameListener();
            questionItems.clear();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            bindTestNameChangeListener(testNum);
            for(int i=1; i<=App.getTest(testNum).getQuestionAmmount(); i++)
                questionItems.add("Q"+i+")   "+App.getTest(testNum).getQuestion(i-1));
            if(App.getTest(testNum).getQuestionAmmount() > 0)
                questionList.getSelectionModel().select(0);
        });
    }
    
    
    private void refreshAnswerList() {
        questionList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            clearAnswerSelectionListeners();
            answerContentBox.getChildren().clear();
            answerNumberBox.getChildren().clear();
            answerContentItems.clear();
            answerNumberItems.clear();
            int testNum = testList.getSelectionModel().getSelectedIndex();
            int questionNum = questionList.getSelectionModel().getSelectedIndex();
            if(testNum >= 0 && questionNum >= 0) {
                removeQuestionListener();
                bindQuestionChangeListener(testNum, questionNum);
                int correctAnsNum = App.getTest(testNum).getCorrectAnswerId(questionNum);
                for(int i=0; i<App.getTest(testNum).getAnswers(questionNum).length; i++) {
                    String answerText = App.getTest(testNum).getAnswers(questionNum)[i];
                    answerContentItems.add(new TextField());
                    answerContentItems.get(i).setText(answerText);
                    answerContentItems.get(i).setEditable(true);
                    if(i == correctAnsNum)
                        answerContentItems.get(i).setStyle("-fx-background-color: gold; -fx-background-radius: 0px 10px 10px 0px; -fx-border-radius: 0px 10px 10px 0px;");
                    else
                        answerContentItems.get(i).setStyle("-fx-background-radius: 0px 10px 10px 0px; -fx-border-radius: 0px 10px 10px 0px;");
                    answerNumberItems.add(new TextField());
                    answerNumberItems.get(i).setCursor(Cursor.DEFAULT);
                    answerNumberItems.get(i).setText("Answer "+(i+1));
                    answerNumberItems.get(i).setEditable(false);
                    answerNumberItems.get(i).setStyle("-fx-background-color: grey; -fx-highlight-fill: null; -fx-highlight-text-fill: null; -fx-background-radius: 10px 0px 0px 10px; -fx-border-radius: 10px 0px 0px 10px;");
                    answerContentBox.getChildren().add(answerContentItems.get(i));
                    answerNumberBox.getChildren().add(answerNumberItems.get(i));
                    bindAnswerChangeListener(testNum, questionNum, i);
                    bindAnswerSelectionListener(i);
                }
            }
        });
    }
    
    
    private void bindTestNameChangeListener(int testNum) {
        testNameField.setText(App.getTest(testNum).getName());
        ChangeListener<String> listener = new ChangeListener<String>(){ 
            @Override
                public void changed(ObservableValue<? extends String> observableName, String oldValueName, String newValueName) {
                    if(!newValueName.equals(App.getTest(testNum).getName())) {
                        App.getTest(testNum).setName(newValueName);
                        refreshInterface(testNum, questionList.getSelectionModel().getSelectedIndex());
                        changes(true);
                    }
                }
        };
        testNameChange = listener;
        testNameField.textProperty().addListener(testNameChange);
    }
    
    
    private void removeTestNameListener() {
        if(testNameChange != null) {
            testNameField.textProperty().removeListener(testNameChange);
            testNameField.clear();
        }
    }
    
    
    private void bindQuestionChangeListener(int testNum, int questionNum) {
        questionField.setText(App.getTest(testNum).getQuestion(questionNum));
        ChangeListener<String> listener = new ChangeListener<String>(){ 
            @Override
                public void changed(ObservableValue<? extends String> observableQuestion, String oldValueQuestion, String newValueQuestion) {
                    if(!newValueQuestion.equals(App.getTest(testNum).getQuestion(questionNum))) {
                        App.getTest(testNum).setQuestion(questionNum, newValueQuestion);
                        refreshInterface(testNum, questionNum);
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
    
    
    private void bindAnswerSelectionListener(int ansNum) {
        selectedAnswerEventHandlers.add(new AnswerInteractionEventHandler(answerNumberItems.get(ansNum).getText(), ansNum) {
            @Override
            public void handle(Event event) {
                String currentColorHex = colorToHex((Color)answerNumberItems.get(getNum()).getBackground().getFills().get(0).getFill());
                if(currentColorHex.equals("808080")) {
                    answerNumberItems.get(getNum()).setStyle("-fx-background-color: #303030; -fx-highlight-fill: null; -fx-highlight-text-fill: null; -fx-text-fill: white;-fx-background-radius: 10px 0px 0px 10px; -fx-border-radius: 10px 0px 0px 10px;");
                    selectedAnswers.add(answerNumberItems.get(getNum()));
                } else {
                    answerNumberItems.get(getNum()).setStyle("-fx-background-color: #808080; -fx-highlight-fill: null; -fx-highlight-text-fill: null; -fx-background-radius: 10px 0px 0px 10px; -fx-border-radius: 10px 0px 0px 10px;");
                    selectedAnswers.remove(answerNumberItems.get(getNum()));
                }
            }
        });
        answerNumberItems.get(ansNum).addEventHandler(MouseEvent.MOUSE_PRESSED, selectedAnswerEventHandlers.get(ansNum));
    }
    

    // TODO: Might depreciate soon if still not being used
    // private void removeAnswerSelectionListener(int ansNum) {
    //     answerNumberItems.get(ansNum).removeEventHandler(MouseEvent.MOUSE_PRESSED, selectedAnswerEventHandlers.get(ansNum));
    //     selectedAnswerEventHandlers.remove(ansNum);
    // }
    
    
    private void clearSelectedAnswers() {
        for(TextField ans : answerNumberItems) {
            ans.setStyle("-fx-background-color: #808080; -fx-highlight-fill: null; -fx-highlight-text-fill: null; -fx-background-radius: 10px 0px 0px 10px; -fx-border-radius: 10px 0px 0px 10px;");
        }
        selectedAnswers.clear();
    }
    
    
    private void clearAnswerSelectionListeners() {
        if(selectedAnswerEventHandlers.size() > 0) {
            for(int i=0; i<selectedAnswerEventHandlers.size(); i++) {
                answerNumberItems.get(i).removeEventHandler(MouseEvent.MOUSE_PRESSED, selectedAnswerEventHandlers.get(i));
            }
            selectedAnswerEventHandlers.clear();
            selectedAnswers.clear();
        }
    }
    
    
    private void changes(boolean status) {
        allChangesSaved = !status;
        saveButton.setDisable(!status);
    }
    
    
    @FXML
    private void addAQuestion() throws IOException {
        int testNum = testList.getSelectionModel().getSelectedIndex();
        if(testNum > -1) {
            int questionNum = questionList.getSelectionModel().getSelectedIndex();
            if(questionNum > -1) {
                App.addQuestion(testNum, questionNum+1);
                refreshInterface(testNum, questionNum+1);
                changes(true);
            } else {
                questionNum = App.getTest(testNum).getQuestionAmmount();
                App.addQuestion(testNum, questionNum);
                refreshInterface(testNum, questionNum);
                changes(true);
            }
        }
    }
    

    @FXML
    private void removeAQuestion() throws IOException {
        int testNum = testList.getSelectionModel().getSelectedIndex();
        int questionNum = questionList.getSelectionModel().getSelectedIndex();
        if(testNum > -1) {
            if(questionNum > -1) {
                App.removeQuestion(testNum, questionNum);
                if(questionNum == 0)
                    refreshInterface(testNum, questionNum);
                else
                    refreshInterface(testNum, questionNum-1);
                changes(true);
            }
        }
    }
    
    
    @FXML
    private void addAnAnswer() throws IOException {
        int questionNum = questionList.getSelectionModel().getSelectedIndex();
        if(questionNum > -1) {
            int answerNum = answerContentItems.size();
            if(answerNum < 14) {
                if(selectedAnswers.size() > 0) {
                    int maxIndex = -1;
                    for(TextField ans : selectedAnswers) {
                        int currentIndex = answerNumberItems.indexOf(ans);
                        if(currentIndex > maxIndex)
                            maxIndex = currentIndex;
                    }
                    answerNum = maxIndex+1;
                }
                int testNum = testList.getSelectionModel().getSelectedIndex();
                for(int i=0; i<selectedAnswerEventHandlers.size(); i++) {
                    answerNumberItems.get(i).removeEventHandler(MouseEvent.MOUSE_PRESSED, selectedAnswerEventHandlers.get(i));
                }
                selectedAnswerEventHandlers.clear();
                App.getTest(testNum).addAnswer(questionNum, answerNum, "");
                answerNumberItems.add(answerNum, new TextField());
                answerNumberItems.get(answerNum).setText("Answer "+(answerNum+1));
                answerNumberItems.get(answerNum).setEditable(false);
                answerNumberItems.get(answerNum).setStyle("-fx-background-color: #808080; -fx-highlight-fill: null; -fx-highlight-text-fill: null; -fx-background-radius: 10px 0px 0px 10px; -fx-border-radius: 10px 0px 0px 10px;");
                answerContentItems.add(answerNum, new TextField());
                answerContentItems.get(answerNum).setText("");
                answerContentItems.get(answerNum).setEditable(true);
                answerNumberBox.getChildren().add(answerNum, answerNumberItems.get(answerNum));
                answerContentBox.getChildren().add(answerNum, answerContentItems.get(answerNum));
                bindAnswerChangeListener(testNum, questionNum, answerNum);
                clearSelectedAnswers();
                for(int i=0; i<answerNumberItems.size(); i++) {
                    answerNumberItems.get(i).setText("Answer "+(i+1));
                    answerNumberBox.getChildren().set(i, answerNumberItems.get(i));
                    bindAnswerSelectionListener(i);
                }
                int correctAnswerNum = App.getTest(testNum).getCorrectAnswerId(questionNum);
                if(correctAnswerNum > answerNum-1) {
                    App.setCorrectAnswerId(testNum, questionNum, correctAnswerNum+1);
                }
                changes(true);
            }
        }
    }
    
    
    @FXML
    private void removeAnswers() throws IOException {
        if(selectedAnswers.size() > 0) {
            for(int i=0; i<selectedAnswerEventHandlers.size(); i++) {
                answerNumberItems.get(i).removeEventHandler(MouseEvent.MOUSE_PRESSED, selectedAnswerEventHandlers.get(i));
            }
            selectedAnswerEventHandlers.clear();
            for(TextField field : selectedAnswers) {
                int testNum = testList.getSelectionModel().getSelectedIndex();
                int questionNum = questionList.getSelectionModel().getSelectedIndex();
                int answerNum = answerNumberItems.indexOf(field);
                int correctAnswerNum = App.getTest(testNum).getCorrectAnswerId(questionNum);
                if(correctAnswerNum > answerNum)
                    App.setCorrectAnswerId(testNum, questionNum, correctAnswerNum-1);
                App.getTest(testNum).removeAnswer(questionNum, answerNum);
                answerNumberItems.remove(answerNum);
                answerContentItems.remove(answerNum);
                answerNumberBox.getChildren().remove(answerNum);
                answerContentBox.getChildren().remove(answerNum);
            }
            clearSelectedAnswers();
            for(int i=0; i<answerNumberItems.size(); i++) {
                answerNumberItems.get(i).setText("Answer "+(i+1));
                answerNumberBox.getChildren().set(i, answerNumberItems.get(i));
                bindAnswerSelectionListener(i);
            }
            changes(true);
        }
    }
    
    
    @FXML
    private void addATest() throws IOException{
        int testNum = testList.getSelectionModel().getSelectedIndex();
        if(testNum > -1) {
            App.addTest(testNum+1);
            refreshInterface(testNum+1, 0);
        } else {
            int testAmmount = App.getTestAmmount();
            App.addTest(testAmmount);
            refreshInterface(testAmmount, 0);
        }
    }
    
    
    @FXML
    private void removeATest() throws IOException{
        int testNum = testList.getSelectionModel().getSelectedIndex();
        if(testNum > -1) {
            App.removeTest(testNum);
            if(testNum > 0)
                refreshInterface(testNum-1, 0);
            else
                refreshInterface(0, 0);
            changes(true);
        }
    }
    
        
    @FXML
    private void saveData() throws IOException {
        if(!allChangesSaved) {
            App.saveLocalData();
            changes(false);
        }
    }
    
    @FXML
    private void markCorrect() throws IOException {
        int testNum = testList.getSelectionModel().getSelectedIndex();
        int questionNum = questionList.getSelectionModel().getSelectedIndex();
        if(selectedAnswers.size() > 0) {
            if(selectedAnswers.size() == 1) {
                int oldCorrectId = App.getTest(testNum).getCorrectAnswerId(questionNum);
                int newCorrectId = answerNumberItems.indexOf(selectedAnswers.get(0));
                answerContentItems.get(oldCorrectId).setStyle("-fx-background-radius: 0px 10px 10px 0px; -fx-border-radius: 0px 10px 10px 0px;");
                answerContentItems.get(newCorrectId).setStyle("-fx-background-color: gold; -fx-background-radius: 0px 10px 10px 0px; -fx-border-radius: 0px 10px 10px 0px;");
                App.setCorrectAnswerId(testNum, questionNum, newCorrectId);
                clearSelectedAnswers();
                changes(true);
            } else {
                System.out.println("Too many selected");
            }
        }
    }
    
    
    private String colorToHex(Color color) {
        String hex1;
        String hex2;
        hex1 = Integer.toHexString(color.hashCode()).toUpperCase();
        switch(hex1.length()) {
            case 2:
                hex2 = "000000";
                break;
            case 3:
                hex2 = String.format("00000%s", hex1.substring(0, 1));
                break;
            case 4:
                hex2 = String.format("0000%s", hex1.substring(0,2));
                break;
            case 5:
                hex2 = String.format("000%s", hex1.substring(0,3));
                break;
            case 6:
                hex2 = String.format("00%s", hex1.substring(0,4));
                break;
            case 7:
                hex2 = String.format("0%s", hex1.substring(0,5));
                break;
            default:
                hex2 = hex1.substring(0, 6);
        }
        return hex2;
    }
} 