package com.biengual.userapi.recommender.infrastructure;

import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.recommender.CategoryRecommenderEntity;
import com.biengual.userapi.category.domain.CategoryCustomRepository;
import com.biengual.userapi.recommender.domain.CategoryRecommenderRepository;
import com.biengual.userapi.recommender.domain.RecommenderCustomRepository;
import com.biengual.userapi.recommender.domain.RecommenderStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class RecommenderStoreImpl implements RecommenderStore {
    private final CategoryRecommenderRepository categoryRecommenderRepository;
    private final CategoryCustomRepository categoryCustomRepository;
    private final RecommenderCustomRepository recommenderCustomRepository;

    @Override
    public void createAndUpdateCategoryRecommender() {
        // CategoryRecommender 에 추가되지 않은 카테고리 있는지 확인 및 추가
        this.createdCategoryRecommender();

        // CategoryRecommender 의 similarCategoryIds 업데이트
        recommenderCustomRepository.updateCategoryRecommender();
    }

    // Internal Methods=================================================================================================
    private void createdCategoryRecommender() {
        List<Long> notUpdatedCategoryIds = categoryCustomRepository.findAllCategoryIds()
            .stream()
            .filter(id -> !categoryRecommenderRepository.existsByCategoryId(id))
            .toList();

        if (!notUpdatedCategoryIds.isEmpty()) {
            List<CategoryRecommenderEntity> categoryRecommenderEntities =
                notUpdatedCategoryIds
                    .stream()
                    .map(CategoryRecommenderEntity::createdByCategoryId)
                    .toList();

            categoryRecommenderRepository.saveAll(categoryRecommenderEntities);
        }
    }
}
