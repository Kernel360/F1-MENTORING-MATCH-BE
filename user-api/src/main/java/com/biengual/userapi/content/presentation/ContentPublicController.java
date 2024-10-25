package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.application.ContentFacade;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.swagger.content.SwaggerContentByScrapCount;
import com.biengual.userapi.swagger.content.SwaggerContentDetail;
import com.biengual.userapi.swagger.content.SwaggerContentPreview;
import com.biengual.userapi.util.PaginationDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.biengual.userapi.message.response.ContentResponseCode.CONTENT_VIEW_SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/contents")
@Tag(name = "Content - public API", description = "컨텐츠 공통 API")
public class ContentPublicController {

	private final ContentService contentService;
	private final ContentDtoMapper contentDtoMapper;
	private final ContentFacade contentFacade;

	// 메인 화면에서 사용하는 스크랩 많은 순으로 컨텐츠 조회
	@GetMapping("/preview/scrap-count")
	@Operation(summary = "스크랩을 많이 한 컨텐츠 조회", description = "스크랩 수가 많은 순으로 정렬된 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스크랩을 많이 한 컨텐츠 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentByScrapCount.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	@Parameters({
		@Parameter(name = "size", description = "컨텐츠 수 / default: 8", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "8")),
	})
	public ResponseEntity<Object> getContentsByScrapCount(
		@RequestParam(defaultValue = "8")
		Integer size
	) {
		ContentInfo.PreviewContents info = contentFacade.getContentsByScrapCount(size);
		ContentResponseDto.ScrapPreviewContentsRes response = contentDtoMapper.ofScrapPreviewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	@GetMapping("/search")
	@Operation(summary = "컨텐츠 검색", description = "페이지네이션을 적용해 컨텐츠를 검색합니다.")
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
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
	})
	public ResponseEntity<Object>
	getContentsBySearch(
		@ModelAttribute ContentRequestDto.ContentSearchDto searchDto,
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@Parameter(hidden = true) @PageableDefault(page = 0, size = 10) Pageable pageable
	) {
		Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), direction, sort);
		PaginationDto<ContentResponseDto.ContentPreviewResponseDto> pageContentList
			= contentService.search(searchDto, pageRequest);
		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, pageContentList);
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
	public ResponseEntity<Object> getReadingContents(
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
	public ResponseEntity<Object>
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
	public ResponseEntity<Object>
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
	public ResponseEntity<Object>
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
	public ResponseEntity<Object> getDetailContents(
		@PathVariable Long id
	) {

		ContentResponseDto.ContentDetailResponseDto scriptsOfContent = contentService.getScriptsOfContent(id);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, scriptsOfContent);
	}

}
