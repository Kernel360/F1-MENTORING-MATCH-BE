package com.biengual.userapi.dashboard.presentation;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardService;
import com.biengual.userapi.dashboard.presentation.dto.*;
import com.biengual.userapi.dashboard.presentation.swagger.*;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.biengual.core.constant.BadRequestMessageConstant.DATE_PATTERN_MISMATCH;
import static com.biengual.core.response.success.DashboardSuccessCode.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard - public API", description = "대시보드 공통 API")
public class DashboardPublicController {

    private final DashboardDtoMapper dashboardDtoMapper;
    private final DashboardService dashboardService;

    @GetMapping("/learning/recent/summary")
    @Operation(summary = "최근 학습 컨텐츠 1개 조회", description = "회원이 최근 학습한 컨텐츠 1개를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최근 학습 컨텐츠 1개 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerGetRecentLearningSummary.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getRecentLearningSummary(
        @AuthenticationPrincipal OAuth2UserPrincipal principal
    ) {
        DashboardInfo.RecentLearningSummary info = dashboardService.getRecentLearningSummary(principal.getId());
        GetRecentLearningSummaryDto.Response response = dashboardDtoMapper.ofRecentLearningSummaryRes(info);
        return ResponseEntityFactory.toResponseEntity(RECENT_LEARNING_SUMMARY_VIEW_SUCCESS, response);
    }

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
        DashboardInfo.RecentLearningList info = dashboardService.getRecentLearning(principal.getId());
        GetRecentLearningDto.Response response = dashboardDtoMapper.ofRecentLearningRes(info);
        return ResponseEntityFactory.toResponseEntity(RECENT_LEARNING_VIEW_SUCCESS, response);
    }

    @GetMapping("/learning/categories")
    @Operation(summary = "월간 카테고리별 학습 컨텐츠 비율 및 개수 조회", description = "월간 카테고리별 학습 컨텐츠 비율 및 개수를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "월간 카테고리별 학습 컨텐츠 비율 및 개수 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerGetCategoryLearning.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
        @Parameter(name = "date", description = "\"yyyy-mm\" 문자열 형태의 날짜 / default: 현재 날짜"),
    })
    public ResponseEntity<Object> getCategoryLearning(
        @AuthenticationPrincipal OAuth2UserPrincipal principal,

        @RequestParam(required = false)
        @Pattern(regexp = "^[1-9][0-9]{3}-(0?[1-9]|1[0-2])$", message = DATE_PATTERN_MISMATCH)
        String date
    ) {
        DashboardInfo.CategoryLearningList info = dashboardService.getCategoryLearning(principal.getId(), date);
        GetCategoryLearningDto.Response response = dashboardDtoMapper.ofCategoryLearningRes(info);
        return ResponseEntityFactory.toResponseEntity(CATEGORY_LEARNING_VIEW_SUCCESS, response);
    }

    @GetMapping("/points")
    @Operation(summary = "현재 포인트 조회", description = "회원의 현재 포인트를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "현재 포인트 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerGetCurrentPoint.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getCurrentPoint(
        @AuthenticationPrincipal OAuth2UserPrincipal principal
    ) {
        Long currentPoint = dashboardService.getCurrentPoint(principal.getId());
        GetCurrentPointDto.Response response = dashboardDtoMapper.ofCurrentPointRes(currentPoint);
        return ResponseEntityFactory.toResponseEntity(CURRENT_POINT_VIEW_SUCCESS, response);
    }

    @GetMapping("/missions/calendar")
    @Operation(summary = "월간 미션 달력 조회", description = "회원의 월간 미션 달력를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "월간 미션 달력 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerGetMissionCalendar.class))}
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
        @Parameter(name = "date", description = "\"yyyy-mm\" 문자열 형태의 날짜 / default: 현재 날짜"),
    })
    public ResponseEntity<Object> getMissionCalendar(
        @AuthenticationPrincipal OAuth2UserPrincipal principal,

        @RequestParam(required = false)
        @Pattern(regexp = "^[1-9][0-9]{3}-(0?[1-9]|1[0-2])$", message = DATE_PATTERN_MISMATCH)
        String date
    ) {
        DashboardInfo.MissionCalendar info = dashboardService.getMissionCalendar(principal.getId(), date);
        GetMissionCalendarDto.Response response = dashboardDtoMapper.ofMissionCalendarRes(info);
        return ResponseEntityFactory.toResponseEntity(MISSION_CALENDAR_VIEW_SUCCESS, response);
    }
}
