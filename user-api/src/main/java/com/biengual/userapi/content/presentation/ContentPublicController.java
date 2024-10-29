package com.biengual.userapi.content.presentation;

import static com.biengual.core.constant.BadRequestMessageConstant.*;
import static com.biengual.core.response.success.ContentSuccessCode.*;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.application.ContentFacade;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentDetail;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentListeningPreview;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentListeningView;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentReadingPreview;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentReadingView;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentScrapPreview;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentSearchPreview;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;


@Validated
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "Content - public API", description = "컨텐츠 공통 API")
public class ContentPublicController {

	private final ContentDtoMapper contentDtoMapper;
	private final ContentFacade contentFacade;

	@GetMapping("/preview/scrap-count")
	@Operation(summary = "스크랩을 많이 한 컨텐츠 조회", description = "스크랩 수가 많은 순으로 정렬된 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "스크랩을 많이 한 컨텐츠 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentScrapPreview.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
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

	// TODO: 검색 키워드 개수와는 별개로 입력 받을 수 있는 글자 수는 정해야줘야 할 것 같습니다.
	// TODO: Get 요청의 공통 컨벤션은 requestBody가 없기에 keyword를 requestParam으로 옮겼고, 이렇게 쓰인다면 프론트 분들에게 공유드려야 합니다.
	@GetMapping("/search")
	@Operation(summary = "컨텐츠 검색", description = "페이지네이션을 적용해 컨텐츠를 검색합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠 검색 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentSearchPreview.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (1부터 시작) / default: 1", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "1")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string"))
	})
	public ResponseEntity<Object> searchContents(
		@RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "10") Integer size,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@NotBlank(message = BLANK_CONTENT_KEYWORD_ERROR_MESSAGE) @RequestParam String searchWords
	) {
		ContentCommand.Search command = contentDtoMapper.doSearch(page, size, direction, sort, searchWords);
		PaginationInfo<ContentInfo.PreviewContent> info = contentFacade.search(command);
		ContentResponseDto.SearchPreviewContentsRes response = contentDtoMapper.ofSearchPreviewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	// TODO: Approve가 된다면 Page 내용을 담는 key 값 변경 사항을 프론트에게 공유해야 합니다.
	@GetMapping("/preview/paginated-reading")
	@Operation(summary = "리딩 컨텐츠 프리뷰 페이지 조회", description = "페이지네이션을 적용하여 리딩 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리딩 컨텐츠 프리뷰 페이지 조회 요청 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentReadingView.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (1부터 시작) / default: 1", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "1")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "categoryId", description = "category Id (값이 없으면 전체 카테고리)", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
	})
	public ResponseEntity<Object> getReadingView(
		@RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "10") Integer size,
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@RequestParam(required = false) Long categoryId
	) {
		ContentCommand.GetReadingView command =
			contentDtoMapper.doGetReadingView(page, size, direction, sort, categoryId);
		PaginationInfo<ContentInfo.ViewContent> info = contentFacade.getReadingView(command);
		ContentResponseDto.ReadingViewContentsRes response = contentDtoMapper.ofReadingViewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	// TODO: Approve가 된다면 Page 내용을 담는 key 값 변경 사항을 프론트에게 공유해야 합니다.
	@GetMapping("/preview/paginated-listening")
	@Operation(summary = "리스닝 컨텐츠 프리뷰 페이지 조회", description = "페이지네이션을 적용하여 리스닝 컨텐츠 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리스닝 컨텐츠 프리뷰 페이지 조회 요청 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentListeningView.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	@Parameters({
		@Parameter(name = "page", description = "페이지 번호 (1부터 시작) / default: 1", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "1")),
		@Parameter(name = "size", description = "페이지당 데이터 수 / default: 10", in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10")),
		@Parameter(name = "direction", description = "정렬 방법 / default: DESC / 대문자로 입력", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "sort", description = "정렬 기준 (createdAt, hits) / default: createdAt", in = ParameterIn.QUERY, schema = @Schema(type = "string")),
		@Parameter(name = "categoryId", description = "category Id (값이 없으면 전체 카테고리)", in = ParameterIn.QUERY, schema = @Schema(type = "integer"))
	})
	public ResponseEntity<Object> getListeningView(
		@RequestParam(required = false, defaultValue = "1") Integer page,
		@RequestParam(required = false, defaultValue = "10") Integer size,
		@RequestParam(required = false, defaultValue = "createdAt") String sort,
		@RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
		@RequestParam(required = false) Long categoryId
	) {
		ContentCommand.GetListeningView command =
			contentDtoMapper.doGetListeningView(page, size, direction, sort, categoryId);
		PaginationInfo<ContentInfo.ViewContent> info = contentFacade.getListeningView(command);
		ContentResponseDto.ListeningViewContentsRes response = contentDtoMapper.ofListeningViewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	@GetMapping("/preview/reading")
	@Operation(summary = "리딩 컨텐츠 프리뷰 조회", description = "리딩 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리딩 컨텐츠 프리뷰 조회 요청 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentReadingPreview.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getPreviewLeadingContents(
		@RequestParam(defaultValue = "8") Integer size,
		@RequestParam(defaultValue = "hits") String sort
	) {
		ContentCommand.GetReadingPreview command = contentDtoMapper.doGetReadingPreview(size, sort);
		ContentInfo.PreviewContents info = contentFacade.getReadingPreview(command);
		ContentResponseDto.ReadingPreviewContentsRes response = contentDtoMapper.ofReadingPreviewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	@GetMapping("/preview/listening")
	@Operation(summary = "리스닝 컨텐츠 프리뷰 조회", description = "리스닝 컨텐츠 프리뷰 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "리스닝 컨텐츠 프리뷰 조회 요청 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentListeningPreview.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getPreviewListeningContents(
		@RequestParam(defaultValue = "8") Integer size,
		@RequestParam(defaultValue = "hits") String sort
	) {
		ContentCommand.GetListeningPreview command = contentDtoMapper.doGetListeningPreview(size, sort);
		ContentInfo.PreviewContents info = contentFacade.getListeningPreview(command);
		ContentResponseDto.ListeningPreviewContentsRes response = contentDtoMapper.ofListeningPreviewContentsRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

	/**
	 * 컨텐츠 상세조회
	 */
	@GetMapping("/details/{contentId}")
	@Operation(summary = "컨텐츠 상세 조회", description = "컨텐츠 내용을 상세 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠 상세 조회 요청 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentDetail.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getDetailContent(
		@PathVariable Long contentId
	) {
		ContentInfo.Detail info = contentFacade.getDetailContent(contentId);
		ContentResponseDto.DetailRes response = contentDtoMapper.ofDetailRes(info);

		return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
	}

}
