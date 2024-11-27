package com.biengual.userapi.point.application;

import com.biengual.core.annotation.RedisDistributedLock;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.payment.domain.PaymentStore;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.point.domain.PointService;
import com.biengual.userapi.validator.PointValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointValidator pointValidator;
    private final ContentReader contentReader;
    private final PointManager pointManager;
    private final PaymentStore paymentStore;

    // 최신 컨텐츠에 대한 포인트 지불
    @Override
    @RedisDistributedLock(key = "#userId + \":\" + #contentId")
    public void payPointsForRecentContent(Long contentId, Long userId) {
        ContentEntity content = contentReader.findUnverifiedContent(contentId);

        if (pointValidator.verifyPaymentForRecentContent(content, userId)) {
            pointManager.updateAndSavePoint(PointReason.VIEW_RECENT_CONTENT, userId);
            paymentStore.updatePaymentHistory(userId, contentId);
        }
    }
}
