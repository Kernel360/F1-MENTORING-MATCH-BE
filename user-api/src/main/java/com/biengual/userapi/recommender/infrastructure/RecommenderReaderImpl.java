package com.biengual.userapi.recommender.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DataProvider
@RequiredArgsConstructor
public class RecommenderReaderImpl implements RecommenderReader {
    private final RecommenderCustomRepository recommenderCustomRepository;
    private final ContentCustomRepository contentCustomRepository;

    @Override
    public List<RecommenderInfo.PopularBookmark> findPopularBookmarks() {
        LocalDate lastWeek =
            PeriodUtil.getFewWeeksAgo(LocalDate.from(LocalDateTime.now()), 1, DayOfWeek.MONDAY);

        LocalDateTime startOfWeek = PeriodUtil.getStartOfWeek(lastWeek);
        LocalDateTime endOfWeek = PeriodUtil.getEndOfWeek(lastWeek);

        return recommenderCustomRepository.findPopularBookmarks(startOfWeek, endOfWeek);
    }

    @Override
    public RecommenderInfo.PreviewRecommender findContents(Long userId, Set<Long> contentIds) {
        return RecommenderInfo.PreviewRecommender
            .of(contentCustomRepository.findRecommendedContentsIn(userId, contentIds));
    }
}
