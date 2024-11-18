package com.biengual.userapi.recommender.domain;

public interface RecommenderService {
    RecommenderInfo.PreviewRecommender getRecommendedContentsByCategory(Long userId);

    void updateCategoryRecommender();
}
