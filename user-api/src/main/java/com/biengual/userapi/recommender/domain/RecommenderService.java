package com.biengual.userapi.recommender.domain;

public interface RecommenderService {
    RecommenderInfo.PreviewRecommender getRecommendedContentsByCategory(Long userId);

    void updateCategoryRecommender();

    RecommenderInfo.PopularBookmarkRecommender getPopularBookmarks();

    void updatePopularBookmarks();

    RecommenderInfo.PreviewRecommender getNewRecommendedContentsByCategory(Long userId);
}
