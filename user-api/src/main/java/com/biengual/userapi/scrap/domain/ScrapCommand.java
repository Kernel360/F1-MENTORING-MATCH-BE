package com.biengual.userapi.scrap.domain;

public class ScrapCommand {

	public record GetByContents(
		Long userId,
		Long contentId
	) {
	}

	public record Create(
		Long userId,
		Long contentId
	) {
	}

	public record Delete(
		Long userId,
		Long contentId
	) {
	}
}
