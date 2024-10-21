package com.biengual.userapi.category.domain.dto;

import com.biengual.userapi.category.domain.entity.CategoryEntity;

public record CategoryResponseDto(
	Long id,
	String name
) {
	public CategoryResponseDto(CategoryEntity category) {
		this(
			category.getId(),
			category.getName()
		);
	}

	public static CategoryResponseDto of(CategoryEntity category) {
		return new CategoryResponseDto(category);
	}
}
