package com.capstone.quiz.lit;
//
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//  JavaFX App
public class App extends Application {    
    private static Scene scene;
    private static List<TestProfile> localTestList;
    private static int testAmmount;
    public static int currentTest = 0;
    public static List<Boolean> currentTestScore;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("homePage"), 1406, 970);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    private static void getLocalData() {
        try {
            JsonObject jsonDataObject = (JsonObject) Jsoner.deserialize(new String(Files.readAllBytes(Paths.get("CapstoneTestProgramLocalData.json"))));
            testAmmount = Integer.parseInt(jsonDataObject.get("testAmmount").toString());
            ArrayList<TestProfile> tList = new ArrayList<>();
            for(int i=0; i<testAmmount; i++) {
                tList.add(new TestProfile((JsonObject)(((JsonArray)jsonDataObject.get("localTestList")).get(i))));
            }
            localTestList = new ArrayList<>();
            localTestList.addAll(tList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createLocalData(List<TestProfile> testList) {
        JsonObject saveObject = new JsonObject();
        saveObject.put("testAmmount", testList.size());
        JsonArray testArray = new JsonArray();
        for(int i=0; i<testList.size(); i++) {
            testArray.add(testList.get(i));
        }
        saveObject.put("localTestList", testArray); 
        try (FileWriter fileWriter = new FileWriter("CapstoneTestProgramLocalData.json")) {
            Jsoner.serialize(saveObject, fileWriter);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void saveLocalData() {
        createLocalData(localTestList);
    }
    
    public static void reloadLocalData() {
        getLocalData();
    }
    
    public static TestProfile getTest(int num) {
        return localTestList.get(num);
    }
    
    public static int getTestAmmount() {
        return testAmmount;
    }

    public static void main(String[] args) {    
        getLocalData();
        launch();
    }
}