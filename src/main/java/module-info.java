module com.capstone.quiz.lit {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.capstone.quiz.lit to javafx.fxml;
    exports com.capstone.quiz.lit;
    requires json.simple;
}
