package com.biengual.userapi.crawling.domain;

import com.biengual.userapi.content.domain.ContentCommand;

public interface CrawlingStore {
    ContentCommand.Create getYoutubeDetail(ContentCommand.CrawlingContent command);

    ContentCommand.Create getCNNDetail(ContentCommand.CrawlingContent command);

    ContentCommand.Create crawlingScheduledContent(ContentCommand.CrawlingContent command);
}
