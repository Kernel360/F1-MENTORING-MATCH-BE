package com.biengual.userapi.recommender.domain;

import java.util.List;

/**
 * Recommender 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 김영래
 */
public interface RecommenderReader {
    List<RecommenderInfo.PopularBookmark> findPopularBookmarks();

    RecommenderInfo.ContentRecommenderMetric findContentRecommenderVector(int vectorSize);
}
