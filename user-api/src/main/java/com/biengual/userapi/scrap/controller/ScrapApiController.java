package com.biengual.userapi.scrap.controller;

import static com.biengual.userapi.message.response.ScrapResponseCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.scrap.domain.dto.ScrapRequestDto;
import com.biengual.userapi.scrap.domain.dto.ScrapResponseDto;
import com.biengual.userapi.scrap.service.ScrapService;
import com.biengual.userapi.swagger.SwaggerBooleanReturn;
import com.biengual.userapi.swagger.SwaggerVoidReturn;
import com.biengual.userapi.swagger.scrap.SwaggerScrapCreate;
import com.biengual.userapi.swagger.scrap.SwaggerScrapView;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/scrap")
@Tag(name = "Scrap - public API", description = "스크랩 회원전용 API")
public class ScrapApiController {
	private final ScrapService scrapService;

	@GetMapping("/view")
	@Operation(summary = "스크랩 조회", description = "전체 스크랩 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerScrapView.class))}
		),
		@ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<Object> getScraps(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
	) {
		Map<String, List<ScrapResponseDto.ScrapViewResponseDto>> data = new HashMap<>();
		data.put("scrapList", scrapService.getAllScraps(oAuth2UserPrincipal.getId()));

		return ResponseEntityFactory.toResponseEntity(SCRAP_VIEW_SUCCESS, data);
	}

	@GetMapping("/check")
	@Operation(summary = "스크랩 확인", description = "스크랩 했는지 확인합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBooleanReturn.class))}
		), @ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<Object> existsScrap(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@RequestParam Long contentId
	) {
		return ResponseEntityFactory.toResponseEntity(
			SCRAP_CHECK_SUCCESS, scrapService.existsScrap(oAuth2UserPrincipal.getId(), contentId)
		);
	}

	@PostMapping("/create")
	@Operation(summary = "스크랩 생성", description = "새로운 스크랩을 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerScrapCreate.class))}
		), @ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<Object> createScrap(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@RequestBody ScrapRequestDto.ScrapCreateRequestDto requestDto
	) {
		return ResponseEntityFactory.toResponseEntity(
			SCRAP_CREATE_SUCCESS, scrapService.createScrap(oAuth2UserPrincipal.getId(), requestDto)
		);
	}

	@DeleteMapping("/delete")
	@Operation(summary = "스크랩 삭제", description = "스크랩을 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
		), @ApiResponse(responseCode = "204", description = "요청한 스크랩이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<Object> deleteScrap(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@RequestBody ScrapRequestDto.ScrapDeleteRequestDto requestDto
	) {
		scrapService.deleteScrap(oAuth2UserPrincipal.getId(), requestDto);
		return ResponseEntityFactory.toResponseEntity(SCRAP_DELETE_SUCCESS);
	}

}
