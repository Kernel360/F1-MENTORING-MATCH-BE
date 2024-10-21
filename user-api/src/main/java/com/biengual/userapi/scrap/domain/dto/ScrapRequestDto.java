package com.biengual.userapi.scrap.domain.dto;

import com.biengual.userapi.content.domain.entity.ContentEntity;
import com.biengual.userapi.scrap.domain.entity.ScrapEntity;

public class ScrapRequestDto {

	public record ScrapCreateRequestDto(
		Long contentId
	) {
		public ScrapEntity toEntity(ContentEntity content) {
			return ScrapEntity.builder()
				.content(content)
				.build();
		}
	}

	public record ScrapDeleteRequestDto(
		Long contentId
	) {

	}
}
