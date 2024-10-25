package com.biengual.userapi.content.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.content.domain.ContentInfo;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
    private final ContentService contentService;

    public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return contentService.getContentsByScrapCount(size);
    }
}
