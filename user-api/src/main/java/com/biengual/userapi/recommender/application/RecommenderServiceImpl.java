package com.biengual.userapi.recommender.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.learning.domain.LearningReader;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderService;
import com.biengual.userapi.recommender.domain.RecommenderStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommenderServiceImpl implements RecommenderService {
    private final LearningReader learningReader;
    private final RecommenderStore recommenderStore;

    @Override
    @Transactional(readOnly = true)
    public RecommenderInfo.PreviewRecommender getRecommendedContentsByCategory(Long userId) {
        return learningReader.findSimilarCategoriesBasedOnLearningHistory(userId);
    }

    @Override
    @Transactional
    public void updateCategoryRecommender() {
        recommenderStore.createAndUpdateCategoryRecommender();
    }

}
