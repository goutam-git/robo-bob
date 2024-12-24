package com.robobob.service.impl;

import com.robobob.exception.InvalidQuestionException;
import com.robobob.model.Question;
import com.robobob.repository.QuestionRepository;
import com.robobob.service.BasicQuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 *  Concrete implementation of the BasicQuestionService.
 *  Finds if the question exists in repository and gets the answer
 *  if it exists
 */
@Service
public class BasicQuestionServiceImpl implements BasicQuestionService {
    private static final Logger logger = LogManager.getLogger(BasicQuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    //Injecting the unknown-answer message from application.properties.

    @Value("${robobob.basic-question.unknown-answer}")
    private String defaultUnknownAnswer;

    @Value("${robobob.basic-question.exception}")
    private String basicQuestionExceptionMsg;

    @Autowired
    public BasicQuestionServiceImpl(QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    @Override
    public boolean isBasicQuestion(String questionText) {
        String regexQuestionText = questionText.replaceAll("\\?", "");
        logger.debug("Checking if '{}' is a basic question...", questionText);
        return questionRepository.exists(regexQuestionText);
    }

    @Override
    public String findAnswer(String questionText,String type,String askedAt) {
        logger.debug("Finding answer for basic question: {}", questionText);
        try {
            String regexQuestionText = questionText.replaceAll("\\?", "");
            Question question = questionRepository.findAnswer(regexQuestionText)
                    .orElseGet(() -> {
                        logger.info("No answer found in repository; creating default response...");
                        Question defaultQuestion = new Question();
                        defaultQuestion.setId(UUID.randomUUID().toString());
                        defaultQuestion.setQuestion(questionText);
                        defaultQuestion.setAnswer(defaultUnknownAnswer);
                        defaultQuestion.setType(type);
                        defaultQuestion.setAskedAt(askedAt);
                        return defaultQuestion;
                    });
            // saving the question.Actual implementation of save is outside scope
            save(question);
            return question.getAnswer();
        }catch (Exception ex){
            // Wrap any unexpected issue and let Exception handler handle it
            logger.error("Failed to find answer for '{}'", questionText, ex);
            throw new InvalidQuestionException(basicQuestionExceptionMsg, ex);
        }
    }
}
