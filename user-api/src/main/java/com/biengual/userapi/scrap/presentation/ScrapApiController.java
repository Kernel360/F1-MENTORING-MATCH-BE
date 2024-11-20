package com.biengual.userapi.scrap.presentation;

import static com.biengual.core.response.success.ScrapSuccessCode.*;

import com.biengual.userapi.scrap.domain.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerBooleanReturn;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.scrap.application.ScrapFacade;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapInfo;
import com.biengual.userapi.scrap.presentation.swagger.SwaggerScrapView;

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
	private final ScrapFacade scrapFacade;
	private final ScrapService scrapService;
	private final ScrapDtoMapper scrapDtoMapper;

	@GetMapping("/view")
	@Operation(summary = "스크랩 조회", description = "전체 스크랩 목록을 조회합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "스크랩 조회 성공",
		content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerScrapView.class))
		}
	),
		@ApiResponse(responseCode = "404", description = "스크랩 조회 실패", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> getScraps(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal
	) {
		ScrapInfo.ViewInfo info = scrapService.getAllScraps(principal.getId());
		ScrapResponseDto.ViewListRes response = scrapDtoMapper.ofViewRes(info);

		return ResponseEntityFactory.toResponseEntity(SCRAP_VIEW_SUCCESS, response);
	}

	@Deprecated
	@GetMapping("/check")
	@Operation(summary = "스크랩 확인", description = "스크랩 했는지 확인합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "스크랩 성공",
		content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBooleanReturn.class))
		}
	),
		@ApiResponse(responseCode = "404", description = "스크랩 조회 실패", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> existsScrap(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal,
		@RequestParam
		Long contentId
	) {
		ScrapCommand.GetByContents command = scrapDtoMapper.doGetByContents(contentId, principal);
		boolean response = scrapFacade.existsScrap(command);

		return ResponseEntityFactory.toResponseEntity(SCRAP_CHECK_SUCCESS, response);
	}

	@PostMapping("/create/{contentId}")
	@Operation(summary = "스크랩 생성", description = "새로운 스크랩을 생성합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "스크랩 생성 성공",
		content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
		}
	),
		@ApiResponse(responseCode = "400", description = "이미 등록된 스크랩 요청", content = @Content),
		@ApiResponse(responseCode = "404", description = "스크랩 조회 실패", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> createScrap(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal,
		@PathVariable
		Long contentId
	) {
		ScrapCommand.Create command = scrapDtoMapper.doCreate(contentId, principal);
		scrapFacade.createScrap(command);

		return ResponseEntityFactory.toResponseEntity(SCRAP_CREATE_SUCCESS);
	}

	@DeleteMapping("/delete/{contentId}")
	@Operation(summary = "스크랩 삭제", description = "스크랩을 삭제합니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "스크랩 삭제 성공",
		content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
		}
	),
		@ApiResponse(responseCode = "404", description = "스크랩 조회 실패", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
	})
	public ResponseEntity<Object> deleteScrap(
		@AuthenticationPrincipal
		OAuth2UserPrincipal principal,
		@PathVariable
		Long contentId
	) {
		ScrapCommand.Delete command = scrapDtoMapper.doDelete(contentId, principal);
		scrapFacade.deleteScrap(command);

		return ResponseEntityFactory.toResponseEntity(SCRAP_DELETE_SUCCESS);
	}
}
