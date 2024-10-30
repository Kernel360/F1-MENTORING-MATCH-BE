package com.biengual.userapi.user.domain;

import static com.biengual.core.domain.entity.user.QUserCategoryEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
        return queryFactory.select(userCategoryEntity.category.id)
            .from(userCategoryEntity)
            .where(userCategoryEntity.userId.eq(userId))
            .fetch();
    }

    // user가 관심을 취소한 Category Id들을 삭제하기 위한 쿼리
    public void deleteAllByUserIdAndCategoryIdIn(Long userId, List<Long> categoryIds) {
        queryFactory.delete(userCategoryEntity)
            .where(userCategoryEntity.userId.eq(userId)
                .and(userCategoryEntity.category.id.in(categoryIds)))
            .execute();
    }
}
