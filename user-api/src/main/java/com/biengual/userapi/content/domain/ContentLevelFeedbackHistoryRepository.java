package com.biengual.userapi.content.domain;

import com.biengual.core.domain.entity.content.ContentLevelFeedbackHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentLevelFeedbackHistoryRepository extends JpaRepository<ContentLevelFeedbackHistoryEntity, Long> {
    boolean existsByUserIdAndContentId(Long userId, Long contentId);
}
