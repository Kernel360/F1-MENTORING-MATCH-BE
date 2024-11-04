package com.biengual.userapi.user.domain;

import static com.biengual.core.domain.entity.user.QUserEntity.*;

import java.time.LocalDateTime;
import java.util.Optional;

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
public class UserCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 서비스에 사용하는 user 정보를 조회하기 위한 쿼리
    public Optional<UserInfo.MyInfoExceptMyCategories> findMyInfoExceptMyCategories(Long userId) {
        UserInfo.MyInfoExceptMyCategories myInfoExceptMyCategories = queryFactory.select(
                Projections.constructor(
                    UserInfo.MyInfoExceptMyCategories.class,
                    userEntity.username,
                    userEntity.nickname,
                    userEntity.email,
                    userEntity.phoneNumber,
                    userEntity.birth,
                    userEntity.gender
                )
            )
            .from(userEntity)
            .where(userEntity.id.eq(userId))
            .fetchOne();

        return Optional.ofNullable(myInfoExceptMyCategories);
    }

    // user의 생성 날짜 및 수정 날짜를 조회하는 쿼리
    public Optional<UserInfo.MySignUpTime> findMySignUpTime(Long userId) {
        UserInfo.MySignUpTime mySignUpTime = queryFactory.select(
                Projections.constructor(
                    UserInfo.MySignUpTime.class,
                    userEntity.createdAt,
                    userEntity.updatedAt
                )
            )
            .from(userEntity)
            .where(userEntity.id.eq(userId))
            .fetchOne();

        return Optional.ofNullable(mySignUpTime);
    }

    // 마지막 로그인 시간을 조회하는 쿼리
    public Optional<LocalDateTime> findLastLoginTime(Long userId) {
        return Optional.ofNullable(
            queryFactory
                .select(userEntity.lastLoginTime)
                .from(userEntity)
                .where(userEntity.id.eq(userId))
                .fetchOne()
        );
    }

    // 마지막 로그인 시간 업데이트하는 쿼리
    public void updateLastLoginTime(Long userId) {
        queryFactory
            .update(userEntity)
            .set(userEntity.lastLoginTime, LocalDateTime.now())
            .where(userEntity.id.eq(userId))
            .execute();
    }
}
