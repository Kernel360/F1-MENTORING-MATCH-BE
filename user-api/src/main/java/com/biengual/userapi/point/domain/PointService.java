package com.biengual.userapi.point.domain;

import com.biengual.core.enums.PointReason;

public interface PointService {
    void updatePoint(Long userId, PointReason reason);

    void updatePointFirstDailyLogin(Long userId);
}
