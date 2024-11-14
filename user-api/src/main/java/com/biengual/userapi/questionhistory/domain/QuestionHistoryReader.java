package com.biengual.userapi.questionhistory.domain;

public interface QuestionHistoryReader {
    boolean existsCorrectedQuestionHistory(Long userId, String questionId);
}
