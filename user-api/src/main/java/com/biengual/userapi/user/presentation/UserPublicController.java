package com.biengual.userapi.user.presentation;

import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.swagger.SwaggerBooleanReturn;
import com.biengual.userapi.swagger.SwaggerVoidReturn;
import com.biengual.userapi.swagger.user.SwaggerUserMyPage;
import com.biengual.userapi.swagger.user.SwaggerUserMyTime;
import com.biengual.userapi.swagger.user.SwaggerUserUpdate;
import com.biengual.userapi.user.application.UserFacade;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.biengual.userapi.message.response.UserResponseCode.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", methods = RequestMethod.GET)
@Tag(name = "User Public API", description = "가입된 유저 공통 API")
public class UserPublicController {

	private final UserDtoMapper userDtoMapper;
	private final UserFacade userFacade;
	private final UserService userService;

	@GetMapping("/me")
	@Operation(summary = "본인 정보 조회", description = "유저가 본인의 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "본인 정보 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUserMyPage.class))}
		),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> getMyInfo(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		UserInfo.MyInfo info = userFacade.getMyInfo(principal.getId());
		UserResponseDto.MyInfoRes response = userDtoMapper.ofMyInfoRes(info);

		return ResponseEntityFactory.toResponseEntity(USER_GET_INFO, response);
	}

	@PutMapping("/me")
	@Operation(summary = "본인 정보 수정", description = "유저가 본인의 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "본인 정보 수정 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUserUpdate.class))}
		),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> updateMyInfo(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal,
		@Valid @RequestBody
		UserRequestDto.UpdateMyInfoReq request
	) {
		UserCommand.UpdateMyInfo command = userDtoMapper.doUpdateMyInfo(request, principal);
		userFacade.updateMyInfo(command);

		return ResponseEntityFactory.toResponseEntity(USER_UPDATE_INFO);
	}

	@GetMapping("/time")
	@Operation(summary = "회원 가입 날짜 조회", description = "유저가 회원 가입 날짜를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUserMyTime.class))}
		),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> getMySignUpTime(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		return ResponseEntityFactory
			.toResponseEntity(USER_GET_INFO, userService.getMySignUpTime(principal.getId()));
	}

	@PostMapping("/logout")
	@Operation(summary = "회원 로그아웃", description = "유저가 로그아웃합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그아웃에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
		}),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> logout(
		HttpServletRequest request,
		HttpServletResponse response,
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		userService.logout(request, response, principal.getId());

		return ResponseEntityFactory.toResponseEntity(USER_LOGOUT_SUCCESS);
	}

	@GetMapping("/status")
	@Operation(summary = "회원 로그인 상태 조회", description = "회원 로그인 상태를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBooleanReturn.class))}
		),
		@ApiResponse(responseCode = "404", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> getUserStatus(
		HttpServletRequest request
	) {

		return ResponseEntityFactory
			.toResponseEntity(USER_STATUS_INFO, userService.getUserStatus(request));
	}
}
