package com.biengual.userapi.content.application;

import com.biengual.core.annotation.RedisDistributedLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.enums.PointReason;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.domain.ContentService;
import com.biengual.userapi.content.domain.ContentStore;
import com.biengual.userapi.payment.domain.PaymentStore;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.validator.PointValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentReader contentReader;
    private final ContentStore contentStore;
    private final PointValidator pointValidator;
    private final PointManager pointManager;
    private final PaymentStore paymentStore;

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

    // 컨텐츠 생성 - 크롤링
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
    public ContentInfo.PreviewContents getContentsByScrapCount(ContentCommand.GetScrapPreview command) {
        return ContentInfo.PreviewContents.of(contentReader.findContentsByScrapCount(command));
    }

    // 어드민 페이지 리딩 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    @Transactional(readOnly = true)
    public PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetAdminReadingView command) {
        return contentReader.findReadingAdmin(command);
    }

    // 어드민 페이지 리스닝 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    @Transactional(readOnly = true)
    public PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetAdminListeningView command) {
        return contentReader.findListeningAdmin(command);
    }

    // 컨텐츠 레벨 피드백
    @Override
    @RedisDistributedLock(key = "#command.userId() + \":\" + #command.contentId()")
    public void submitLevelFeedback(ContentCommand.SubmitLevelFeedback command) {
        contentStore.recordContentLevelFeedbackHistory(command);
    }

    // 컨텐츠 상태 변경 ACTIVATED <-> DEACTIVATED
    @Override
    @Transactional
    public void modifyContentStatus(Long contentId) {
        contentStore.modifyContentStatus(contentId);
    }

    // 컨텐츠 디테일 조회 및 최근 컨텐츠인 경우 포인트 소모
    @Override
    @Transactional    // hit 증가 로직 있어서 readOnly 생략
    public ContentInfo.Detail getScriptsOfContent(ContentCommand.GetDetail command) {
        ContentInfo.Detail info = contentReader.findActiveContentWithScripts(command);

        if (!pointValidator.verifyContentView(command)) {
            pointManager.updateAndSavePoint(PointReason.VIEW_RECENT_CONTENT, command.userId());
            paymentStore.updatePaymentHistory(command.userId(), command.contentId());
        }
        // TODO: 추후 레디스로 바꿀 예정
        contentStore.increaseHits(command.contentId());

        return info;
    }
}
