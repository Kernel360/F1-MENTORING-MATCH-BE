package com.biengual.userapi.category.controller;

import com.biengual.userapi.category.domain.dto.CategoryResponseDto;
import com.biengual.userapi.category.service.CategoryService;
import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.swagger.category.SwaggerCategory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.biengual.userapi.message.response.CategoryResponseCode.CATEGORY_FOUND_SUCCESS;

/**
 * 카테고리 공통 API
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category - public API", description = "카테고리 공통 API")
public class CategoryPublicController {

	private final CategoryService categoryService;

	@GetMapping("/all")
	@Operation(summary = "전체 카테고리 조회", description = "전체 카테고리를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerCategory.class))
		}),
		@ApiResponse(responseCode = "204", description = "카테고리가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<Object> findAllCategory() {
		Map<String, List<CategoryResponseDto>> response = new HashMap<>();
		response.put("categoryList", categoryService.findAllCategory());

		return ResponseEntityFactory.toResponseEntity(CATEGORY_FOUND_SUCCESS, response);
	}
}
