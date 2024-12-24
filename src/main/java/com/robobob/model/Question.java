package com.robobob.model;

/**
 * this is the model class which will contain the
 * details of predefined questions
 */
public class Question {
    private String id;
    private String question;
    private String answer;
    private String type;        // e.g., "BASIC" or "ARITHMETIC" or other Types
    private String askedAt;

    public Question() {
    }

    public Question(String id, String question, String answer, String type, String askedAt) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.askedAt = askedAt;
    }
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAskedAt() {
        return askedAt;
    }

    public void setAskedAt(String askedAt) {
        this.askedAt = askedAt;
    }
}
