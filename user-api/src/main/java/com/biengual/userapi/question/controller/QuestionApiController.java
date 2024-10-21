package com.biengual.userapi.question.controller;

import static com.biengual.userapi.message.response.QuestionResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.question.domain.dto.QuestionRequestDto;
import com.biengual.userapi.question.domain.dto.QuestionResponseDto;
import com.biengual.userapi.question.service.QuestionServiceImpl;
import com.biengual.userapi.swagger.content.SwaggerContentUpdate;
import com.biengual.userapi.swagger.question.SwaggerQuestionCreate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/questions")
@Tag(name = "Question - private API", description = "문제 생성 회원전용 API")
public class QuestionApiController {
	private final QuestionServiceImpl questionService;

	@PostMapping("/create/{contentId}")
	@Operation(summary = "어드민 - 문제 생성", description = "어드민 회원이 컨텐츠에 대한 문제를 새로 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "컨텐츠가 성공적으로 생성되었습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerQuestionCreate.class))}
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ApiCustomResponse<QuestionResponseDto.QuestionCreateResponseDto>> createQuestion(
		@PathVariable Long contentId,
		@RequestBody QuestionRequestDto.QuestionCreateRequestDto questionCreateRequestDto
	) {
		QuestionResponseDto.QuestionCreateResponseDto createResponseDto
			= questionService.createQuestion(contentId, questionCreateRequestDto);
		return ResponseEntityFactory.toResponseEntity(QUESTION_CREATE_SUCCESS, createResponseDto);
	}

}
