package com.biengual.userapi.questionhistory.domain;

public interface QuestionHistoryStore {
    void updateQuestionHistory(Long userId, String questionId, Boolean isCorrect);
}
