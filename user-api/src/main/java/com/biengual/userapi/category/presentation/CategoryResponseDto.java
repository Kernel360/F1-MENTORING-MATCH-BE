package com.biengual.userapi.category.presentation;

import lombok.Builder;

import java.util.List;

public class CategoryResponseDto {

	public record Category(
		Long id,
		String name
	) {
	}

	@Builder
	public record AllCategoriesRes(
		List<Category> categoryList
	) {
	}
}
