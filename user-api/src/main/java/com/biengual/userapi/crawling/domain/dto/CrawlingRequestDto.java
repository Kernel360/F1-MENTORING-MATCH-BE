package com.biengual.userapi.crawling.domain.dto;

public class CrawlingRequestDto {

	public record YoutubeRequestDto(
		String youtubeUrl
	) {

	}

	public record CNNRequestDto(
		String cnnUrl
	) {

	}
}
