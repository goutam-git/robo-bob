package com.robobob.exception;

/**
 This Exception will be thrown if the Basic Question asked by the user is
 not present in the predefined list
 */
public class InvalidQuestionException extends RuntimeException {

    public InvalidQuestionException(String message) {
        super(message);
    }
    public InvalidQuestionException(String message,Exception ex) {
        super(message,ex);
    }


}
