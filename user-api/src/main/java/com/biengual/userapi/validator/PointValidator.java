package com.biengual.userapi.validator;

import com.biengual.core.annotation.Validator;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.payment.domain.PaymentContentHistoryRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_IS_DEACTIVATED;
import static com.biengual.core.response.error.code.PointErrorCode.*;

@Validator
@RequiredArgsConstructor
public class PointValidator {
    private final ContentReader contentReader;
    private final PaymentContentHistoryRepository paymentContentHistoryRepository;

    // 업데이트 전에 포인트가 음수가 되지 않는지 검증
    public void verifyUpdatePoint(Long currentPoint, PointReason reason) {
        if (reason.add(currentPoint) < 0) {
            throw new CommonException(POINT_NEVER_MINUS);
        }
    }

    // 하루 첫 로그인인지 검증
    public boolean verifyFirstDailyLogin(UserEntity user) {
        return user.getLastLoginTime().toLocalDate().isBefore(LocalDate.now());
    }

    // 포인트 지불이 필요한 최신 컨텐츠인지 검증
    public boolean verifyPaymentForRecentContent(Long contentId, Long userId) {
        ContentEntity content = contentReader.find(contentId);

        if (content.isDeactivated()) {
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        if (!content.isRecentContent()) {
            throw new CommonException(CONTENT_NOT_RECENT);
        }

        if (paymentContentHistoryRepository.existsByUserIdAndContentId(userId, contentId)) {
            throw new CommonException(ALREADY_PAID_FOR_RECENT_CONTENT);
        }

        return true;
    }

    // 미션 성공했는지 검증 - 미션은 F -> T로만 바뀜
    public boolean verifyMission(boolean missionInfo, boolean missionCommand) {
        return !missionInfo && missionCommand;
    }
}