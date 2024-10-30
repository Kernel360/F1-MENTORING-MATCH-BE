package com.biengual.userapi.bookmark.domain;

import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
	Optional<BookmarkEntity> findByIdAndUserId(Long id, Long userId);

	List<BookmarkEntity> findAllByUserIdAndScriptIndex(Long userId, Long scriptIndex);
}
