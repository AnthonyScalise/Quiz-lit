/**
 * @author Anthony
 */

package com.capstone.quiz.lit;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import java.util.ArrayList;

import java.util.List;

public class QuestionProfile {
    private int id = 0;
    private String question = "";
    private int answerAmmount = 0;
    private List<String> answerList;
    private int correctAnswerId = 0;
    
    public QuestionProfile(int qId, String qQuestion, int qAnsAmmount, List<String> qAnsList, int qCorrectAnsId) {
        this.id =  qId;
        this.question = qQuestion;
        this.answerAmmount = qAnsAmmount;
        this.answerList = qAnsList;
        this.correctAnswerId = qCorrectAnsId;
    }
    
    public QuestionProfile(JsonObject json) {
        this.id = Integer.parseInt(json.get("id").toString());
        this.question = json.get("question").toString();
        this.answerAmmount = Integer.parseInt(json.get("answerAmmount").toString());
        ArrayList<String> ansList = new ArrayList<>();
        for(int i=0; i<this.answerAmmount; i++) {
            ansList.add(((JsonArray)json.get("answerList")).get(i).toString());
        }
        this.answerList = new ArrayList<>();
        this.answerList.addAll(ansList);
        this.correctAnswerId = Integer.parseInt(json.get("correctAnswerId").toString());
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getQuestion() {
        return this.question;
    }
    
    public String[] getAnswers() {
        String[] answerStringList = new String[this.answerAmmount];
        for(int i=0; i<this.answerAmmount; i++) {
            answerStringList[i] = this.answerList.get(i);
        }
        return answerStringList;
    }
    
    public int getCorrectAnswerId() {
        return this.correctAnswerId;
    }
    
    public JsonObject packJsonObject() {
        JsonObject json = new JsonObject();
        json.put("id", this.id);
        json.put("question", this.question);
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
