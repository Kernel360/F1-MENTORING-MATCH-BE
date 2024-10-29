package com.biengual.userapi.crawling.application;

import static com.biengual.userapi.core.message.error.code.ContentErrorCode.*;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.core.common.enums.ContentType;
import com.biengual.userapi.core.message.error.exception.CommonException;
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
    public ContentCommand.Create getCrawlingDetail(ContentCommand.CrawlingContent command) {

        if (StringUtils.equals(ContentType.LISTENING, command.contentType())) {
            return getYoutubeDetail(command);
        }
        if (StringUtils.equals(ContentType.READING, command.contentType())) {
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