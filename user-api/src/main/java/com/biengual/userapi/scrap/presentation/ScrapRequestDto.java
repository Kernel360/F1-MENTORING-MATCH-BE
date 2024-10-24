package com.biengual.userapi.scrap.presentation;

public class ScrapRequestDto {

	public record CreateReq(
		Long contentId
	) {
	}

	public record DeleteReq(
		Long contentId
	) {
	}
}
