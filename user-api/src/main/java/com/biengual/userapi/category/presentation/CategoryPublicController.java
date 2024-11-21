package com.biengual.userapi.category.presentation;

import static com.biengual.core.response.success.CategorySuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.enums.ContentType;
import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.category.application.CategoryFacade;
import com.biengual.userapi.category.domain.CategoryInfo;
import com.biengual.userapi.category.domain.CategoryService;
import com.biengual.userapi.category.presentation.swagger.SwaggerCategory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


/**
 * 카테고리 공통 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category - public API", description = "카테고리 공통 API")
public class CategoryPublicController {

	private final CategoryDtoMapper categoryDtoMapper;
	private final CategoryFacade categoryFacade;
	private final CategoryService categoryService;

	@GetMapping("/all")
	@Operation(summary = "모든 카테고리 조회", description = "서비스에 존재하는 모든 카테고리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 카테고리 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerCategory.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getAllCategories() {
		CategoryInfo.AllCategories info = categoryFacade.getAllCategories();
		CategoryResponseDto.AllCategoriesRes response = categoryDtoMapper.ofAllCategoriesRes(info);

		return ResponseEntityFactory.toResponseEntity(CATEGORY_FOUND_SUCCESS, response);
	}

	@GetMapping("/type/{contentType}")
	@Operation(summary = "컨텐츠 타입의 모든 카테고리 조회", description = "컨텐츠 타입에 따른 모든 카테고리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐트 타입의 카테고리 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerCategory.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getCategoriesByContentType(
		@PathVariable
		ContentType contentType
	) {
		CategoryInfo.AllCategories info = categoryService.getCategoriesByContentType(contentType);
		CategoryResponseDto.AllCategoriesRes response = categoryDtoMapper.ofAllCategoriesRes(info);

		return ResponseEntityFactory.toResponseEntity(CATEGORY_FOUND_SUCCESS, response);
	}
}
