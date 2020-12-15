package com.capstone.quiz.lit;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScorePageController {
    @FXML Label score;
    
    @FXML
    protected void initialize() {
        System.out.println(App.currentTestScore);
        double total = App.currentTestScore.size();
        double totalCorrect = 0;
        for(int i=0; i<total; i++) {
            if (App.currentTestScore.get(i)) {
                totalCorrect++;
            }
        }
        double percentScore = ((totalCorrect*100)/total);
        String result = String.format("%.2f", percentScore);
        score.setText(result+"%");
    }
    
    @FXML
    private void goHome() throws IOException {
        App.setRoot("homePage");
    }
}
