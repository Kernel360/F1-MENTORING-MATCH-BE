package com.biengual.userapi.pointhistory.domain;

import com.biengual.core.enums.PointReason;

public interface PointHistoryStore {
    void recordPointHistory(Long userId, PointReason reason, Long currentPoint);
}
