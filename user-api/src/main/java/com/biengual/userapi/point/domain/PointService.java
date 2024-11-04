package com.biengual.userapi.point.domain;

import com.biengual.core.enums.PointReason;
import com.biengual.userapi.content.domain.ContentCommand;

public interface PointService {
    void updatePoint(ContentCommand.GetDetail command, PointReason reason);

    void updatePointFirstDailyLogin(Long userId);
}
