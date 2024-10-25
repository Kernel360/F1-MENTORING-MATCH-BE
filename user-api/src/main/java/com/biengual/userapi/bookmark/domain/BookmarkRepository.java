package com.biengual.userapi.bookmark.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
	Optional<BookmarkEntity> findByIdAndUserId(Long id, Long userId);
}
