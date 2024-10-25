package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.dto.ContentRequestDto;
import com.biengual.userapi.content.domain.dto.ContentResponseDto;
import com.biengual.userapi.content.application.ContentService;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.swagger.content.SwaggerContentCreate;
import com.biengual.userapi.swagger.content.SwaggerContentUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.biengual.userapi.message.response.ContentResponseCode.*;

/**
 * admin, 회원 전용 컨텐츠 controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/contents")
@Tag(name = "Content - private API", description = "컨텐츠 회원전용 API")
public class ContentApiController {
	private final ContentService contentService;

	/**
	 * 컨텐츠 등록
	 */
	@PostMapping("/create")
	@Operation(summary = "어드민 - 컨텐츠 등록", description = "어드민 회원이 컨텐츠를 새로 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "컨텐츠가 성공적으로 생성되었습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentCreate.class))}
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> createContent(
		Authentication authentication,
		@RequestBody ContentRequestDto.ContentCreateRequestDto contentRequest
	) throws Exception {

		ContentResponseDto.ContentCreateResponseDto createdContent
			= contentService.createContent(authentication, contentRequest);
		return ResponseEntityFactory.toResponseEntity(CONTENT_CREATE_SUCCESS, createdContent);
	}

	/**
	 * 컨텐츠 수정
	 */
	@PutMapping("/modify/{id}")
	@Operation(summary = "어드민 - 컨텐츠 수정", description = "어드민 회원이 컨텐츠를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠가 성공적으로 수정되었습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentUpdate.class))}
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "404", description = "컨텐츠를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> modifyContent(
		@PathVariable Long id,
		@RequestBody ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	) {
		ContentResponseDto.ContentUpdateResponseDto updatedContent
			= contentService.updateContent(id, contentUpdateRequest);

		return ResponseEntityFactory.toResponseEntity(CONTENT_MODIFY_SUCCESS, updatedContent);
	}

	/**
	 * 컨텐츠 비활성화
	 */
	@PutMapping("/deactivate/{id}")
	@Operation(summary = "어드민 - 컨텐츠 비활성화", description = "어드민 회원이 컨텐츠를 비활성화합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "컨텐츠가 성공적으로 비활성화되었습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContentUpdate.class))}
		),
		@ApiResponse(responseCode = "404", description = "컨텐츠를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> deactivateContent(
		@PathVariable Long id) {
		ContentResponseDto.ContentUpdateResponseDto contentDeleteResponseDto = contentService.deactivateContent(id);

		return ResponseEntityFactory.toResponseEntity(CONTENT_DEACTIVATE_SUCCESS, contentDeleteResponseDto);
	}
}
