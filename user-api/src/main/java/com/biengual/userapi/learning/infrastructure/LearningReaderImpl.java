package com.biengual.userapi.learning.infrastructure;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.learning.domain.LearningReader;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.user.domain.UserCategoryCustomRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class LearningReaderImpl implements LearningReader {
    private final UserCategoryCustomRepository userCategoryCustomRepository;
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;
    private final ContentCustomRepository contentCustomRepository;
    private final RecommenderCustomRepository recommenderCustomRepository;


    @Override
    public RecommenderInfo.PreviewRecommender findSimilarCategoriesBasedOnLearningHistory(Long userId) {
        // 1. 유저의 관심 높은 카테고리 찾기(관심 카테고리, 최근 많이 학습한 카테고리)
        List<Long> userSelectedCategoryIds = userCategoryCustomRepository.findAllMyRegisteredCategoryId(userId);
        List<Long> recentLearningCategoryIdsInMonth =
            recentLearningHistoryCustomRepository.findRecentlyFrequentCategoryIds(userId);

        // 2. recentLearningCategoryIdsInMonth 기반으로 userSelectedCategoryIds 와 병합
        List<Long> interestedCategoryIds =
            this.mergeRecommendedCategories(userSelectedCategoryIds, recentLearningCategoryIdsInMonth);

        // 3. 위 카테고리를 학습한 유저들의 다른 카테고리 리턴(나는 많이 학습하지 않은)
        List<Long> similarCategories =  recommenderCustomRepository.findSimilarCategories(interestedCategoryIds);

        // 4. 각 카테고리 ID 당 조회수, 스크랩수 기준으로 상위 컨텐츠 리턴
        return RecommenderInfo.PreviewRecommender.of(contentCustomRepository.findCustomizedContentsByCategories(userId, similarCategories));
    }

    // Internal Methods ================================================================================================
    private List<Long> mergeRecommendedCategories(
        List<Long> userSelectedCategoryIds, List<Long> recentLearningCategoryIdsInMonth
    ) {
        // 중복 제거를 위해 Set으로 변환
        Set<Long> userSelectedSet = new LinkedHashSet<>(userSelectedCategoryIds);

        // 결과를 저장할 리스트
        List<Long> mergedList = new ArrayList<>();

        // userSelectedCategoryIds와 겹치는 항목을 먼저 추가
        for (Long id : recentLearningCategoryIdsInMonth) {
            if (userSelectedSet.contains(id)) {
                mergedList.add(id);
                userSelectedSet.remove(id);
            }
        }

        // userSelectedSet 에 남아 있는 항목들을 추가
        mergedList.addAll(userSelectedSet);

        // recentLearningCategoryIdsInMonth 의 나머지 항목들을 추가
        for (Long id : recentLearningCategoryIdsInMonth) {
            if (!mergedList.contains(id)) {
                mergedList.add(id);
            }
        }

        return (mergedList.size() > 3) ? mergedList.subList(0, 3) : mergedList;
    }
}
