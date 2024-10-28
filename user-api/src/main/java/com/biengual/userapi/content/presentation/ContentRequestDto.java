package com.biengual.userapi.content.presentation;

import java.util.List;

import com.biengual.userapi.content.domain.ContentStatus;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.script.domain.entity.Script;

public class ContentRequestDto {

	public record CreateReq(
		ContentType contentType,
		String url
	) {
	}

	public record SearchReq(
		String searchWords
	) {
	}
}