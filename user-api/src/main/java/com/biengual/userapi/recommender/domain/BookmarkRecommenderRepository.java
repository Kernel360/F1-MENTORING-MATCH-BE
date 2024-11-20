package com.biengual.userapi.recommender.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.recommender.BookmarkRecommenderEntity;

public interface BookmarkRecommenderRepository extends JpaRepository<BookmarkRecommenderEntity, Long> {
}
