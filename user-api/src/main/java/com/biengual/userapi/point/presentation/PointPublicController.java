package com.biengual.userapi.point.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.point.domain.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.biengual.core.constant.BadRequestMessageConstant.NULL_CONTENT_ID_ERROR_MESSAGE;
import static com.biengual.core.response.success.PointSuccessCode.POINT_PAYMENT_FOR_RECENT_CONTENT_SUCCESS;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
@Tag(name = "Point - public API", description = "포인트 공통 API")
public class PointPublicController {

    private final PointService pointService;

    @PostMapping("/payment/recent-content")
    @Operation(summary = "최신 컨텐츠 포인트 차감", description = "최신 컨텐츠에 대한 포인트를 차감합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최신 컨텐츠 포인트 차감 요청 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
        }),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    public ResponseEntity<Object> payPointsForRecentContent(
        @AuthenticationPrincipal OAuth2UserPrincipal principal,
        @NotNull(message = NULL_CONTENT_ID_ERROR_MESSAGE) @RequestParam Long contentId
    ) {
        pointService.payPointsForRecentContent(contentId, principal.getId());

        return ResponseEntityFactory.toResponseEntity(POINT_PAYMENT_FOR_RECENT_CONTENT_SUCCESS);
    }
}
