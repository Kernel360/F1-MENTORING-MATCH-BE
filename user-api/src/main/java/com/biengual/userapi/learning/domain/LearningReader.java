package com.biengual.userapi.learning.domain;

import com.biengual.userapi.recommender.domain.RecommenderInfo;

public interface LearningReader {
    RecommenderInfo.PreviewRecommender findSimilarCategoriesBasedOnLearningHistory(Long userId);
}
