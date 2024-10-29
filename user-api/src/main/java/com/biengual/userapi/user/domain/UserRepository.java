package com.biengual.userapi.user.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.userapi.core.entity.user.UserEntity;

/**
 * UserEntity의 Repository 계층의 인터페이스
 *
 * @author 김영래
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findById(Long userId);

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByIdAndEmail(Long userId, String email);
}
