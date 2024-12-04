package com.biengual.userapi.image.presentation;

import static com.biengual.core.response.success.ImageSuccessCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.swagger.SwaggerStringReturn;
import com.biengual.core.swagger.SwaggerVoidReturn;
import com.biengual.userapi.image.domain.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// TODO: PROD 연결 완료 및 테스트 후에는 LocalStack 용 API 이므로 Profile 설정할 것
@RestController
@RequestMapping("/api/s3-test")
@RequiredArgsConstructor
@Tag(name = "S3 - private test API", description = "S3 어드민 테스트 용 API - 사용 및 테스트 X")
public class ImageApiController {
    private final ImageService imageService;

    @PostMapping("/post")
    @Operation(summary = "S3 데이터 저장 테스트", description = "S3 데이터 저장 테스트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "S3 데이터 저장 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> saveImageToS3(
        @RequestParam
        Long contentId
    ) {
        imageService.save(contentId);
        return ResponseEntityFactory.toResponseEntity(IMAGE_STORE_SUCCESS);
    }

    @GetMapping("/get/{contentId}")
    @Operation(summary = "S3 데이터 조회 테스트", description = "S3 데이터 조회 테스트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "S3 데이터 조회 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerStringReturn.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> getImageToS3(
        @PathVariable
        Long contentId
    ) {
        String imageUrlFromS3 = imageService.getImage(contentId);
        return ResponseEntityFactory.toResponseEntity(IMAGE_READ_SUCCESS, imageUrlFromS3);
    }


    // TODO: PROD 까지 적용 되면 관련 메서드 모두 삭제 예정
    @PostMapping("/save-all-content")
    @Operation(summary = "기존 컨텐츠 이미지 S3 데이터 저장 - 일회용", description = "S3 데이터 저장 테스트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "S3 데이터 저장 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> saveAllImageToS3() {
        imageService.saveAllToS3();
        return ResponseEntityFactory.toResponseEntity(IMAGE_STORE_SUCCESS);
    }
}


