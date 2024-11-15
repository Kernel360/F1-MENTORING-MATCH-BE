package com.biengual.userapi.domain.learning;

import com.biengual.core.domain.entity.learning.CategoryLearningHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 테스트에서 사용할 카테고리별 학습 내역 Repository 메서드를 위한 인터페이스
 *
 * @author 문찬욱
 */
public interface TestCategoryLearningHistoryRepository extends JpaRepository<CategoryLearningHistoryEntity, Long> {
    List<CategoryLearningHistoryEntity> findAllByUserId(Long userId);
}
