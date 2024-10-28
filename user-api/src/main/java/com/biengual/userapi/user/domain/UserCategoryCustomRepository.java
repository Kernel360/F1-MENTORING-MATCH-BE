package com.biengual.userapi.user.domain;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User 비지니스 로직에 사용하는 QueryDsl Repository
 *
 * @author 문찬욱
 */
@Repository
@RequiredArgsConstructor
public class UserCategoryCustomRepository {
    private final JPAQueryFactory queryFactory;
    // user의 관심 Category 들을 조회하기 위한 쿼리
    public List<UserInfo.MyCategory> findAllMyCategories(Long userId) {
        QUserCategoryEntity userCategoryEntity = QUserCategoryEntity.userCategoryEntity;

        return queryFactory.select(
                Projections.constructor(
                    UserInfo.MyCategory.class,
                    userCategoryEntity.category.id,
                    userCategoryEntity.category.name
                )
            )
            .from(userCategoryEntity)
            .where(userCategoryEntity.userId.eq(userId))
            .fetch();

    }


    // user가 이미 관심 등록한 Category Id들을 조회하기 위한 쿼리
    public List<Long> findAllMyRegisteredCategoryId(Long userId) {
        QUserCategoryEntity userCategoryEntity = QUserCategoryEntity.userCategoryEntity;

        return queryFactory.select(userCategoryEntity.category.id)
            .from(userCategoryEntity)
            .where(userCategoryEntity.userId.eq(userId))
            .fetch();
    }

    // user가 관심을 취소한 Category Id들을 삭제하기 위한 쿼리
    public void deleteAllByUserIdAndCategoryIdIn(Long userId, List<Long> categoryIds) {
        QUserCategoryEntity userCategoryEntity = QUserCategoryEntity.userCategoryEntity;

        queryFactory.delete(userCategoryEntity)
            .where(userCategoryEntity.userId.eq(userId)
                .and(userCategoryEntity.category.id.in(categoryIds)))
            .execute();
    }
}
