package com.biengual.userapi.point.domain;

import com.biengual.core.annotation.Manager;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.pointhistory.domain.PointHistoryStore;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.validator.PointValidator;

import lombok.RequiredArgsConstructor;

@Manager
@RequiredArgsConstructor
public class PointManager {
    private final PointValidator pointValidator;
    private final UserReader userReader;
    private final UserStore userStore;
    private final PointHistoryStore pointHistoryStore;

    public void updateAndSavePoint(PointReason reason, Long userId) {
        Long currentPoint = userReader.findUserPoint(userId);

        pointValidator.verifyUpdatePoint(currentPoint, reason);
        userStore.updatePoint(userId, reason);
        pointHistoryStore.recordPointHistory(userId, reason, currentPoint);
    }
}
