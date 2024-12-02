package com.biengual.userapi.recommender.infrastructure;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class RecommenderReaderImpl implements RecommenderReader {
    private final RecommenderCustomRepository recommenderCustomRepository;

    @Override
    public List<RecommenderInfo.PopularBookmark> findPopularBookmarks() {
        LocalDate lastWeek =
            PeriodUtil.getFewWeeksAgo(LocalDate.from(LocalDateTime.now()), 1, DayOfWeek.MONDAY);

        LocalDateTime startOfWeek = PeriodUtil.getStartOfWeek(lastWeek);
        LocalDateTime endOfWeek = PeriodUtil.getEndOfWeek(lastWeek);

        return recommenderCustomRepository.findPopularBookmarks(startOfWeek, endOfWeek);
    }
}
