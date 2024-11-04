package com.biengual.userapi.point.domain;

import com.biengual.core.enums.PointReason;

public interface PointStore {
    Long updateCurrentPointByPointReason(Long userId, PointReason reason);

}
