package com.robobob.controller;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.service.ArithmeticQuestionService;
import com.robobob.service.BasicQuestionService;
import com.robobob.service.QuestionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * This is the controller class which will be used for asking the
 * Basic questions and evaluation of arithmetic expressions
 *
 */
@RestController
@RequestMapping("/api/v1/questions")
public class AskQuestionController {
    private static final Logger logger = LogManager.getLogger(AskQuestionController.class);
    private final BasicQuestionService basicQuestionService;
    private final ArithmeticQuestionService arithmeticQuestionService;

    @Value("${robobob.basic-question.unknown-answer}")
    private String unknownQuestionMsg;
    @Autowired
    public AskQuestionController(BasicQuestionService basicQuestionService,
                              ArithmeticQuestionService arithmeticQuestionService) {
        this.basicQuestionService = basicQuestionService;
        this.arithmeticQuestionService = arithmeticQuestionService;
    }
    @PostMapping
    public ResponseEntity<String> processQuestion(@RequestBody String question) {
        logger.info("Received question: {}", question);
        // Trim to avoid accidental whitespace
        question = question.trim();

        // Check if it's an arithmetic expression
        if (arithmeticQuestionService.isArithmeticExpression(question)) {
            logger.debug("Delegating to ArithmeticQuestionService...");
            String type = QuestionType.ARITHMETIC.name();
            String askedAt = LocalDateTime.now().toString();
            return ResponseEntity.ok(arithmeticQuestionService.evaluateExpression(question,type,askedAt));
        }
       // else Check if it's a basic RoboBob question
        else if (basicQuestionService.isBasicQuestion(question)) {
            logger.debug("Delegating to BasicQuestionService...");
            String type = QuestionType.ARITHMETIC.name();
            String askedAt = LocalDateTime.now().toString();
            return ResponseEntity.ok(basicQuestionService.findAnswer(question,type,askedAt));
        }
        // Otherwise, unknown question
        else {
            logger.warn("Unknown question encountered: {}", question);
            throw new InvalidQuestionException(unknownQuestionMsg);
        }
    }

}
