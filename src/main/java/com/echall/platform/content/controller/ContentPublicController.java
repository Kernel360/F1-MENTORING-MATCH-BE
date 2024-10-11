package com.echall.platform.content.controller;

import com.echall.platform.content.domain.dto.ContentPageResponse;
import com.echall.platform.content.domain.dto.ContentResponseDto;
import com.echall.platform.content.domain.enums.ContentType;
import com.echall.platform.content.service.ContentService;
import com.echall.platform.message.ApiCustomResponse;
import com.echall.platform.message.ResponseEntityFactory;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.echall.platform.message.response.ContentResponseCode.CONTENT_VIEW_SUCCESS;

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
	@GetMapping("/view/reading")
	@Operation(summary = "리딩 컨텐츠 조회", description = "페이지네이션을 적용하여 리딩 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (0부터 시작) / default: 0", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "direction", description = "정렬 방법 / default: desc", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
	})
	public ResponseEntity<ApiCustomResponse<PaginationDto<ContentResponseDto.ContentPreviewResponseDto>>> getReadingContents(
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PaginationDto<ContentResponseDto.ContentPreviewResponseDto> pageContentList
			= contentService.getAllContents(ContentType.READING, pageable);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, pageContentList);
	}

	@GetMapping("/view/listening")
	@Operation(summary = "리스닝 컨텐츠 조회", description = "페이지네이션을 적용하여 리스닝 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (0부터 시작) / default: 0", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "direction", description = "정렬 방법 / default: desc", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
	})
	public ResponseEntity<ApiCustomResponse<PaginationDto<ContentResponseDto.ContentPreviewResponseDto>>> getListeningContents(
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		PaginationDto<ContentResponseDto.ContentPreviewResponseDto> pageContentList
			= contentService.getAllContents(ContentType.LISTENING, pageable);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, pageContentList);
	}

	@GetMapping("/preview/reading")
	@Operation(summary = "리딩 컨텐츠 프리뷰 조회", description = "리딩 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<List<ContentResponseDto.ContentPreviewResponseDto>>>
	getPreviewLeadingContents(
		@RequestParam(defaultValue = "hits") String sortBy,
		@RequestParam(defaultValue = "8") int num
	) {
		List<ContentResponseDto.ContentPreviewResponseDto> leadingPreview
			= contentService.getPreviewContents(ContentType.READING, sortBy, num);
		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, leadingPreview);
	}

	@GetMapping("/preview/listening")
	@Operation(summary = "리스닝 컨텐츠 프리뷰 조회", description = "리스닝 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
		}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<List<ContentResponseDto.ContentPreviewResponseDto>>>
	getPreviewListeningContents(
		@RequestParam(defaultValue = "hits") String sortBy,
		@RequestParam(defaultValue = "8") int num
	) {
		List<ContentResponseDto.ContentPreviewResponseDto> listeningPreviews
			= contentService.getPreviewContents(ContentType.LISTENING, sortBy, num);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, listeningPreviews);
	}

	/**
	 * 컨텐츠 상세조회
	 */
	@GetMapping("/details/{id}")
	@Operation(summary = "컨텐츠 상세 조회", description = "컨텐츠 내용을 상세 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ContentPageResponse.class))
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
