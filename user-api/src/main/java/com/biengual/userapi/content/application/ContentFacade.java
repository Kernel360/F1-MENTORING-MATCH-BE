package com.biengual.userapi.content.application;

import com.biengual.core.annotation.Facade;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.crawling.domain.CrawlingService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
    private final CrawlingService crawlingService;
    private final ContentService contentService;

    public void createContent(ContentCommand.CrawlingContent command) {
        ContentCommand.Create createContent = crawlingService.getCrawlingDetail(command);
        contentService.createContent(createContent);
    }

    public void modifyContentStatus(Long contentId) {
        contentService.modifyContentStatus(contentId);
    }

    // 어드민 페이지 리딩 컨텐츠 조회
    public PaginationInfo<ContentInfo.Admin> getAdminReadingView(ContentCommand.GetAdminReadingView command) {
        return contentService.getAdminView(command);
    }

    // 어드민 페이지 리스닝 컨텐츠 조회
    public PaginationInfo<ContentInfo.Admin> getAdminListening(ContentCommand.GetAdminListeningView command) {
        return contentService.getAdminView(command);
    }
}