package com.biengual.userapi.crawling.application;

import org.springframework.stereotype.Service;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.crawling.domain.CrawlingService;
import com.biengual.userapi.crawling.domain.CrawlingStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {
	private final CrawlingStore crawlingStore;

	@Override
	public ContentCommand.Create getCrawlingDetail(ContentCommand.GetDetail command) {
		ContentCommand.Create contentCommand = null;
		if (command.contentType().equals(ContentType.LISTENING)) {
			contentCommand = getYoutubeDetail(command);
		}
		if (command.contentType().equals(ContentType.READING)) {
			contentCommand = getCNNDetail(command);
		}
		return contentCommand;
	}

	@Override
	public ContentCommand.Create getYoutubeDetail(ContentCommand.GetDetail command) {
		return crawlingStore.getYoutubeDetail(command);
	}

	@Override
	public ContentCommand.Create getCNNDetail(ContentCommand.GetDetail command) {
		return crawlingStore.getCNNDetail(command);
	}

}