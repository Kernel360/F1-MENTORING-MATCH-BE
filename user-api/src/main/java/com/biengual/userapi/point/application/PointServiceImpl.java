package com.biengual.userapi.point.application;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.enums.PointReason;
import com.biengual.userapi.point.domain.PointService;
import com.biengual.userapi.point.domain.PointStore;
import com.biengual.userapi.pointhistory.domain.PointHistoryStore;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.user.domain.UserStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointStore pointStore;
    private final PointHistoryStore pointHistoryStore;
    private final UserReader userReader;
    private final UserStore userStore;

    @Override
    @Transactional
    public void updatePoint(Long userId, PointReason reason) {
        Long currentPoint = pointStore.updateCurrentPointByPointReason(userId, reason);
        pointHistoryStore.recordPointHistory(userId, reason, currentPoint);
    }

    @Override
    @Transactional
    public void updatePointFirstDailyLogin(Long userId) {
        // 로그인 포인트 지급
        if (userReader.findLastLoginTime(userId).toLocalDate().isBefore(LocalDate.now())) {
            Long loginPoint = pointStore.updateCurrentPointByPointReason(userId, PointReason.FIRST_DAILY_LOG_IN);
            pointHistoryStore.recordPointHistory(userId, PointReason.FIRST_DAILY_LOG_IN, loginPoint);
        }
        // 로그인 시간 업데이트
        userStore.updateLastLoginTime(userId);
    }
}
