package com.biengual.userapi.recommender.domain;

import java.util.List;

public interface RecommenderReader {
    List<RecommenderInfo.PopularBookmark> findPopularBookmarks();
}
