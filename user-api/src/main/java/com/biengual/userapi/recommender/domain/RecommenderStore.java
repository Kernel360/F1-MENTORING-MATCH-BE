package com.biengual.userapi.recommender.domain;

public interface RecommenderStore {
    void createAndUpdateCategoryRecommender();

    void createLastWeekBookmarkRecommender();
}
