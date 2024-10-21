package com.biengual.userapi.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.userapi.bookmark.domain.entity.BookmarkEntity;
import com.biengual.userapi.bookmark.repository.custom.BookmarkRepositoryCustom;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long>, BookmarkRepositoryCustom {
}
