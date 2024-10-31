package com.biengual.userapi.content.presentation;

import static com.biengual.core.response.success.ContentSuccessCode.*;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.application.ContentFacade;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.presentation.swagger.SwaggerContentAdminView;

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

/**
 * admin, 회원 전용 컨텐츠 controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/contents")
@Tag(name = "Content - private API", description = "컨텐츠 어드민 전용 API")
public class ContentApiController {
    private final ContentFacade contentFacade;
    private final ContentDtoMapper contentDtoMapper;

    /**
     * 컨텐츠 등록
     */
    @PostMapping("/create")
    @Operation(summary = "어드민 - 컨텐츠 등록", description = "어드민 회원이 컨텐츠를 새로 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "컨텐츠 생성 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
        ),
        @ApiResponse(responseCode = "400", description = "크롤링 불가능한 길이 영상", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "406", description = "크롤링 Selenium, JSOUP 에러", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "번역기 혹은 Jackson 에러", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> createContent(
        @RequestBody
        ContentRequestDto.CreateReq request
    ) {
        ContentCommand.CrawlingContent command = contentDtoMapper.doCrawlingContent(request);
        contentFacade.createContent(command);

        return ResponseEntityFactory.toResponseEntity(CONTENT_CREATE_SUCCESS);
    }

    /**
     * 컨텐츠 활성화, 비활성화
     */
    @PutMapping("/toggle-active/{contentId}")
    @Operation(summary = "어드민 - 컨텐츠 상태 수정", description = "어드민 회원이 컨텐츠 상태를 변경합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "컨텐츠 상태 변경 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
        ),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> modifyContentStatus(
        @PathVariable
        Long contentId
    ) {
        contentFacade.modifyContentStatus(contentId);

        return ResponseEntityFactory.toResponseEntity(CONTENT_MODIFY_SUCCESS);
    }

    /**
     * 어드민 컨텐츠 조회
     */
    @GetMapping("/view/reading")
    @Operation(summary = "어드민 페이지 리딩 컨텐츠 조회", description = "페이지네이션을 적용하여 어드민 페이지에서 리딩 컨텐츠 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "어드민 페이지 리딩 컨텐츠 조회 요청 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentAdminView.class))
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
            contentDtoMapper.doGetReadingView(page, size, direction, sort, categoryId, null);
        PaginationInfo<ContentInfo.Admin> info = contentFacade.getAdminReadingView(command);
        ContentResponseDto.AdminListRes response = contentDtoMapper.ofAdminListRes(info);

        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }

    @GetMapping("/view/listening")
    @Operation(summary = "어드민 페이지 리스닝 컨텐츠 조회", description = "페이지네이션을 적용하여 어드민 페이지에서 리스닝 컨텐츠 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "어드민 페이지 리스닝 컨텐츠 조회 요청 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentAdminView.class))
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
            contentDtoMapper.doGetListeningView(page, size, direction, sort, categoryId, null);
        PaginationInfo<ContentInfo.Admin> info = contentFacade.getAdminListening(command);
        ContentResponseDto.AdminListRes response = contentDtoMapper.ofAdminListRes(info);

        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }
}
