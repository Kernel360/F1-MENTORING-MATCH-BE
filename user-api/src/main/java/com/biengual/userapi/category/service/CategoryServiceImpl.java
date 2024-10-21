package com.biengual.userapi.category.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biengual.userapi.category.domain.dto.CategoryRequestDto;
import com.biengual.userapi.category.domain.dto.CategoryResponseDto;
import com.biengual.userapi.category.domain.entity.CategoryEntity;
import com.biengual.userapi.category.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<CategoryResponseDto> findAllCategory() {
		List<CategoryEntity> categories = categoryRepository.findAll();

		return categories.stream()
			.map(CategoryResponseDto::of)
			.toList();
	}

	@Override
	public CategoryResponseDto findCategoryById(Long id) {
		return CategoryResponseDto.of(categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다.")));
	}

	@Override
	public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {

		CategoryEntity newCategory = categoryRequestDto.toEntity();
		CategoryEntity savedCategory = categoryRepository.save(newCategory);

		return CategoryResponseDto.of(savedCategory);
	}

	@Override
	public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryUpdateRequestDto) {
		Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

		if (optionalCategory.isPresent()) {
			CategoryEntity category = optionalCategory.get();

			return CategoryResponseDto.of(categoryRepository.save(
				category.toBuilder()
					.name(categoryUpdateRequestDto.name())
					.build()
			));
		} else {
			throw new IllegalArgumentException("해당 ID의 카테고리를 찾을 수 없습니다.");
		}
	}


}
