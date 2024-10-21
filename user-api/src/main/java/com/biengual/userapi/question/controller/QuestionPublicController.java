package com.biengual.userapi.question.controller;

import static com.biengual.userapi.message.response.QuestionResponseCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.question.domain.dto.QuestionResponseDto;
import com.biengual.userapi.question.service.QuestionServiceImpl;
import com.biengual.userapi.swagger.content.SwaggerContentUpdate;
import com.biengual.userapi.swagger.question.SwaggerQuestionView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/questions")
@Tag(name = "Question - public API", description = "문제 공통 API")
public class QuestionPublicController {
	private final QuestionServiceImpl questionService;

	@GetMapping("/view/{contentId}")
	@Operation(summary = "문제 조회", description = "컨텐츠 Id를 이용해 문제를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "컨텐츠가 성공적으로 생성되었습니다.",
			content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerQuestionView.class))}
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<QuestionResponseDto.QuestionViewResponseDto>>>> getQuestion(
		@PathVariable Long contentId
	) {
		Map<String, List<QuestionResponseDto.QuestionViewResponseDto>> data = new HashMap<>();
		data.put("question-answer", questionService.getQuestions(contentId));

		return ResponseEntityFactory.toResponseEntity(QUESTION_VIEW_SUCCESS, data);
	}
}
