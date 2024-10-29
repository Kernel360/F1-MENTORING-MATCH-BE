package com.biengual.userapi.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.user.UserCategoryEntity;

/**
 * UserCategoryEntity의 Repository 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserCategoryRepository extends JpaRepository<UserCategoryEntity, Long> {
}
