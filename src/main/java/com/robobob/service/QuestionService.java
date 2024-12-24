package com.robobob.service;

import com.robobob.model.Question;

/**
 *  Base service for the Robo App
 *  contains methods common to Base and Arithmetic services
 */
public interface QuestionService {
    // Not being used in current scope
    // will be utilized in future for storing the questions
    default void save(Question question){}
}
