package com.echall.platform.content.controller;

import static com.echall.platform.message.response.ContentResponseCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.service.ContentService;
import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;
import com.echall.platform.swagger.content.SwaggerContentByScrapCount;
import com.echall.platform.swagger.content.SwaggerContentDetail;
import com.echall.platform.swagger.content.SwaggerContentPreview;
import com.echall.platform.util.PaginationDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contents")
@Tag(name = "Content - public API", description = "컨텐츠 공통 API")
public class ContentPublicController {

	private final ContentService contentService;

	/**
	 * 컨텐츠 조회
	 * (pageable)
	 */
	@GetMapping("/view/scrap-count")
	@Operation(summary = "컨텐츠 조회", description = "정렬된 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentByScrapCount.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String,List<ContentResponseDto.ContentByScrapCountDto>>>>
	getContentsByScrapCount(@RequestParam(defaultValue = "8") int num) {
		Map<String, List<ContentResponseDto.ContentByScrapCountDto>> data = new HashMap<>();
		data.put("contentByScrapCount", contentService.contentByScrapCount(num));
		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, data);
	}

	@GetMapping("/view/reading")
	@Operation(summary = "리딩 컨텐츠 조회", description = "페이지네이션을 적용하여 리딩 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentPreview.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (0부터 시작) / default: 0", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "categoryId", description = "category Id (값이 없으면 전체 카테고리)", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
	})
	public ResponseEntity<ApiCustomResponse<PaginationDto<ContentResponseDto.ContentPreviewResponseDto>>> getReadingContents(
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam(required = false) Long categoryId
	) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), direction, sort);
		PaginationDto<ContentResponseDto.ContentPreviewResponseDto> pageContentList
			= contentService.getAllContents(ContentType.READING, pageRequest, categoryId);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, pageContentList);
	}

	@GetMapping("/view/listening")
	@Operation(summary = "리스닝 컨텐츠 조회", description = "페이지네이션을 적용하여 리스닝 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentPreview.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (0부터 시작) / default: 0", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "categoryId", description = "category Id (값이 없으면 전체 카테고리)", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
	})
	public ResponseEntity<ApiCustomResponse<PaginationDto<ContentResponseDto.ContentPreviewResponseDto>>>
	getListeningContents(
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10) Pageable pageable,
		@RequestParam(required = false) Long categoryId
	) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), direction, sort);
		PaginationDto<ContentResponseDto.ContentPreviewResponseDto> pageContentList
			= contentService.getAllContents(ContentType.LISTENING, pageRequest, categoryId);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, pageContentList);
	}

	@GetMapping("/preview/reading")
	@Operation(summary = "리딩 컨텐츠 프리뷰 조회", description = "리딩 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentPreview.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<ContentResponseDto.ContentPreviewResponseDto>>>>
	getPreviewLeadingContents(
		@RequestParam(defaultValue = "hits") String sortBy,
		@RequestParam(defaultValue = "8") int num
	) {
		Map<String, List<ContentResponseDto.ContentPreviewResponseDto>> data = new HashMap<>();
		data.put("readingPreview", contentService.findPreviewContents(ContentType.READING, sortBy, num));

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, data);
	}

	@GetMapping("/preview/listening")
	@Operation(summary = "리스닝 컨텐츠 프리뷰 조회", description = "리스닝 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentPreview.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<ContentResponseDto.ContentPreviewResponseDto>>>>
	getPreviewListeningContents( // 최소한 list, map은 객체로 만들어야 함
		@RequestParam(defaultValue = "hits") String sortBy,
		@RequestParam(defaultValue = "8") int num
	) {
		Map<String, List<ContentResponseDto.ContentPreviewResponseDto>> data = new HashMap<>();
		data.put("listeningPreview", contentService.findPreviewContents(ContentType.LISTENING, sortBy, num));

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, data);
	}

	/**
	 * 컨텐츠 상세조회
	 */
	@GetMapping("/details/{id}")
	@Operation(summary = "컨텐츠 상세 조회", description = "컨텐츠 내용을 상세 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentDetail.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<ContentResponseDto.ContentDetailResponseDto>> getDetailContents(
		@PathVariable Long id
	) {

		ContentResponseDto.ContentDetailResponseDto scriptsOfContent = contentService.getScriptsOfContent(id);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, scriptsOfContent);
	}

}
