package com.biengual.userapi.s3.presentation;

import static com.biengual.core.response.success.S3SuccessCode.*;

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
import com.biengual.userapi.s3.domain.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/s3-test")
@RequiredArgsConstructor
@Tag(name = "S3 - test API", description = "S3 테스트용 API")
public class S3TestController {
    private final S3Service s3Service;

    @PostMapping("/put")
    @Operation(summary = "S3 데이터 저장 테스트", description = "S3 데이터 저장 테스트")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "S3 데이터 저장 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVoidReturn.class))
            }
        ),
        @ApiResponse(responseCode = "404", description = "유저 조회 실패", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Object> saveImageToLocalStack(
        @RequestParam
        Long contentId
    ) {
        s3Service.saveToS3(contentId);
        return ResponseEntityFactory.toResponseEntity(S3_STORE_SUCCESS);
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
    public ResponseEntity<Object> getImageToLocalStack(
        @PathVariable
        Long contentId
    ) {
        String imageUrlFromS3 = s3Service.getImageFromS3(contentId);
        return ResponseEntityFactory.toResponseEntity(S3_READ_SUCCESS, imageUrlFromS3);
    }
}


