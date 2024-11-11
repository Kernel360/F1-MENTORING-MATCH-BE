package com.biengual.userapi.dashboard.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardService;
import com.biengual.userapi.dashboard.presentation.dto.GetRecentLearningDto;
import com.biengual.userapi.dashboard.presentation.swagger.SwaggerGetRecentLearning;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.biengual.core.response.success.ContentSuccessCode.CONTENT_VIEW_SUCCESS;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard - public API", description = "대시보드 공통 API")
public class DashboardPublicController {

    private final DashboardDtoMapper dashboardDtoMapper;
    private final DashboardService dashboardService;

    // TODO: 프런트에서 목록 사이즈를 조절할 수 있게 할 것인지?
    @GetMapping("/learning/recent")
    @Operation(summary = "최근 학습 컨텐츠 조회", description = "회원이 최근 학습한 컨텐츠 프리뷰 목록 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최근 학습 컨텐츠 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerGetRecentLearning.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getRecentLearning(
        @AuthenticationPrincipal OAuth2UserPrincipal principal
        ) {
        DashboardInfo.RecentLearnings info = dashboardService.getRecentLearning(principal.getId());
        GetRecentLearningDto.Response response = dashboardDtoMapper.ofRecentLearningRes(info);
        return ResponseEntityFactory.toResponseEntity(CONTENT_VIEW_SUCCESS, response);
    }
}
