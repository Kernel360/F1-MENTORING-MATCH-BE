package com.biengual.userapi.crawling.domain;

import com.biengual.userapi.content.domain.ContentCommand;

public interface CrawlingStore {
	ContentCommand.Create getYoutubeDetail(ContentCommand.GetDetail command);

	ContentCommand.Create getCNNDetail(ContentCommand.GetDetail command);
}
