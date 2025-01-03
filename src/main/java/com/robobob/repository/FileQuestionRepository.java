package com.robobob.repository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robobob.model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileQuestionRepository implements QuestionRepository {
    private static final Logger logger = LogManager.getLogger(FileQuestionRepository.class);
    private final ObjectMapper objectMapper;
    private final Map<String, Question> questionAnswerMap = new ConcurrentHashMap<>();

    @Value("${questions.file:questions.json}")  // Load from properties or default to "questions.json"
    private String fileName;

    @Value("${robobob.filerpository.json-not-found}")
    private String fileNotFoundMsg;


    public FileQuestionRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @PostConstruct
    public void loadQuestionsAndAnswers() throws IOException {
        logger.info("Loading questions from questions.json...");
        // Create ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Load questions.json from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("questions.json");

        // Handle missing file scenario
        if (inputStream == null) {
            logger.error("questions.json not found in classpath.");
            throw new FileNotFoundException(fileNotFoundMsg);
        }
        // Deserialize questions.json -> array of Question
        Question[] questions = objectMapper.readValue(inputStream, Question[].class);

        for (Question question : questions) {
            questionAnswerMap.put(question.getQuestion().toLowerCase().trim(), question);
        }
        logger.info("Loading of questions completed. Total loaded: {}", questionAnswerMap.size());
    }

    @Override
    public boolean exists(String questionText) {
        logger.debug("exists('{}') -> {}", questionText, questionText.toLowerCase().trim());
        return questionAnswerMap.containsKey(questionText.toLowerCase().trim());
    }

    @Override
    public Optional<Question> findAnswer(String questionText) {
        logger.debug("findAnswer('{}') called", questionText);
        return Optional.ofNullable(questionAnswerMap.get(questionText.toLowerCase().trim()));
    }
}
