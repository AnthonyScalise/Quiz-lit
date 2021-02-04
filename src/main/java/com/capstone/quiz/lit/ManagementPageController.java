package com.capstone.quiz.lit;

import java.io.IOException;
import javafx.fxml.FXML;

public class ManagementPageController {
    
    @FXML
    private void switchToHomePage() throws IOException {
        App.setRoot("homePage");
    }
}
