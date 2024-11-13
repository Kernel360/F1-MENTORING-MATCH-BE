package com.biengual.userapi.question.presentation;

import static com.biengual.core.response.success.QuestionSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.question.application.QuestionFacade;

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
    private final QuestionFacade questionFacade;

    @PostMapping("/create/{contentId}")
    @Operation(summary = "어드민 - 문제 생성", description = "어드민 회원이 컨텐츠에 대한 문제를 새로 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "문제 생성 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
            }
        ),
        @ApiResponse(responseCode = "400", description = "이미 퀴즈가 생성된 컨텐츠", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "406", description = "문제 생성 API 에러", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "409", description = "문제 생성 JSON 파싱 에러", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> createQuestion(
        @PathVariable
        Long contentId
    ) {
        questionFacade.createQuestion(contentId);

        return ResponseEntityFactory.toResponseEntity(QUESTION_CREATE_SUCCESS);
    }
}
