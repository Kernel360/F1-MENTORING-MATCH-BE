package com.biengual.userapi.content.application;

import com.biengual.core.annotation.Facade;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.crawling.domain.CrawlingService;
import com.biengual.userapi.user.domain.UserService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
    private final CrawlingService crawlingService;
    private final ContentService contentService;
    private final UserService userService;

    public void createContent(ContentCommand.CrawlingContent command) {
        ContentCommand.Create createContent = crawlingService.getCrawlingDetail(command);
        contentService.createContent(createContent);
    }

    public void modifyContentStatus(Long contentId) {
        contentService.modifyContentStatus(contentId);
    }

    // 스크랩 많은 순 컨텐츠 프리뷰 조회
    public ContentInfo.PreviewContents getContentsByScrapCount(ContentCommand.GetScrapPreview command) {
        return contentService.getContentsByScrapCount(command);
    }

    // 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
    public PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command) {
        return contentService.search(command);
    }

    // 리딩 컨텐츠 프리뷰 페이지 조회
    public PaginationInfo<ContentInfo.ViewContent> getReadingView(ContentCommand.GetReadingView command) {
        return contentService.getViewContents(command);
    }

    // 리스닝 컨텐츠 프리뷰 페이지 조회
    public PaginationInfo<ContentInfo.ViewContent> getListeningView(ContentCommand.GetListeningView command) {
        return contentService.getViewContents(command);
    }

    // 리딩 컨텐츠 프리뷰 조회
    public ContentInfo.PreviewContents getReadingPreview(ContentCommand.GetReadingPreview command) {
        return contentService.getPreviewContents(command);
    }

    // 리스닝 컨텐츠 프리뷰 조회
    public ContentInfo.PreviewContents getListeningPreview(ContentCommand.GetListeningPreview command) {
        return contentService.getPreviewContents(command);
    }

    // 어드민 페이지 리딩 컨텐츠 조회
    public PaginationInfo<ContentInfo.Admin> getAdminReadingView(ContentCommand.GetAdminReadingView command) {
        return contentService.getAdminView(command);
    }

    // 어드민 페이지 리스닝 컨텐츠 조회
    public PaginationInfo<ContentInfo.Admin> getAdminListening(ContentCommand.GetAdminListeningView command) {
        return contentService.getAdminView(command);
    }

    // 컨텐츠 상세 조회 및 최근 컨텐츠인 경우 포인트 소모
    public ContentInfo.Detail viewContentAndUpdatePointIfNeed(ContentCommand.GetDetail command) {
        return contentService.getScriptsOfContent(command);
    }

}