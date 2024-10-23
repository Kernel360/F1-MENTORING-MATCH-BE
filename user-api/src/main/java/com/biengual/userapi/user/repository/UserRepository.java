package com.biengual.userapi.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.userapi.user.domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByIdAndEmail(Long userId, String email);
}
