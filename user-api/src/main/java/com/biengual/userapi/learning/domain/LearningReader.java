package com.biengual.userapi.learning.domain;

import com.biengual.userapi.recommender.domain.RecommenderInfo;

/**
 * Learning 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 김영래
 */
public interface LearningReader {
    RecommenderInfo.PreviewRecommender findSimilarCategoriesBasedOnLearningHistory(Long userId);
}
