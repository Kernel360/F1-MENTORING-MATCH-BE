package com.biengual.userapi.point.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.point.PointEntity;
import com.biengual.userapi.point.domain.PointReader;
import com.biengual.userapi.point.domain.PointRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class PointReaderImpl implements PointReader {
    private final PointRepository pointRepository;

    // 유저 id(PK) 로 포인트 조회
    @Override
    public void findPoint(Long userId) {
        pointRepository.findById(userId)
            .orElseGet(() -> {
                    PointEntity point = PointEntity.createByUserId(userId);
                    return pointRepository.save(point);
                }
            );
    }

}
