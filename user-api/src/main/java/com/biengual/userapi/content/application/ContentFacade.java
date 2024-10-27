package com.biengual.userapi.content.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.crawling.domain.CrawlingService;
import com.biengual.userapi.util.PaginationInfo;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ContentFacade {
	private final CrawlingService crawlingService;
	private final ContentService contentService;

	public void createContent(ContentCommand.CrawlingContent crawlingContentCommand) {
		ContentCommand.Create createContent = crawlingService.getCrawlingDetail(crawlingContentCommand);
		contentService.createContent(createContent);
	}

	public void modifyContent(ContentCommand.Modify command) {
		contentService.updateContent(command);
	}

	public void deactivateContent(Long id) {
		contentService.deactivateContent(id);
	}

    // 스크랩 많은 순 컨텐츠 프리뷰 조회
    public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return contentService.getContentsByScrapCount(size);
    }

	// 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
	public PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command) {
		return contentService.search(command);
	}

	// 리딩 컨텐츠 뷰 페이지 조회
	public PaginationInfo<ContentInfo.ViewContent> getReadingView(ContentCommand.GetReadingView command) {
		return contentService.getViewContents(command);
	}

	// 리스닝 컨텐츠 뷰 페이지 조회
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

	// 컨텐츠 디테일 조회
	public ContentInfo.Detail getDetailContent(Long contentId) {
		return contentService.getScriptsOfContent(contentId);
	}
}
