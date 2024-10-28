package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentType;

public class ContentRequestDto {

	public record CreateReq(
		ContentType contentType,
		String url
	) {
	}
}