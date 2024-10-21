package com.biengual.userapi.scrap.domain.dto;

import java.time.LocalDateTime;

import com.biengual.userapi.content.domain.entity.ContentEntity;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.scrap.domain.entity.ScrapEntity;

public class ScrapResponseDto {

	public record ScrapViewResponseDto(
		Long scrapId,
		Long contentId,
		String title,
		ContentType contentType,
		LocalDateTime createdAt,
		String preScripts,
		String thumbnailUrl
	) {
		public static ScrapViewResponseDto from(ScrapEntity scrap) {
			return new ScrapViewResponseDto(
				scrap.getId(),
				scrap.getContent().getId(),
				scrap.getContent().getTitle(),
				scrap.getContent().getContentType(),
				scrap.getCreatedAt(),
				scrap.getContent().getPreScripts(),
				scrap.getContent().getThumbnailUrl()
			);
		}
	}

	public record ScrapCreateResponseDto(
		Long contentId
	) {
		public static ScrapCreateResponseDto from(ContentEntity content) {
			return new ScrapCreateResponseDto(
				content.getId()
			);
		}
	}

}
