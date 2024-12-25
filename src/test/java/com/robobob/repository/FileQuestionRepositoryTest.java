package com.robobob.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robobob.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileQuestionRepositoryTest {
    @Mock
    private ObjectMapper mockObjectMapper;

    @InjectMocks
    private FileQuestionRepository fileQuestionRepository;

    private Question[] mockQuestions;

    @BeforeEach
    void setUp() throws IOException {
        // Prepare mock questions
        Question q1 = new Question("q1", "What is your name", "RoboBob", "BASIC", "2024-12-21T10:00:00");
        Question q2 = new Question("q2", "How are you", "I am fine", "BASIC", "2024-12-21T10:05:00");
        mockQuestions = new Question[]{q1, q2};

        // Simulate JSON file content
        String mockJson = """
            [
              { "id": "q1", "question": "What is your name", "answer": "RoboBob", "type": "BASIC", "askedAt": "2024-12-21T10:00:00" },
              { "id": "q2", "question": "How are you", "answer": "I am fine", "type": "BASIC", "askedAt": "2024-12-21T10:05:00" }
            ]
        """;
        InputStream mockInputStream = new ByteArrayInputStream(mockJson.getBytes(StandardCharsets.UTF_8));

        // Mock ObjectMapper to read InputStream
        when(mockObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false))
                .thenReturn(mockObjectMapper);
        when(mockObjectMapper.readValue(any(InputStream.class), ArgumentMatchers.eq(Question[].class)))
                .thenReturn(mockQuestions);

        // Inject the mock InputStream by creating a new repository with the mock file name
        fileQuestionRepository = new FileQuestionRepository(mockObjectMapper);

        // Manually call the method
        fileQuestionRepository.loadQuestionsAndAnswers();
    }

    @Test
    void testExists_knownQuestion() {
        // "What is your name?" is in mock data
        assertTrue(fileQuestionRepository.exists("What is your name"));
        // "What is your purpose?" also in mock data
        assertTrue(fileQuestionRepository.exists("How are you"));
    }

    @Test
    void testExists_unknownQuestion() {
        // This question is not in mock data
        assertFalse(fileQuestionRepository.exists("Where are you located?"));
    }

    @Test
    void testFindAnswer_found() {
        // We know "What is your name?" is in mock data
        Optional<Question> optQ = fileQuestionRepository.findAnswer("What is your name");
        assertTrue(optQ.isPresent(), "Expected question to be found");

        Question q = optQ.get();
        assertEquals("q1", q.getId());
        assertEquals("RoboBob", q.getAnswer());
        assertEquals("BASIC", q.getType());
    }

    @Test
    void testFindAnswer_notFound() {
        Optional<Question> optQ = fileQuestionRepository.findAnswer("Where are you located?");
        assertFalse(optQ.isPresent(), "Expected question to be absent");
    }
}
