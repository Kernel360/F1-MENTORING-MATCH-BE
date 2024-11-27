package com.biengual.userapi.content.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentLevel;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.content.domain.*;
import com.biengual.userapi.content.presentation.ContentDtoMapper;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import com.biengual.userapi.scrap.domain.ScrapCustomRepository;
import com.biengual.userapi.validator.ContentValidator;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

import static com.biengual.core.constant.RestrictionConstant.PERIOD_FOR_POINT_CONTENT_ACCESS;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_NOT_FOUND;
import static com.biengual.core.response.error.code.ContentErrorCode.UNPAID_RECENT_CONTENT;

@DataProvider
@RequiredArgsConstructor
public class ContentReaderImpl implements ContentReader {
    private final ContentDtoMapper contentDtoMapper;
    private final ContentRepository contentRepository;
    private final ContentCustomRepository contentCustomRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ScrapCustomRepository scrapCustomRepository;
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;
    private final ContentValidator contentValidator;
    private final ContentLevelFeedbackHistoryCustomRepository contentLevelFeedbackHistoryCustomRepository;

    // 스크랩 많은 순 컨텐츠 프리뷰 조회
    @Override
    public List<ContentInfo.PreviewContent> findContentsByScrapCount(ContentCommand.GetScrapPreview command) {
        return contentCustomRepository.findContentsByScrapCount(command.size(), command.userId());
    }

    // 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.PreviewContent> findPreviewPageBySearch(ContentCommand.Search command) {
        Page<ContentInfo.PreviewContent> page =
            contentCustomRepository.findPreviewPageBySearch(command.pageable(), command.keyword(), command.userId());

        return PaginationInfo.from(page);
    }

    // 리딩 컨텐츠 프리뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.ViewContent> findReadingViewPage(ContentCommand.GetReadingView command) {
        Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
            command.pageable(), command.contentType(), command.categoryId(), command.userId()
        );

        return PaginationInfo.from(page);
    }

    // 리스닝 컨텐츠 프리뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.ViewContent> findListeningViewPage(ContentCommand.GetListeningView command) {
        Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
            command.pageable(), command.contentType(), command.categoryId(), command.userId()
        );

        return PaginationInfo.from(page);
    }

    // 리딩 컨텐츠 프리뷰 조회
    @Override
    public List<ContentInfo.PreviewContent> findReadingPreview(ContentCommand.GetReadingPreview command) {
        return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
            command.size(), command.sort(), command.contentType(), command.userId()
        );
    }

    // 리스닝 컨텐츠 프리뷰 조회
    @Override
    public List<ContentInfo.PreviewContent> findListeningPreview(ContentCommand.GetListeningPreview command) {
        return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
            command.size(), command.sort(), command.contentType(), command.userId()
        );
    }

    // 어드민 페이지 리딩 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    public PaginationInfo<ContentInfo.Admin> findReadingAdmin(ContentCommand.GetAdminReadingView command) {
        Page<ContentInfo.Admin> page = contentCustomRepository.findContentDetailForAdmin(
            command.pageable(), command.contentType(), command.categoryId()
        );
        return PaginationInfo.from(page);
    }

    // 어드민 페이지 리스닝 컨텐츠 조회 - DEACTIVATED 포함
    @Override
    public PaginationInfo<ContentInfo.Admin> findListeningAdmin(ContentCommand.GetAdminListeningView command) {
        Page<ContentInfo.Admin> page = contentCustomRepository.findContentDetailForAdmin(
            command.pageable(), command.contentType(), command.categoryId()
        );
        return PaginationInfo.from(page);

    }

    // TODO: 로직 개선해볼 것
    // 로그인 여부에 따른 컨텐츠 디테일 조회
    @Override
    public ContentInfo.Detail findActiveContentWithScripts(ContentCommand.GetDetail command) {
        ContentEntity content = this.findContent(command.contentId());

        if (!contentValidator.verifyLearnableContent(content, command.userId())) {
            throw new CommonException(UNPAID_RECENT_CONTENT);
        }

        ContentDocument contentDocument =
            contentDocumentRepository.findContentDocumentById(new ObjectId(content.getMongoContentId()))
                .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        List<Script> scripts = contentDocument.getScripts();

        if (command.userId() != null) {
            List<BookmarkEntity> bookmarks =
                bookmarkRepository.findAllByUserIdAndScriptIndex(command.userId(), command.contentId());
            UserContentBookmarks userContentBookmarks = new UserContentBookmarks(bookmarks);
            List<ContentInfo.UserScript> userScripts = userContentBookmarks.getUserScripts(scripts);

            boolean isScrapped = scrapCustomRepository.existsScrap(command.userId(), command.contentId());

            ContentInfo.LearningRateInfo learningRateInfo =
                recentLearningHistoryCustomRepository
                    .findLearningRateByUserIdAndContentId(command.userId(), command.contentId())
                    .orElse(ContentInfo.LearningRateInfo.createInitLearningRateInfo());

            ContentLevel customLevel =
                contentLevelFeedbackHistoryCustomRepository
                    .findContentLevelByUserIdAndContentId(command.userId(), command.contentId());

            return contentDtoMapper.buildDetail(content, isScrapped, learningRateInfo, userScripts, customLevel);
        }

        return contentDtoMapper.buildDetail(content, ContentInfo.UserScript.toResponse(scripts));
    }

    @Override
    public ContentEntity findLearnableContent(Long contentId, Long userId) {
        ContentEntity content = this.findContent(contentId);

        contentValidator.verifyLearnableContent(content, userId);

        return content;
    }

    // contentId로 content 조회
    @Override
    public ContentEntity find(Long contentId) {
        return this.findContent(contentId);
    }

    // Internal Methods ================================================================================================

    // contentId로 content 조회
    private ContentEntity findContent(Long contentId) {
        return contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }
}
