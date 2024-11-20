package com.biengual.userapi.content.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.content.ContentLevelFeedbackDataMart;
import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.content.domain.*;
import com.biengual.userapi.validator.ContentValidator;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.biengual.core.constant.RestrictionConstant.CONTENT_LEVEL_DETERMINATION_THRESHOLD;
import static com.biengual.core.constant.RestrictionConstant.MINIMUM_CONTENT_LEVEL_FEEDBACK_COUNT;
import static com.biengual.core.response.error.code.CategoryErrorCode.CATEGORY_NOT_FOUND;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_LEVEL_FEEDBACK_DATA_MART_NOT_FOUND;
import static com.biengual.core.response.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class ContentStoreImpl implements ContentStore {
    private final ContentCustomRepository contentCustomRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final CategoryRepository categoryRepository;
    private final ContentLevelFeedbackHistoryRepository contentLevelFeedbackHistoryRepository;
    private final ContentLevelFeedbackDataMartRepository contentLevelFeedbackDataMartRepository;
    private final ContentValidator contentValidator;

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
        contentValidator.verifyAlreadySubmitLevelFeedback(command.userId(), command.contentId());

        contentLevelFeedbackHistoryRepository.save(command.toContentLevelFeedbackHistoryEntity());
    }

    // ContentLevelFeedback에 대해 집계된 Content에 컨텐츠 난이도 반영
    @Override
    public void reflectContentLevel(List<Long> contentIdList) {
        for (Long contentId : contentIdList) {
            ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

            ContentLevelFeedbackDataMart contentLevelFeedbackDataMart =
                contentLevelFeedbackDataMartRepository.findById(contentId)
                    .orElseThrow(() -> new CommonException(CONTENT_LEVEL_FEEDBACK_DATA_MART_NOT_FOUND));

            ContentLevel contentLevel = this.determineContentLevel(contentLevelFeedbackDataMart);

            content.updateContentLevel(contentLevel);
        }
    }

    // Internal Methods=================================================================================================

    private CategoryEntity getCategoryEntity(ContentCommand.Create command) {
        if (categoryRepository.existsByName(command.category())) {
            return categoryRepository.findByName(command.category())
                .orElseThrow(() -> new CommonException(CATEGORY_NOT_FOUND));
        }

        return categoryRepository.save(command.toCategoryEntity());
    }

    // 난이도 계산 로직
    private ContentLevel determineContentLevel(ContentLevelFeedbackDataMart contentLevelFeedbackDataMart) {
        Long feedbackTotalCount = contentLevelFeedbackDataMart.getFeedbackTotalCount();

        if (feedbackTotalCount < MINIMUM_CONTENT_LEVEL_FEEDBACK_COUNT) {
            return null;
        }

        Long levelHighCount = contentLevelFeedbackDataMart.getLevelHighCount();
        Long levelMediumCount = contentLevelFeedbackDataMart.getLevelMediumCount();
        Long levelLowCount = contentLevelFeedbackDataMart.getLevelLowCount();

        double levelHighRatio = (double) levelHighCount / feedbackTotalCount;
        double levelMediumRatio = (double) levelMediumCount / feedbackTotalCount;
        double levelLowRatio = (double) levelLowCount / feedbackTotalCount;

        if (levelMediumRatio > levelHighRatio && levelMediumRatio > levelLowRatio) {
            return ContentLevel.MEDIUM;
        }

        if (levelHighRatio * CONTENT_LEVEL_DETERMINATION_THRESHOLD >= levelLowRatio) {
            return ContentLevel.HIGH;
        }

        if (levelLowRatio * CONTENT_LEVEL_DETERMINATION_THRESHOLD >= levelHighRatio) {
            return ContentLevel.LOW;
        }

        return ContentLevel.MEDIUM;
    }
}
