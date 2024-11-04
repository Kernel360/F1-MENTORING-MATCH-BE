package com.biengual.userapi.pointhistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.pointhistory.domain.PointHistoryRepository;
import com.biengual.userapi.pointhistory.domain.PointHistoryStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PointHistoryStoreImpl implements PointHistoryStore {
    private final PointHistoryRepository pointHistoryRepository;

    // 포인트 변동 내역을 PointHistory에 저장
    @Override
    public void recordPointHistory(Long userId, PointReason reason, Long currentPoint) {
        PointHistoryEntity history = PointHistoryEntity.createPointHistory(
            userId, reason.getValue(), currentPoint + reason.getValue(), reason
        );
        pointHistoryRepository.save(history);
    }
}
