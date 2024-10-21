package com.biengual.userapi.content.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "컨텐츠 페이지 응답")
public class ContentPageResponse {

	@Schema(description = "현재 페이지 번호 (0부터 시작)")
	private int pageNumber;

	@Schema(description = "페이지 크기")
	private int pageSize;

	@Schema(description = "전체 페이지 수")
	private int totalPages;

	@Schema(description = "전체 데이터 수")
	private long totalElements;

	@Schema(description = "컨텐츠 목록")
	private List<ContentResponseDto> content;

}