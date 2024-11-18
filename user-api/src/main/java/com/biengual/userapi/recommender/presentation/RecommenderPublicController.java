package com.biengual.userapi.recommender.presentation;

import static com.biengual.core.response.success.RecommenderSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.recommender.domain.RecommenderInfo;
import com.biengual.userapi.recommender.domain.RecommenderService;
import com.biengual.userapi.recommender.presentation.swagger.SwaggerCategoryRecommender;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommender")
@Tag(name = "Recommender - private API", description = "추천 시스템 회원 전용 API")
public class RecommenderPublicController {
    private final RecommenderDtoMapper recommenderDtoMapper;
    private final RecommenderService recommenderService;

    @GetMapping("/category")
    @Operation(summary = "카테고리 기반 추천 컨텐츠 조회", description = "카테고리 학습 기록을 이용해 추천된 컨텐츠를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카테고리 기반 추천 컨텐츠 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerCategoryRecommender.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getRecommendedContentsByCategory(
        @AuthenticationPrincipal
        OAuth2UserPrincipal principal
    ) {
        RecommenderInfo.PreviewRecommender info = recommenderService.getRecommendedContentsByCategory(principal.getId());
        RecommenderDto.Response response = recommenderDtoMapper.ofPreviewRes(info);
        return ResponseEntityFactory.toResponseEntity(RECOMMENDER_CATEGORY_VIEW_SUCCESS, response);
    }
}
