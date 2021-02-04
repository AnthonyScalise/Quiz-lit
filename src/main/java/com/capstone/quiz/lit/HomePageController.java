package com.capstone.quiz.lit;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomePageController {
    
    @FXML
    private void switchToTestSelectPage() throws IOException {
        App.setRoot("testSelectPage");
    }
    
    @FXML
    private void switchToManagementPage() throws IOException {
        App.setRoot("managementPage");
    }
}
