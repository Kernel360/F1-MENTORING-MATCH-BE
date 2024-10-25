package com.biengual.userapi.crawling.application;

import static com.biengual.userapi.message.error.code.ContentErrorCode.*;

import org.springframework.stereotype.Service;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.crawling.domain.CrawlingService;
import com.biengual.userapi.crawling.domain.CrawlingStore;
import com.biengual.userapi.message.error.exception.CommonException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
	private final CrawlingStore crawlingStore;

	@Override
	public ContentCommand.Create getCrawlingDetail(ContentCommand.CrawlingContent command) {
		if (command.contentType().equals(ContentType.LISTENING)) {
			return getYoutubeDetail(command);
		}
		if (command.contentType().equals(ContentType.READING)) {
			return getCNNDetail(command);
		}
		throw new CommonException(CONTENT_TYPE_NOT_FOUND);
	}

	@Override
	public ContentCommand.Create getYoutubeDetail(ContentCommand.CrawlingContent command) {
		return crawlingStore.getYoutubeDetail(command);
	}

	@Override
	public ContentCommand.Create getCNNDetail(ContentCommand.CrawlingContent command) {
		return crawlingStore.getCNNDetail(command);
	}

}