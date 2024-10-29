package com.biengual.userapi.user.presentation;

import static com.biengual.userapi.core.response.success.UserSuccessCode.*;

import com.biengual.userapi.core.response.ResponseEntityFactory;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.core.swagger.SwaggerBooleanReturn;
import com.biengual.userapi.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.user.presentation.swagger.SwaggerUserMyPage;
import com.biengual.userapi.user.presentation.swagger.SwaggerUserMyTime;
import com.biengual.userapi.user.application.UserFacade;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
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

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*", methods = RequestMethod.GET)
@Tag(name = "User Public API", description = "가입된 유저 공통 API")
public class UserPublicController {

	private final UserDtoMapper userDtoMapper;
	private final UserFacade userFacade;

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
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))}
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


	// TODO: 나중에 누적 학습일자 정책이 바뀌어 필요없어지면 삭제할 것
	@GetMapping("/time")
	@Operation(summary = "회원 가입 날짜 조회", description = "유저가 회원 가입 날짜를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원 가입 날짜 조회 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUserMyTime.class))}
		),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> getMySignUpTime(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		UserInfo.MySignUpTime info = userFacade.getMySignUpTime(principal.getId());
		UserResponseDto.MySignUpTimeRes response = userDtoMapper.ofMySignUpTimeRes(info);

		return ResponseEntityFactory.toResponseEntity(USER_GET_INFO, response);
	}

	@PostMapping("/logout")
	@Operation(summary = "회원 로그아웃", description = "유저가 로그아웃합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원 로그아웃 성공", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
		}),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> logout(
		HttpServletRequest request,
		HttpServletResponse response,
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		userFacade.logout(request, response, principal.getId());

		return ResponseEntityFactory.toResponseEntity(USER_LOGOUT_SUCCESS);
	}

	// TODO: 컨트롤러에서 바로 반환할 수 있는데 UserService 까지 들어가는 것이 맞는가에 대해 생각해볼 것
	@GetMapping("/status")
	@Operation(summary = "회원 로그인 상태 조회", description = "회원 로그인 상태를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBooleanReturn.class))}
		),
		@ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<Object> getLoginStatus(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		boolean response = userFacade.getLoginStatus(principal);

		return ResponseEntityFactory.toResponseEntity(USER_STATUS_INFO, response);
	}
}
