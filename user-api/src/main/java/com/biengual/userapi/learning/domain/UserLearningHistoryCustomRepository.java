package com.biengual.userapi.learning.domain;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.biengual.core.domain.entity.userlearninghistory.QUserLearningHistoryEntity.userLearningHistoryEntity;

@Repository
@RequiredArgsConstructor
public class UserLearningHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 해당 컨텐츠의 유저 학습률을 조회하기 위한 쿼리
    public Optional<Integer> findLearningRateByUserIdAndContentId(Long userId, Long contentId) {
        return Optional.ofNullable(
            queryFactory
            .select(userLearningHistoryEntity.learningRate)
            .from(userLearningHistoryEntity)
                .where(userLearningHistoryEntity.userId.eq(userId)
                    .and(userLearningHistoryEntity.contentId.eq(contentId)))
            .fetchOne()
        );
    }
}
