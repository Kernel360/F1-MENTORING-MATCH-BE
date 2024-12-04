package com.biengual.userapi.recommender.infrastructure;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.learning.CategoryLearningProgressEntity;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.recommender.domain.CategoryLearningProgressRepository;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class RecommenderReaderImpl implements RecommenderReader {
    private final RecommenderCustomRepository recommenderCustomRepository;
    private final CategoryLearningProgressRepository categoryLearningProgressRepository;

    @Override
    public List<RecommenderInfo.PopularBookmark> findPopularBookmarks() {
        LocalDate lastWeek =
            PeriodUtil.getFewWeeksAgo(LocalDate.from(LocalDateTime.now()), 1, DayOfWeek.MONDAY);

        LocalDateTime startOfWeek = PeriodUtil.getStartOfWeek(lastWeek);
        LocalDateTime endOfWeek = PeriodUtil.getEndOfWeek(lastWeek);

        return recommenderCustomRepository.findPopularBookmarks(startOfWeek, endOfWeek);
    }

    // 컨텐츠 추천에 사용할 벡터를 찾는 메서드
    @Override
    public RecommenderInfo.ContentRecommenderVector findContentRecommenderVector(int vectorSize) {
        List<CategoryLearningProgressEntity> categoryLearningProgressList = categoryLearningProgressRepository.findAll();

        Map<Long, Long[]> vectorMap = new HashMap<>();

        for (CategoryLearningProgressEntity categoryLearningProgress : categoryLearningProgressList) {
            Long userId = categoryLearningProgress.getCategoryLearningProgressId().getUserId();
            Long categoryId = categoryLearningProgress.getCategoryLearningProgressId().getCategoryId();
            Long totalLearningCount = categoryLearningProgress.getTotalLearningCount();
            Long completedLearningCount = categoryLearningProgress.getCompletedLearningCount();

            Long[] vector = vectorMap.getOrDefault(userId, new Long[vectorSize]);

            int index = Math.toIntExact(categoryId);

            vector[index] = totalLearningCount;
            vector[vectorSize - 1] += completedLearningCount;

            vectorMap.put(userId, vector);
        }

        return RecommenderInfo.ContentRecommenderVector.of(vectorMap);
    }
}
