package com.biengual.userapi.point.domain;

import static com.biengual.core.domain.entity.point.QPointEntity.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PointCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 포인트 업데이트를 위한 쿼리
    public void updatePoint(Long userId, int value) {
        queryFactory
            .update(pointEntity)
            .set(pointEntity.currentPoint, pointEntity.currentPoint.add(value))
            .where(pointEntity.userId.eq(userId))
            .execute();

    }
}
