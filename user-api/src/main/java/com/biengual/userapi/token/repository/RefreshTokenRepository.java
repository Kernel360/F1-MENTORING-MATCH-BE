package com.biengual.userapi.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.jwt.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUserId(Long userId);

	void deleteByUserId(Long userId);
}
