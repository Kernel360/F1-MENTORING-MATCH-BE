package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentStatus;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.script.domain.entity.Script;

import java.util.List;

public class ContentRequestDto {

	public record CreateReq(
		ContentType contentType,
		String url
	) {
	}

	public record UpdateReq(
		String url,
		String title,
		List<Script> script,
		ContentStatus contentStatus
	) {

	}

	public record SearchReq(
		String searchWords
	) {
	}
}