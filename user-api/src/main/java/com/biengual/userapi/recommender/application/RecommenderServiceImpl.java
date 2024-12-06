package com.biengual.userapi.recommender.application;

import com.biengual.userapi.learning.domain.LearningReader;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;
import com.biengual.userapi.recommender.domain.RecommenderService;
import com.biengual.userapi.recommender.domain.RecommenderStore;
import com.biengual.userapi.validator.RecommenderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.biengual.core.constant.RestrictionConstant.MAX_RECOMMENDED_CONTENT_COUNT;

@Service
@RequiredArgsConstructor
public class RecommenderServiceImpl implements RecommenderService {
    private final LearningReader learningReader;
    private final RecommenderStore recommenderStore;
    private final RecommenderReader recommenderReader;

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

    @Override
    @Transactional(readOnly = true)
    public RecommenderInfo.PopularBookmarkRecommender getPopularBookmarks() {
        return RecommenderInfo.PopularBookmarkRecommender.of(recommenderReader.findPopularBookmarks());
    }

    @Override
    @Transactional
    public void updatePopularBookmarks() {
        recommenderStore.createLastWeekBookmarkRecommender();
    }

    @Override
    @Transactional(readOnly = true)
    public RecommenderInfo.PreviewRecommender getNewRecommendedContentsByCategory(Long userId) {
        Set<Long> recommendedContentIdSet = recommenderReader.findRecommendedContentIdSet(userId);

        return recommenderReader.findContents(userId, recommendedContentIdSet);
    }
}
