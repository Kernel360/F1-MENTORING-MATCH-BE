package com.biengual.userapi.point.infrastructure;

import static com.biengual.core.response.error.code.PointErrorCode.*;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.point.PointEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.point.domain.PointCustomRepository;
import com.biengual.userapi.point.domain.PointRepository;
import com.biengual.userapi.point.domain.PointStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PointStoreImpl implements PointStore {
    private final PointRepository pointRepository;
    private final PointCustomRepository pointCustomRepository;

    @Override
    public Long updateCurrentPointByPointReason(Long userId, PointReason reason) {
        PointEntity point = pointRepository.findById(userId)
            .orElseThrow(() -> new CommonException(POINT_NOT_FOUND));

        verifyUpdatePoint(point.getCurrentPoint(), reason);

        pointCustomRepository.updatePoint(userId, reason.getValue());

        return point.getCurrentPoint();
    }

    // Internal Methods ===============================================================================================
    private void verifyUpdatePoint(Long currentPoint, PointReason reason) {
        if (currentPoint + reason.getValue() < 0) {
            throw new CommonException(POINT_NEVER_MINUS);
        }
    }
}
