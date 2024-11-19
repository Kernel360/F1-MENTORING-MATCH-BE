package com.biengual.userapi.recommender.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.recommender.CategoryRecommenderEntity;

public interface CategoryRecommenderRepository extends JpaRepository<CategoryRecommenderEntity, Long> {

    boolean existsByCategoryId(Long categoryId);

}
