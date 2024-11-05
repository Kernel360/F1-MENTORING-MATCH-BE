package com.biengual.userapi.learning.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.learning.domain.LearningService;
import com.biengual.userapi.learning.presentation.dto.UpdateLearningRateDto;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.presentation.swagger.SwaggerUserMyPage;
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

    @PostMapping("/progress")
    @Operation(summary = "학습률 업데이트", description = "해당 컨텐츠에 대한 학습률을 업데이트합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "학습률 업데이트 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUserMyPage.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> updateLearningRate(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal,
        @RequestBody
        UpdateLearningRateDto.Request request
    ) {
        LearningCommand.UpdateLearningRate command = learningDtoMapper.doUpdateLearningRate(request, principal);
        learningService.updateLearningRate(command);

        return ResponseEntityFactory.toResponseEntity(LEARNING_UPDATE_LEARNING_RATE);
    }
}
