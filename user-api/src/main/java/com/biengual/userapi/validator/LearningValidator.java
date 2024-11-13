package com.biengual.userapi.validator;

import com.biengual.core.annotation.Validator;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.learning.domain.LearningHistoryCustomRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Learning 도메인의 검증을 위한 클래스
 *
 * @author 문찬욱
 */
@Validator
@RequiredArgsConstructor
public class LearningValidator {
    private final LearningHistoryCustomRepository learningHistoryCustomRepository;

    public boolean verifyAlreadyLearningInMonth(Long userId, Long contentId, LocalDateTime learningTime) {
        YearMonth yearMonth = PeriodUtil.toYearMonth(learningTime);

        return learningHistoryCustomRepository
            .existsByUserIdAndContentIdInMonth(userId, contentId, yearMonth);
    }
}
