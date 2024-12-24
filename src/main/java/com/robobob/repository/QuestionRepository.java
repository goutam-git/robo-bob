package com.robobob.repository;

import com.robobob.model.Question;

import java.util.Optional;

/**
 * This is the repository interface ,which can be extended to
 * File based or external storage based implementation
 */
public interface QuestionRepository {
    boolean exists(String questionText);
    Optional<Question> findAnswer(String questionText);
}
