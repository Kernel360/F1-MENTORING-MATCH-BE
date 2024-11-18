package com.biengual.core.domain.entity.recommender;

import java.util.Collections;
import java.util.List;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.util.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category_recommender")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRecommenderEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long categoryId;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false, columnDefinition = "varchar(512)")
    private List<String> similarCategoryIds;

    @Builder
    public CategoryRecommenderEntity(Long id, Long categoryId, List<String> similarCategoryIds) {
        this.id = id;
        this.categoryId = categoryId;
        this.similarCategoryIds = similarCategoryIds;
    }

    public static CategoryRecommenderEntity createdByCategoryId(Long categoryId) {
        return CategoryRecommenderEntity.builder()
            .categoryId(categoryId)
            .similarCategoryIds(Collections.emptyList())
            .build();
    }
}
