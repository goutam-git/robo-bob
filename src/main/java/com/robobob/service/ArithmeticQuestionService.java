package com.robobob.service;

import com.robobob.model.Question;

public interface ArithmeticQuestionService extends QuestionService{
    boolean isArithmeticExpression(String questionText);
    String evaluateExpression(String questionText,String type,String askedAt);
}
