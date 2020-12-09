/**
 * @author Anthony
 */

package com.capstone.quiz.lit;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import java.util.List;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class TestProfile implements Jsonable {
    private String name = "";
    private int timeLimit = 0;
    private int questionAmmount = 0;
    private List<QuestionProfile> questionList;
    
    public TestProfile(String tName, int tTestTimeLimit, int tQuestionAmmount, List<QuestionProfile> tQuestionList) {
        this.name = tName;
        this.timeLimit = tTestTimeLimit;
        this.questionAmmount = tQuestionAmmount;
        this.questionList = tQuestionList;
    }
    
    public TestProfile(JsonObject json) {
        this.name = json.get("name").toString();
        this.timeLimit = Integer.parseInt(json.get("timeLimit").toString());
        this.questionAmmount = Integer.parseInt(json.get("questionAmmount").toString());
        ArrayList<QuestionProfile> qList = new ArrayList<>();
        for(int i=0; i<this.questionAmmount; i++) { 
            qList.add(new QuestionProfile((JsonObject)(((JsonArray)json.get("questionList")).get(i))));
        }
        this.questionList = new ArrayList<>();
        this.questionList.addAll(qList);
    }
    
    public String getName() {
        return this.name;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }
    
    public String getQuestion(int questionId) {
        return this.questionList.get(questionId).getQuestion();
    }
    
    public int getQuestionAmmount() {
        return this.questionAmmount;
    }

    public String[] getAnswers(int questionId) {
        return this.questionList.get(questionId).getAnswers();
    }

    public int getCorrectAnswerId(int questionId) {
        return this.questionList.get(questionId).getCorrectAnswerId();
    }
    
    //Debug log function
    public void printFirstQuestionDataTest() {
        System.out.println("Name: "+this.name+"\n");
        System.out.println("TimeLimit: "+this.timeLimit+"\n");
        System.out.println("CorrectAnswer: "+this.questionList.get(0).getCorrectAnswerId()+"\n");
        System.out.println("QuestionAmmount: "+this.questionAmmount+"\n");
        System.out.println("Question: "+this.questionList.get(0).getQuestion()+"\n");
        System.out.println("AnswerList: ");
        for(int i=0; i<this.getAnswers(0).length; i++) {
            System.out.println("    Q# "+i+": "+this.questionList.get(0).getAnswers()[i]);
        }
        System.out.println("\n");
    }
    
    @Override
    public String toJson() {
        JsonObject json = new JsonObject();
        json.put("name", this.name);
        json.put("timeLimit", this.timeLimit);
        json.put("questionAmmount", this.questionAmmount);
        JsonArray questionArray = new JsonArray();
        for(int i=0; i<this.questionAmmount; i++) {
            questionArray.add(this.questionList.get(i).packJsonObject());
        }
        json.put("questionList", questionArray);
        return json.toJson();
    }
    
    @Override
    public void toJson(Writer writable) throws IOException {
        try {
            writable.write(this.toJson());
        } catch(Exception ignored) {}
    }
}
