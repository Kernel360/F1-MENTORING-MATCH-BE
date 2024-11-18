package com.biengual.userapi.questionhistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryReader;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionHistoryReaderImpl implements QuestionHistoryReader {
    private final QuestionHistoryRepository questionHistoryRepository;

    @Override
    public boolean existsCorrectedQuestionHistory(Long userId, String questionId) {
        return questionHistoryRepository.existsByUserIdAndQuestionIdAndFinalTryIsTrue(userId, questionId);
    }
}
