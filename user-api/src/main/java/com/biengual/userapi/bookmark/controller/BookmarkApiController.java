package com.biengual.userapi.bookmark.controller;

import static com.biengual.userapi.message.response.BookmarkResponseCode.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.userapi.bookmark.domain.dto.BookmarkRequestDto;
import com.biengual.userapi.bookmark.domain.dto.BookmarkResponseDto;
import com.biengual.userapi.bookmark.service.BookmarkService;
import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.swagger.SwaggerVoidReturn;
import com.biengual.userapi.swagger.bookmark.SwaggerBookmarkCreate;
import com.biengual.userapi.swagger.bookmark.SwaggerBookmarkList;
import com.biengual.userapi.swagger.bookmark.SwaggerBookmarkMyList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
@RestController
@Tag(name = "Bookmark - private API", description = "북마크 회원전용 API")
public class BookmarkApiController {
	private final BookmarkService bookmarkService;

	@GetMapping("/view/{contentId}")
	@Operation(summary = "게시글 북마크 조회", description = "게시글에 대해 회원이 등록해 둔 북마크 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBookmarkList.class))}
		),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<BookmarkResponseDto.BookmarkListResponseDto>>>>
	getBookmarks(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal, @PathVariable Long contentId) {
		Map<String, List<BookmarkResponseDto.BookmarkListResponseDto>> data = new HashMap<>();
		data.put("bookmarkList", bookmarkService.getBookmarks(oAuth2UserPrincipal.getEmail(), contentId));

		return ResponseEntityFactory.toResponseEntity(BOOKMARK_VIEW_SUCCESS, data);
	}

	@GetMapping("/view")
	@Operation(summary = "북마크 전체 조회", description = "회원이 등록해 둔 모든 북마크 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBookmarkMyList.class))}
		),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Map<String, List<BookmarkResponseDto.BookmarkMyListResponseDto>>>>
	getAllBookmarks(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {
		Map<String, List<BookmarkResponseDto.BookmarkMyListResponseDto>> data = new HashMap<>();
		data.put("bookmarkMyList", bookmarkService.getAllBookmarks(oAuth2UserPrincipal.getId()));

		return ResponseEntityFactory.toResponseEntity(BOOKMARK_VIEW_SUCCESS, data);
	}

	@PostMapping("/create/{contentId}")
	@Operation(summary = "북마크 생성", description = "회원이 북마크를 생성합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBookmarkCreate.class))
			}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<BookmarkResponseDto.BookmarkCreateResponse>> createBookmark(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@PathVariable Long contentId,
		@RequestBody BookmarkRequestDto.BookmarkCreateRequest bookmarkRequestDto
	) {

		return ResponseEntityFactory.toResponseEntity(
			BOOKMARK_CREATE_SUCCESS,
			bookmarkService.createBookmark(oAuth2UserPrincipal.getEmail(), bookmarkRequestDto, contentId)
		);
	}

	@PutMapping("/update/{contentId}")
	@Operation(summary = "북마크 메모 수정", description = "회원이 북마크 메모를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerBookmarkList.class))
			}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<BookmarkResponseDto.BookmarkListResponseDto>> updateBookmark(
		@PathVariable Long contentId,
		@RequestBody BookmarkRequestDto.BookmarkUpdateRequest bookmarkRequestDto
	) {

		return ResponseEntityFactory.toResponseEntity(
			BOOKMARK_UPDATE_SUCCESS, bookmarkService.updateBookmark(bookmarkRequestDto, contentId)
		);
	}

	@DeleteMapping("/delete/{bookmarkId}")
	@Operation(summary = "북마크 삭제", description = "회원이 북마크를 삭제합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
			content = {
				@Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
			}),
		@ApiResponse(responseCode = "204", description = "컨텐츠가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content)
	})
	public ResponseEntity<ApiCustomResponse<Void>> deleteBookmark(
		@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal,
		@PathVariable Long bookmarkId
	) {
		bookmarkService.deleteBookmark(oAuth2UserPrincipal.getId(), bookmarkId);
		return ResponseEntityFactory.toResponseEntity(BOOKMARK_DELETE_SUCCESS);
	}

}
