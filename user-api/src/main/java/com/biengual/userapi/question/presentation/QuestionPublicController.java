package com.biengual.userapi.question.presentation;

import static com.biengual.core.response.success.QuestionSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerBooleanReturn;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.question.application.QuestionFacade;
import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.presentation.swagger.SwaggerQuestionView;

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
    private final QuestionDtoMapper questionDtoMapper;
    private final QuestionFacade questionFacade;

    @GetMapping("/view/{contentId}")
    @Operation(summary = "문제 조회", description = "컨텐츠 Id를 이용해 문제를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "문제 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerQuestionView.class))
            }
        ),
        @ApiResponse(responseCode = "403", description = "비활성화 컨텐츠(U-C-905)", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패(U-C-901), 해당 컨텐츠의 문제 모두 정답(U-Q-905)", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getQuestion(
        @PathVariable
        Long contentId,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        QuestionCommand.GetQuestion command = questionDtoMapper.doGetQuestion(contentId, principal);
        QuestionInfo.DetailInfo info = questionFacade.getQuestions(command);
        QuestionResponseDto.ViewListRes response = questionDtoMapper.ofViewListRes(info);

        return ResponseEntityFactory.toResponseEntity(QUESTION_VIEW_SUCCESS, response);
    }

    @GetMapping("/view/correct/{contentId}")
    @Operation(summary = "정답 맞춘 문제 조회", description = "컨텐츠 Id를 이용해 문제를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "문제 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerQuestionView.class))
            }
        ),
        @ApiResponse(responseCode = "403", description = "비활성화 컨텐츠(U-C-905)", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패(U-C-901), 해당 컨텐츠의 문제 모두 정답(U-Q-905)", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getCorrectedQuestion(
        @PathVariable
        Long contentId,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        QuestionCommand.GetQuestion command = questionDtoMapper.doGetQuestion(contentId, principal);
        QuestionInfo.DetailInfo info = questionFacade.getCorrectQuestions(command);
        QuestionResponseDto.ViewListRes response = questionDtoMapper.ofViewListRes(info);

        return ResponseEntityFactory.toResponseEntity(QUESTION_VIEW_SUCCESS, response);
    }

    @PostMapping("/hint/view")
    @Operation(summary = "문제 힌트 조회", description = "문제 Id를 이용해 문제 힌트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "문제 힌트 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerQuestionView.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "문제 힌트 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getHintOfQuestion(
        @RequestBody
        GetHintDto.Request request,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        QuestionCommand.GetHint command = questionDtoMapper.doGetHint(request, principal);
        GetHintDto.Response response = questionDtoMapper.ofGetHintRes(questionFacade.getHint(command));

        return ResponseEntityFactory.toResponseEntity(QUESTION_VIEW_SUCCESS, response);
    }

    @PostMapping("/verify")
    @Operation(summary = "문제 정답 확인", description = "문제에 대한 정답을 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "문제 정답 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBooleanReturn.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "문제 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "405", description = "잘못된 사용자 요청", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> verifyAnswer(
        @RequestBody
        VerifyDto.Request request,
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        QuestionCommand.Verify command = questionDtoMapper.doVerify(request, principal);
        boolean response = questionFacade.verifyAnswer(command);

        return ResponseEntityFactory.toResponseEntity(QUESTION_ANSWER_VIEW_SUCCESS, response);
    }
}
