package com.biengual.userapi.content.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentDocumentReader;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.content.domain.ContentStore;
import com.biengual.userapi.content.presentation.ContentDtoMapper;
import com.biengual.userapi.core.util.PaginationInfo;
import com.biengual.userapi.core.domain.entity.content.entity.ContentEntity;
import com.biengual.userapi.core.domain.entity.content.document.script.Script;

import lombok.RequiredArgsConstructor;

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

    // 리딩 콘텐츠 프리뷰 조회
    @Override
    @Transactional(readOnly = true)
    public ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetReadingPreview command) {
        return ContentInfo.PreviewContents.of(contentReader.findReadingPreview(command));
    }

    // 리스닝 콘텐츠 프리뷰 조회
    @Override
    @Transactional(readOnly = true)
    public ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetListeningPreview command) {
        return ContentInfo.PreviewContents.of(contentReader.findListeningPreview(command));
    }

    // 스크랩 많은 순 컨텐츠 프리뷰 조회
    @Override
    @Transactional(readOnly = true)
    public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return ContentInfo.PreviewContents.of(contentReader.findContentsByScrapCount(size));
    }

    // 어드민 페이지 리딩 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    public PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetReadingView command) {
        return contentReader.findReadingAdmin(command);
    }

    // 어드민 페이지 리스닝 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    public PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetListeningView command) {
        return contentReader.findListeningAdmin(command);
    }

    @Override
    @Transactional
    public void modifyContentStatus(Long contentId) {
        contentStore.modifyContentStatus(contentId);
    }

    // TODO: 멘토님에게 DataProvider의 영역에 대한 답변을 들어볼 것
    // 컨텐츠 디테일 조회
    @Override
    @Transactional    // hit 증가 로직 있어서 readOnly 생략
    public ContentInfo.Detail getScriptsOfContent(Long contentId) {
        ContentEntity content = contentReader.findActiveContent(contentId);

        List<Script> scripts = contentDocumentReader.findScripts(content.getMongoContentId());

        // TODO: 추후 레디스로 바꿀 예정
        content.updateHits();

        return contentDtoMapper.buildDetail(content, scripts);
    }
}
