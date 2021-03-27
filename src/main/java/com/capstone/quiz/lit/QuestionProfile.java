package com.capstone.quiz.lit;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;

import java.util.List;

public class QuestionProfile {
    private int id = 0;
    private StringBuilder question = new StringBuilder("");
    //private String question = "";
    private int answerAmmount = 0;
    private List<String> answerList;
    private int correctAnswerId = 0;
    
    public QuestionProfile(int qId, String qQuestion, int qAnsAmmount, List<String> qAnsList, int qCorrectAnsId) {
        this.id =  qId;
        this.question.replace(0, this.question.length(), qQuestion);
        this.answerAmmount = qAnsAmmount;
        this.answerList = qAnsList;
        this.correctAnswerId = qCorrectAnsId;
    }
    
    public QuestionProfile(JsonObject json) {
        this.id = Integer.parseInt(json.get("id").toString());
        this.question.replace(0, this.question.length(), json.get("question").toString());
        this.answerAmmount = Integer.parseInt(json.get("answerAmmount").toString());
        ArrayList<String> ansList = new ArrayList<>();
        for(int i=0; i<this.answerAmmount; i++) {
            ansList.add(((JsonArray)json.get("answerList")).get(i).toString());
        }
        this.answerList = new ArrayList<>();
        this.answerList.addAll(ansList);
        this.correctAnswerId = Integer.parseInt(json.get("correctAnswerId").toString());
    }
    
    public QuestionProfile(QuestionProfile question) {
        this.id = question.getId();
        this.question.replace(0, this.question.length(), question.getQuestion());
        this.answerAmmount = question.getAnswerAmmount();
        for(String answer : question.getAnswers()) {
            this.answerList.add(answer);
        }
        this.correctAnswerId = question.getCorrectAnswerId();
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getQuestion() {
        return this.question.toString();
    }
    
    public int getAnswerAmmount() {
        return this.answerAmmount;
    }
    
    public String[] getAnswers() {
        String[] answerStringList = new String[this.answerAmmount];
        for(int i=0; i<this.answerAmmount; i++) {
            answerStringList[i] = this.answerList.get(i);
        }
        return answerStringList;
    }
    
    public void setAnswer(int index, String answerData) {
        if(index < this.answerAmmount) {
            this.answerList.set(index, answerData);
        }
    }
    
    public void removeAnswer(int index) {
        if(index < this.answerAmmount) {
            this.answerList.remove(index);
            this.answerAmmount--;
        }
    } 
    
    public void addAnswer(int index, String answerData) {
        this.answerList.add(index, answerData);
        this.answerAmmount++;
    }
    
    public void setQuestion(String questionData) {
        this.question.replace(0, this.question.length(), questionData);
    }
    
    public int getCorrectAnswerId() {
        return this.correctAnswerId;
    }
    
    public JsonObject packJsonObject() {
        JsonObject json = new JsonObject();
        json.put("id", this.id);
        json.put("question", this.question.toString());
        json.put("answerAmmount", this.answerAmmount);
        String[] answerArray = new String[this.answerAmmount];
        for(int i=0; i<this.answerAmmount; i++) {
            answerArray[i] = answerList.get(i);
        }
        json.put("answerList", answerArray);
        json.put("correctAnswerId", this.correctAnswerId);
        return json;
    }
}
