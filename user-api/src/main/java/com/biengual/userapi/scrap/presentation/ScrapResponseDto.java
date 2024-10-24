package com.biengual.userapi.scrap.presentation;

import java.time.LocalDateTime;
import java.util.List;

import com.biengual.userapi.content.domain.enums.ContentType;

import lombok.Builder;

public class ScrapResponseDto {

	public record View(
		Long scrapId,
		Long contentId,
		String title,
		ContentType contentType,
		LocalDateTime createdAt,
		String preScripts,
		String thumbnailUrl
	) {
	}

	@Builder
	public record ViewListRes(
		List<View> scrapList
	) {
	}

}
