package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.learning.RecentLearningHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserLearningHistory 도메인의 Repository 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface RecentLearningHistoryRepository extends JpaRepository<RecentLearningHistoryEntity, Long> {
    Optional<RecentLearningHistoryEntity> findByUserIdAndContentId(Long userId, Long contentId);
}
