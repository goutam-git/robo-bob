package com.robobob.controller;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.service.ArithmeticQuestionService;
import com.robobob.service.BasicQuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AskQuestionControllerTest {

    @Mock
    private BasicQuestionService basicQuestionService;

    @Mock
    private ArithmeticQuestionService arithmeticQuestionService;

    @InjectMocks
    private AskQuestionController askQuestionController;

    @BeforeEach
    void setUp() {
        // Manually inject the unknownQuestionMsg field to simulate @Value
        ReflectionTestUtils.setField(
                askQuestionController,
                "unknownQuestionMsg",
                "Unknown question encountered"
        );
    }
    @Test
    void testProcessQuestion_arithmeticExpression() {
        // GIVEN
        String question = "2+2";

        // Arithmetic service should recognize question as arithmetic
        when(arithmeticQuestionService.isArithmeticExpression(question))
                .thenReturn(true);
        // Suppose evaluateExpression returns "4.0"
        when(arithmeticQuestionService.evaluateExpression(eq(question), anyString(), anyString()))
                .thenReturn("4.0");

        // WHEN
        ResponseEntity<String> response = askQuestionController.processQuestion(question);

        // THEN
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("4.0", response.getBody());

        // Verify interactions
        verify(arithmeticQuestionService).isArithmeticExpression(question);
        verify(arithmeticQuestionService).evaluateExpression(eq(question), anyString(), anyString());
        verifyNoInteractions(basicQuestionService);
    }

    @Test
    void testProcessQuestion_basicQuestion() {
        // GIVEN
        String question = "What is your name?";

        // Arithmetic check returns false
        when(arithmeticQuestionService.isArithmeticExpression(question))
                .thenReturn(false);
        // Basic question check returns true
        when(basicQuestionService.isBasicQuestion(question))
                .thenReturn(true);
        // Suppose the answer is "RoboBob"
        when(basicQuestionService.findAnswer(eq(question), anyString(), anyString()))
                .thenReturn("RoboBob");

        // WHEN
        ResponseEntity<String> response = askQuestionController.processQuestion(question);

        // THEN
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("RoboBob", response.getBody());

        verify(arithmeticQuestionService).isArithmeticExpression(question);
        verifyNoMoreInteractions(arithmeticQuestionService);  // No evaluateExpression call

        verify(basicQuestionService).isBasicQuestion(question);
        verify(basicQuestionService).findAnswer(eq(question), anyString(), anyString());
    }

    @Test
    void testProcessQuestion_unknownQuestion() {
        // GIVEN
        String question = "Unknown question text";

        // Both checks return false
        when(arithmeticQuestionService.isArithmeticExpression(question))
                .thenReturn(false);
        when(basicQuestionService.isBasicQuestion(question))
                .thenReturn(false);

        // WHEN + THEN: Expect exception
        InvalidQuestionException ex = assertThrows(
                InvalidQuestionException.class,
                () -> askQuestionController.processQuestion(question)
        );
        assertEquals("Unknown question encountered", ex.getMessage());

        // Verify both services are called
        verify(arithmeticQuestionService).isArithmeticExpression(question);
        verify(basicQuestionService).isBasicQuestion(question);
        verifyNoMoreInteractions(arithmeticQuestionService, basicQuestionService);
    }

}
