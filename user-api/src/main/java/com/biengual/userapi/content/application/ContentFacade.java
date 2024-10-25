package com.biengual.userapi.content.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
    private final ContentService contentService;

    // 스크랩 많은 순 컨텐츠 조회
    public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return contentService.getContentsByScrapCount(size);
    }
}
