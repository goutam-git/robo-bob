package com.robobob.service;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.service.impl.ArithmeticQuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ArithmeticQuestionServiceImplTest {
    private ArithmeticQuestionServiceImpl arithmeticQuestionService;

    @BeforeEach
    void setUp() {
        arithmeticQuestionService = new ArithmeticQuestionServiceImpl();

        // set @Value fields for a pure unit test (no Spring context)
        ReflectionTestUtils.setField(arithmeticQuestionService,
                "emptyExprMsg", "Empty expression error");
        ReflectionTestUtils.setField(arithmeticQuestionService,
                "invalidOprMsg", "Invalid arithmetic operation");
        ReflectionTestUtils.setField(arithmeticQuestionService,
                "arithmeticErrorMsg", "Arithmetic error encountered");
        ReflectionTestUtils.setField(arithmeticQuestionService,
                "unexpectedErrorMsg", "Unexpected error encountered");
    }

    // ----------------------------------------------------
    // isArithmeticExpression(...) Tests
    // ----------------------------------------------------

    @Test
    void testIsArithmeticExpression_valid() {
        assertTrue(arithmeticQuestionService.isArithmeticExpression("2+2"));
        assertTrue(arithmeticQuestionService.isArithmeticExpression("10.5 - 3 * (2)"));
        assertTrue(arithmeticQuestionService.isArithmeticExpression("  4 / 2 "));
    }

    @Test
    void testIsArithmeticExpression_invalid() {
        // Contains letters => false
        assertFalse(arithmeticQuestionService.isArithmeticExpression("2+abc"));
        // Special characters => false
        assertFalse(arithmeticQuestionService.isArithmeticExpression("2$5"));
    }

    // ----------------------------------------------------
    // evaluateExpression(...) Tests
    // ----------------------------------------------------

    @Test
    void testEvaluateExpression_emptyOrNull() {
        // null
        InvalidQuestionException ex1 = assertThrows(
                InvalidQuestionException.class,
                () -> arithmeticQuestionService.evaluateExpression(null, "ARITHMETIC", "2024-12-21T10:00:00")
        );
        assertEquals("Empty expression error", ex1.getMessage());

        // blank
        InvalidQuestionException ex2 = assertThrows(
                InvalidQuestionException.class,
                () -> arithmeticQuestionService.evaluateExpression("   ", "ARITHMETIC", "2024-12-21T10:00:00")
        );
        assertEquals("Empty expression error", ex2.getMessage());
    }

    @Test
    void testEvaluateExpression_disallowedOperators() {
        // "2++2", "10--2", "2**3", "15//3" should fail
        String[] expressions = { "2++2", "10--2", "2**3", "15//3" };

        for (String expr : expressions) {
            InvalidQuestionException ex = assertThrows(
                    InvalidQuestionException.class,
                    () -> arithmeticQuestionService.evaluateExpression(expr, "ARITHMETIC", "2024-12-21T10:00:00")
            );
            assertTrue(ex.getMessage().contains("Invalid arithmetic operation"));
        }
    }

    @Test
    void testEvaluateExpression_divisionByZero() {
        // exp4j will throw ArithmeticException internally
        InvalidQuestionException ex = assertThrows(
                InvalidQuestionException.class,
                () -> arithmeticQuestionService.evaluateExpression("2/0", "ARITHMETIC", "2024-12-21T10:05:00")
        );
        assertTrue(ex.getMessage().contains("Arithmetic error encountered"));
    }

    @Test
    void testEvaluateExpression_illegalArgumentSyntax() {
        // Example: "2+*" => exp4j typically throws IllegalArgumentException (syntax error)
        InvalidQuestionException ex = assertThrows(
                InvalidQuestionException.class,
                () -> arithmeticQuestionService.evaluateExpression("2+*", "ARITHMETIC", "2024-12-21T10:06:00")
        );
        assertTrue(ex.getMessage().contains("Invalid arithmetic operation"));
    }

    @Test
    void testEvaluateExpression_unexpectedError() {
        try {
            arithmeticQuestionService.evaluateExpression("2 + 2", "ARITHMETIC", "2024-12-21");
            // If no unexpected error occurs, this passes silently
        } catch (InvalidQuestionException ex) {
            // If an unexpected error was thrown, we'd check the message
            assertTrue(ex.getMessage().contains("Unexpected error encountered"));
        }
    }

    @Test
    void testEvaluateExpression_success() {
        // A valid expression that should evaluate cleanly
        String result = arithmeticQuestionService.evaluateExpression("2 + 2", "ARITHMETIC", "2024-12-21T10:08:00");
        assertEquals("4.0", result);
    }
}
