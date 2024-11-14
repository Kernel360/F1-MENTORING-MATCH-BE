package com.biengual.userapi.questionhistory.domain;

public interface QuestionHistoryStore {
    void recordQuestionHistory(Long userId, String questionId, Boolean isCorrect);
}
