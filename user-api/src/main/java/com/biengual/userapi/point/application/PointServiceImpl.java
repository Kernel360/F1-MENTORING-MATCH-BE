package com.biengual.userapi.point.application;

import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.payment.domain.PaymentStore;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.point.domain.PointService;
import com.biengual.userapi.validator.ContentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.biengual.core.response.error.code.PointErrorCode.POINTLESS_CONTENT;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final ContentValidator contentValidator;
    private final ContentReader contentReader;
    private final PointManager pointManager;
    private final PaymentStore paymentStore;

    // 최신 컨텐츠에 대한 포인트 지불
    @Override
    @Transactional
    public void payPointsForRecentContent(Long contentId, Long userId) {
        ContentEntity content = contentReader.find(contentId);

        if (contentValidator.verifyLearnableContent(content, userId)) {
            throw new CommonException(POINTLESS_CONTENT);
        }

        pointManager.updateAndSavePoint(PointReason.VIEW_RECENT_CONTENT, userId);
        paymentStore.updatePaymentHistory(userId, contentId);
    }
}
