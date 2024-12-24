package com.robobob.service;

import com.robobob.model.Question;

/**
 Interface for Basic Questions.Checks if question exits
 and provide answer to the predefined question
 */
public interface BasicQuestionService extends QuestionService{
    boolean isBasicQuestion(String questionText);
    String findAnswer(String questionText,String type,String askedAt);
}
