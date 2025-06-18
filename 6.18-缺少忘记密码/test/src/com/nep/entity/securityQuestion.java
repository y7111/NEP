package com.nep.entity;

import java.io.Serializable;


public class securityQuestion implements Serializable {
   private String Question;

    @Override
    public String toString() {
        return "securityQuestion{" +
                "Question='" + Question + '\'' +
                '}';
    }

    public securityQuestion(String Question){
        this.Question=Question;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }
}
