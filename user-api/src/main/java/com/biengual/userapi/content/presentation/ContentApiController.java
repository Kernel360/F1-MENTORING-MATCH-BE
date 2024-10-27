package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.application.ContentFacade;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.swagger.SwaggerVoidReturn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.biengual.userapi.message.response.ContentResponseCode.*;

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

	// TODO: request 매핑이 안되는 버그 있음
	/**
	 * 컨텐츠 수정
	 */
	@PutMapping("/modify/{contentId}")
	@Operation(summary = "어드민 - 컨텐츠 수정", description = "어드민 회원이 컨텐츠를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠 수정 성공",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
		),
		@ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> modifyContent(
		@PathVariable
		Long contentId,
		@RequestBody
		ContentRequestDto.UpdateReq request
	) {
		ContentCommand.Modify command = contentDtoMapper.doModify(contentId, request);
		contentFacade.modifyContent(command);

		return ResponseEntityFactory.toResponseEntity(CONTENT_MODIFY_SUCCESS);
	}

	/**
	 * 컨텐츠 비활성화
	 */
	@PutMapping("/deactivate/{contentId}")
	@Operation(summary = "어드민 - 컨텐츠 비활성화", description = "어드민 회원이 컨텐츠를 비활성화합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠 비활성화 성공",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
		),
		@ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> deactivateContent(
		@PathVariable
		Long contentId
	) {
		contentFacade.deactivateContent(contentId);

		return ResponseEntityFactory.toResponseEntity(CONTENT_DEACTIVATE_SUCCESS);
	}
}
