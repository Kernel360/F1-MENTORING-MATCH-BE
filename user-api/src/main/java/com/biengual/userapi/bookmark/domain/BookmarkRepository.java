package com.biengual.userapi.bookmark.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>, BookmarkRepositoryCustom {
}
