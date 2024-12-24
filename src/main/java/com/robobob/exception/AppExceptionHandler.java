package com.robobob.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A global exception handler that catches exceptions
 * thrown anywhere in the application (controllers, services, etc.)
 * and translates them to a standardized HTTP response.
 */
@ControllerAdvice
public class AppExceptionHandler {

    private static final Logger logger = LogManager.getLogger(AppExceptionHandler.class);
    /**
     * Custom handler for InvalidQuestionException.
     * Prompt the user to ask another question.
     */
   @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<String> handleInvalidQuestion(InvalidQuestionException ex) {
        String message = ex.getMessage();
        // Return a 400 Bad Request, with the user-friendly prompt
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all for other unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        // Return a 500 Internal Server Error response
        logger.error("An unexpected error occurred.: {}", ex);
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
