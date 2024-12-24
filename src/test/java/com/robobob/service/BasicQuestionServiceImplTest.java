package com.robobob.service;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.model.Question;
import com.robobob.repository.QuestionRepository;
import com.robobob.service.impl.BasicQuestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BasicQuestionServiceImplTest {
    @Mock
    private QuestionRepository questionRepository;
    // Instead of @Value injection,setting these fields manually
    private final String defaultUnknownAnswer = "I don't know. Please ask another question.";
    private final String basicQuestionExceptionMsg = "An unexpected error occurred in BasicQuestionService.";
    @InjectMocks
    private BasicQuestionServiceImpl basicQuestionService;

    @BeforeEach
    void setUp() {
        // Use ReflectionTestUtils to set @Value fields
        ReflectionTestUtils.setField(basicQuestionService,
                "defaultUnknownAnswer", defaultUnknownAnswer);
        ReflectionTestUtils.setField(basicQuestionService,
                "basicQuestionExceptionMsg", basicQuestionExceptionMsg);
    }

    @Test
    void testIsBasicQuestion_knownQuestion() {
        // Mock repository response
        when(questionRepository.exists("What is your name")).thenReturn(true);
        boolean result = basicQuestionService.isBasicQuestion("What is your name?");
        assertTrue(result, "Expected 'What is your name?' to be recognized as a basic question");
        // Verify that '?' was removed before calling the repository
        verify(questionRepository).exists("What is your name");
    }

    @Test
    void testIsBasicQuestion_unknownQuestion() {
        when(questionRepository.exists("Where are you")).thenReturn(false);
        boolean result = basicQuestionService.isBasicQuestion("Where are you?");
        assertFalse(result, "Expected 'Where are you?' to be unknown");
        verify(questionRepository).exists("Where are you");
    }

    @Test
    void testFindAnswer_foundInRepository() {
        // Suppose the repository returns a known question
        Question existingQuestion = new Question();
        existingQuestion.setId("q1");
        existingQuestion.setQuestion("What is your name");
        existingQuestion.setAnswer("RoboBob");
        when(questionRepository.findAnswer("What is your name")).thenReturn(Optional.of(existingQuestion));
        String answer = basicQuestionService.findAnswer("What is your name?", "BASIC", "2024-12-21");
        assertEquals("RoboBob", answer);
        verify(questionRepository).findAnswer("What is your name");
        // No fallback to default question since the question is found
    }

    @Test
    void testFindAnswer_notFoundInRepository() {
        // The repository returns empty
        when(questionRepository.findAnswer("Where are you")).thenReturn(Optional.empty());
        // We expect the service to create a default question with the defaultUnknownAnswer
        String answer = basicQuestionService.findAnswer("Where are you?", "BASIC", "2024-12-21");
        assertEquals(defaultUnknownAnswer, answer, "Should use default unknown answer");
        verify(questionRepository).findAnswer("Where are you");
    }

    @Test
    void testFindAnswer_unexpectedException() {
        // findAnswer(...) in repository throws a runtime exception
        when(questionRepository.findAnswer("invalid question")).thenThrow(new RuntimeException("unexpected failure"));
        InvalidQuestionException thrown = assertThrows(
                InvalidQuestionException.class,
                () -> basicQuestionService.findAnswer("invalid question", "BASIC", "2024-12-21")
        );
        // Check the message is from the property (basicQuestionExceptionMsg)
        assertTrue(thrown.getMessage().contains(basicQuestionExceptionMsg));
    }

}
