package com.biengual.userapi.content.application;

import com.biengual.userapi.content.domain.*;
import com.biengual.userapi.content.presentation.ContentDtoMapper;
import com.biengual.userapi.script.domain.entity.Script;
import com.biengual.userapi.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
	private final ContentDtoMapper contentDtoMapper;
    private final ContentReader contentReader;
    private final ContentStore contentStore;
	private final ContentDocumentReader contentDocumentReader;

	// 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command) {
		return contentReader.findPreviewPageBySearch(command);
	}

	// 리딩 컨텐츠 프리뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetReadingView command) {
		return contentReader.findReadingViewPage(command);
	}

	// 리스닝 컨텐츠 프리뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetListeningView command) {
		return contentReader.findListeningViewPage(command);
	}

	@Override
	@Transactional
	public void createContent(ContentCommand.Create command) {
		contentStore.createContent(command);
	}

	@Override
	@Transactional
	public void updateContent(ContentCommand.Modify command) {
		contentStore.updateContent(command);
	}

	// 리딩 콘텐츠 프리뷰 조회
	@Override
	public ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetReadingPreview command) {
		return ContentInfo.PreviewContents.of(contentReader.findReadingPreview(command));
	}

	// 리스닝 콘텐츠 프리뷰 조회
	@Override
	public ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetListeningPreview command) {
		return ContentInfo.PreviewContents.of(contentReader.findListeningPreview(command));
	}

	// 스크랩 많은 순 컨텐츠 프리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return ContentInfo.PreviewContents.of(contentReader.findContentsByScrapCount(size));
    }

	@Override
	@Transactional
	public void deactivateContent(Long contentId) {
		contentStore.deactivateContent(contentId);
	}

	// TODO: 멘토님에게 DataProvider의 영역에 대한 답변을 들어볼 것
	// 컨텐츠 디테일 조회
	@Override
	@Transactional    // hit 증가 로직 있어서 readOnly 생략
	public ContentInfo.Detail getScriptsOfContent(Long contentId) {
		ContentEntity content = contentReader.findContent(contentId);

		List<Script> scripts = contentDocumentReader.findScripts(content.getMongoContentId());

		// TODO: 추후 레디스로 바꿀 예정
		content.updateHits();

		return contentDtoMapper.buildDetail(content, scripts);
	}
}
