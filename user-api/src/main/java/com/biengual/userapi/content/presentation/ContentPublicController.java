package com.biengual.userapi.content.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.content.presentation.dto.SubmitLevelFeedbackDto;
import com.biengual.userapi.content.presentation.swagger.*;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.biengual.core.constant.BadRequestMessageConstant.BLANK_CONTENT_KEYWORD_ERROR_MESSAGE;
import static com.biengual.core.response.success.ContentSuccessCode.CONTENT_LEVEL_FEEDBACK_SUBMIT_SUCCESS;
import static com.biengual.core.response.success.ContentSuccessCode.CONTENT_VIEW_SUCCESS;

@Validated
@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "Content - public API", description = "컨텐츠 공통 API")
public class ContentPublicController {

    private final ContentDtoMapper contentDtoMapper;
    private final ContentService contentService;

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
        Integer size,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetScrapPreview command = contentDtoMapper.doGetScrapPreview(size, principal);
        ContentInfo.PreviewContents info = contentService.getContentsByScrapCount(command);
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
        @NotBlank(message = BLANK_CONTENT_KEYWORD_ERROR_MESSAGE) @RequestParam String searchWords,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.Search command = contentDtoMapper.doSearch(
            page, size, direction, sort, searchWords, principal
        );
        PaginationInfo<ContentInfo.PreviewContent> info = contentService.search(command);
        ContentResponseDto.SearchPreviewContentsRes response = contentDtoMapper.ofSearchPreviewContentsRes(info);

        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }

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
        @RequestParam(required = false) Long categoryId,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetReadingView command =
            contentDtoMapper.doGetReadingView(page, size, direction, sort, categoryId, principal);
        PaginationInfo<ContentInfo.ViewContent> info = contentService.getViewContents(command);
        ContentResponseDto.ReadingViewContentsRes response = contentDtoMapper.ofReadingViewContentsRes(info);

        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }

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
        @RequestParam(required = false) Long categoryId,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetListeningView command =
            contentDtoMapper.doGetListeningView(page, size, direction, sort, categoryId, principal);
        PaginationInfo<ContentInfo.ViewContent> info = contentService.getViewContents(command);
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
        @RequestParam(defaultValue = "hits") String sort,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetReadingPreview command = contentDtoMapper.doGetReadingPreview(size, sort, principal);
        ContentInfo.PreviewContents info = contentService.getPreviewContents(command);
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
        @RequestParam(defaultValue = "hits") String sort,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetListeningPreview command = contentDtoMapper.doGetListeningPreview(size, sort, principal);
        ContentInfo.PreviewContents info = contentService.getPreviewContents(command);
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
        @ApiResponse(responseCode = "404", description = "유저 조회 실패, 포인트 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "405", description = "포인트 부족", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    public ResponseEntity<Object> getDetailContent(
        @PathVariable Long contentId,
        @AuthenticationPrincipal OAuth2UserPrincipal principal
    ) {
        ContentCommand.GetDetail command = contentDtoMapper.doGetDetail(contentId, principal);
        ContentInfo.Detail info = contentService.getScriptsOfContent(command);
        ContentResponseDto.DetailRes response = contentDtoMapper.ofDetailRes(info);

        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }

    @PostMapping("/feedback/level")
    @Operation(summary = "컨텐츠 난이도 피드백", description = "회원이 컨텐츠에 대한 난이도를 피드백합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "컨텐츠 난이도 피드백 요청 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
        }),
        @ApiResponse(responseCode = "400", description = "이미 제출한 해당 컨텐츠의 난이도 피드백", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "비활성화 컨텐츠", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "포인트를 지불하지 않은 최신 컨텐츠", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    public ResponseEntity<Object> submitLevelFeedback(
        @AuthenticationPrincipal OAuth2UserPrincipal principal,
        @RequestBody SubmitLevelFeedbackDto.Request request
    ) {
        ContentCommand.SubmitLevelFeedback command = contentDtoMapper.doSubmitLevelFeedback(request, principal);
        contentService.submitLevelFeedback(command);
        return ResponseEntityFactory.toResponseEntity(CONTENT_LEVEL_FEEDBACK_SUBMIT_SUCCESS);
    }
}
