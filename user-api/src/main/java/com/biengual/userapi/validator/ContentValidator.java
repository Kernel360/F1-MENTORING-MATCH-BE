package com.biengual.userapi.validator;

import com.biengual.core.annotation.Validator;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentLevelFeedbackHistoryRepository;
import com.biengual.userapi.payment.domain.PaymentContentHistoryRepository;
import lombok.RequiredArgsConstructor;

import static com.biengual.core.response.error.code.ContentErrorCode.*;

/**
 * Content 도메인의 검증을 위한 클래스
 *
 * @author 문찬욱
 */
@Validator
@RequiredArgsConstructor
public class ContentValidator {
    private final PaymentContentHistoryRepository paymentContentHistoryRepository;
    private final ContentLevelFeedbackHistoryRepository contentLevelFeedbackHistoryRepository;

    // 해당 컨텐츠가 학습할 수 있는 컨텐츠인지 검증
    public void verifyLearnableContent(ContentEntity content, Long userId) {
        if (content.isActivated()) {
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        if (content.isRecentContent()) {
            validatePaymentForRecentContent(content.getId(), userId);
        }
    }

    // 이미 해당 컨텐츠에 대해 난이도 피드백을 했는지 검증
    public void verifyAlreadySubmitLevelFeedback(Long userId, Long contentId) {
        if (contentLevelFeedbackHistoryRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new CommonException(CONTENT_LEVEL_FEEDBACK_HISTORY_ALREADY_EXISTS);
        }
    }

    // Internal Method =================================================================================================

    // 해당 최신 컨텐츠에 대해 포인트를 지불했는지 검증
    private void validatePaymentForRecentContent(Long contentId, Long userId) {
        if (!paymentContentHistoryRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new CommonException(UNPAID_RECENT_CONTENT);
        }
    }
}
