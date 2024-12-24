package com.robobob.service.impl;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.model.Question;
import com.robobob.service.ArithmeticQuestionService;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ArithmeticQuestionServiceImpl implements ArithmeticQuestionService {
    private static final Logger logger = LogManager.getLogger(ArithmeticQuestionServiceImpl.class);
    //Injecting the Arithmetic error message from application.properties.

    @Value("${robobob.arithmetic-question.empty-expression}")
    private String emptyExprMsg;

    @Value("${robobob.arithmetic-question.invalid-operation}")
    private String invalidOprMsg;

    @Value("${robobob.arithmetic-question.arithmetic-error}")
    private String arithmeticErrorMsg;

    @Value("${robobob.arithmetic-question.unexpected-error}")
    private String unexpectedErrorMsg;

    @Override
    public boolean isArithmeticExpression(String questionText) {
        // check if question text might be an arithmetic expression (digits + operators)
        logger.debug("Checking if '{}' is a arithmetic expression...", questionText);
        String expr = questionText.replaceAll("\\s+", "");
        return expr.matches("^[0-9+\\-*/().]+$");
    }

    @Override
    public String evaluateExpression(String questionText,String type,String askedAt) {
        if (questionText == null || questionText.trim().isEmpty()) {
            logger.error("expression is null or empty ...throwing exception");
            throw new InvalidQuestionException(emptyExprMsg);
        }

        //  Check for disallowed repeated operators (e.g., ++, --, **, //)
        //    and ensure only digits and arithmetic symbols are used.
        //    This will catch "2++2", "2--2", etc.
        String expr = questionText.replaceAll("\\s+", "");  // remove all whitespace
        String regex = "^(?!.*(\\+\\+|--|\\*\\*|//))[0-9+\\-*/().]+$";
        if (!expr.matches(regex)) {
            logger.error("invalid expression '{}'", questionText);
            throw new InvalidQuestionException(invalidOprMsg+": "+ questionText);
        }
        try {
            Question question = new Question();
            question.setId(UUID.randomUUID().toString());
            question.setQuestion(questionText);
            question.setType(type);
            question.setAskedAt(askedAt);
            String expressionString =questionText.trim();
            // Evaluate expression using exp4j
            Expression expression = new ExpressionBuilder(expressionString).build();
            double result = expression.evaluate();
            String answer = String.valueOf(result);
            question.setAnswer(answer);
            // saving the question.Actual implementation of save is outside scope
            save(question);
            return answer;
        } catch (ArithmeticException ex) {
            // e.g., division by zero or other arithmetic error
            logger.error("Failed to find answer for '{}'", questionText, ex);
            throw new InvalidQuestionException(arithmeticErrorMsg+": " + ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            // Expression is invalid (e.g., syntax error)
            logger.error("Failed to find answer for '{}'", questionText, ex);
            throw new InvalidQuestionException(invalidOprMsg+": ", ex);
        } catch (Exception ex) {
            // Any other unexpected error
            logger.error("Failed to find answer for '{}'", questionText, ex);
            throw new InvalidQuestionException(unexpectedErrorMsg+": ", ex);
        }
    }
}
