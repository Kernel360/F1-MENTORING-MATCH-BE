package com.biengual.userapi.point.domain;

import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.content.domain.ContentCommand;

public interface PointService {
    void updatePoint(ContentCommand.GetDetail command, PointReason reason);

    void updatePointFirstDailyLogin(UserEntity user);
}
