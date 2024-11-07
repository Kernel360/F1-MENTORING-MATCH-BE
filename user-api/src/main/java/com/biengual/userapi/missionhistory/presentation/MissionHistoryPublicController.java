package com.biengual.userapi.missionhistory.presentation;

import static com.biengual.core.response.success.MissionHistorySuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.missionhistory.domain.MissionHistoryInfo;
import com.biengual.userapi.missionhistory.domain.MissionHistoryService;
import com.biengual.userapi.missionhistory.presentation.swagger.SwaggerMissionHistoryRecent;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 회원 전용 미션 기록 controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mission-history")
@Tag(name = "Mission History - public API", description = "미션 기록 공통 API")
public class MissionHistoryPublicController {
    private final MissionHistoryService missionHistoryService;
    private final MissionHistoryDtoMapper missionHistoryDtoMapper;

    @GetMapping("/recent")
    @Operation(summary = "최근 미션 기록 조회", description = "최근 7일 동안 미션 완료 갯수를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최근 미션 기록 조회 성공", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMissionHistoryRecent.class))
        }),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    })
    public ResponseEntity<Object> getRecentMissionHistory(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        MissionHistoryInfo.RecentHistories info = missionHistoryService.getRecentHistory(principal);
        RecentHistoryDto.Response response = missionHistoryDtoMapper.ofRecentHistory(info);

        return ResponseEntityFactory.toResponseEntity(MISSION_RECENT_HISTORY_VIEW_SUCCESS, response);
    }
}
