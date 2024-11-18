package com.biengual.userapi.content.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.content.domain.*;
import lombok.RequiredArgsConstructor;

import static com.biengual.core.response.error.code.CategoryErrorCode.CATEGORY_NOT_FOUND;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_LEVEL_FEEDBACK_HISTORY_ALREADY_EXISTS;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class ContentStoreImpl implements ContentStore {
    private final ContentCustomRepository contentCustomRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final CategoryRepository categoryRepository;
    private final ContentLevelFeedbackHistoryRepository contentLevelFeedbackHistoryRepository;

    @Override
    public void createContent(ContentCommand.Create command) {
        ContentDocument contentDocument = command.toDocument();
        contentDocumentRepository.save(contentDocument);

        CategoryEntity category = getCategoryEntity(command);

        ContentEntity content = command.toEntity(contentDocument.getId(), command.contentType(), category);
        contentRepository.save(content);
    }

    @Override
    public void modifyContentStatus(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
        content.updateStatus(
            content.getContentStatus() == ContentStatus.ACTIVATED ? ContentStatus.DEACTIVATED : ContentStatus.ACTIVATED
        );
        contentRepository.save(content);
    }

    @Override
    public void increaseHits(Long contentId) {
        contentCustomRepository.increaseHitsByContentId(contentId);
    }

    // 컨텐츠 난이도 피드백 기록
    @Override
    public void recordContentLevelFeedbackHistory(ContentCommand.SubmitLevelFeedback command) {

        validateAlreadySubmitLevelFeedback(command);

        contentLevelFeedbackHistoryRepository.save(command.toContentLevelFeedbackHistoryEntity());
    }

    // Internal Methods=================================================================================================

    private CategoryEntity getCategoryEntity(ContentCommand.Create command) {
        if (categoryRepository.existsByName(command.category())) {
            return categoryRepository.findByName(command.category())
                .orElseThrow(() -> new CommonException(CATEGORY_NOT_FOUND));
        }

        return categoryRepository.save(command.toCategoryEntity());
    }

    // 이미 해당 컨텐츠에 대해 난이도 피드백을 했는지 검증
    private void validateAlreadySubmitLevelFeedback(ContentCommand.SubmitLevelFeedback command) {
        if (contentLevelFeedbackHistoryRepository.existsByUserIdAndContentId(command.userId(), command.contentId())) {
            throw new CommonException(CONTENT_LEVEL_FEEDBACK_HISTORY_ALREADY_EXISTS);
        }
    }
}
