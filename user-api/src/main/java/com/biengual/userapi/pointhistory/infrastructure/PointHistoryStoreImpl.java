package com.biengual.userapi.pointhistory.infrastructure;

import static com.biengual.core.response.error.code.UserErrorCode.*;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.pointhistory.domain.PointHistoryRepository;
import com.biengual.userapi.pointhistory.domain.PointHistoryStore;
import com.biengual.userapi.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PointHistoryStoreImpl implements PointHistoryStore {
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;

    // 포인트 변동 내역을 PointHistory에 저장
    @Override
    public void recordPointHistory(Long userId, PointReason reason) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new CommonException(USER_NOT_FOUND));

        PointHistoryEntity history = PointHistoryEntity.createPointHistoryAfterPointUpdate(
            user, reason.getValue(), reason
        );
        pointHistoryRepository.save(history);
    }
}
