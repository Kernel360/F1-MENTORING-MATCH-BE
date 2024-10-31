package com.biengual.userapi.mission.presentation;

import static com.biengual.core.response.success.MissionSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionInfo;
import com.biengual.userapi.mission.domain.MissionService;
import com.biengual.userapi.mission.presentation.swagger.SwaggerMissionStatus;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 회원 전용 미션 controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission - public API", description = "미션 공통 API")
public class MissionPublicController {
    private final MissionService missionService;
    private final MissionDtoMapper missionDtoMapper;

    @GetMapping("/status")
    @Operation(summary = "유저 - 미션 진행 상황 조회", description = "회원이 미션을 진행 상황을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "미션 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMissionStatus.class))}
        ),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getMissionStatus(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        MissionInfo.StatusInfo info = missionService.getMissionStatus(principal.getId());
        StatusDto.Response response = missionDtoMapper.ofStatus(info);

        return ResponseEntityFactory.toResponseEntity(MISSION_STATUS_CHECK_SUCCESS, response);
    }

    @PutMapping("/status")
    @Operation(summary = "유저 - 미션 완료 요청", description = "회원이 미션 완료 요청을 보냅니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "미션 완료 요청 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
        ),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> updateMissionComplete(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal,
        @RequestBody
        UpdateDto.Request request
    ) {
        MissionCommand.Update command = missionDtoMapper.doUpdate(principal, request);

        missionService.updateMissionComplete(command);

        return ResponseEntityFactory.toResponseEntity(MISSION_STATUS_UPDATE_SUCCESS);
    }
}
