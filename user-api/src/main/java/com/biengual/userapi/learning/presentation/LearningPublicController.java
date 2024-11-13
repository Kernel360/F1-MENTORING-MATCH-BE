package com.biengual.userapi.learning.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.domain.LearningService;
import com.biengual.userapi.learning.presentation.dto.RecordLearningRateDto;
import com.biengual.userapi.learning.presentation.swagger.SwaggerRecordLearningRate;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.biengual.core.response.success.LearningSuccessCode.LEARNING_UPDATE_LEARNING_RATE;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
@Tag(name = "Learning Public API", description = "학습 공통 API")
public class LearningPublicController {
    private final LearningDtoMapper learningDtoMapper;
    private final LearningService learningService;

    // TODO: 유저가 정말 학습했는지에 대한 검증을 생각해볼 것
    @PostMapping("/progress")
    @Operation(summary = "학습률 기록", description = "해당 컨텐츠에 대한 학습률을 기록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "학습률 기록 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerRecordLearningRate.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "컨텐츠 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "비활성화 컨텐츠", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> recordLearningRate(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal,
        @RequestBody
        RecordLearningRateDto.Request request
    ) {
        LearningCommand.RecordLearningRate command = learningDtoMapper.doRecordLearningRate(request, principal);
        learningService.recordLearningRate(command);

        return ResponseEntityFactory.toResponseEntity(LEARNING_UPDATE_LEARNING_RATE);
    }
}
