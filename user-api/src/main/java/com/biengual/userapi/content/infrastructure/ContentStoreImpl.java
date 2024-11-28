package com.biengual.userapi.content.infrastructure;

import static com.biengual.core.constant.RestrictionConstant.*;
import static com.biengual.core.response.error.code.CategoryErrorCode.*;
import static com.biengual.core.response.error.code.ContentErrorCode.*;

import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.ContentSearchDocument;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.content.ContentLevelFeedbackDataMart;
import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentLevelFeedbackDataMartRepository;
import com.biengual.userapi.content.domain.ContentLevelFeedbackHistoryRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.content.domain.ContentSearchRepository;
import com.biengual.userapi.content.domain.ContentStore;
import com.biengual.userapi.validator.ContentValidator;

import lombok.RequiredArgsConstructor;

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
    private final ContentSearchRepository contentSearchRepository;

    @Override
    public void createContent(ContentCommand.Create command) {
        // MongoDB 에 Content Script 저장
        ContentDocument contentDocument = command.toDocument();
        contentDocumentRepository.save(contentDocument);

        CategoryEntity category = getCategoryEntity(command);

        // MySql 에 Content Info 저장
        ContentEntity content = command.toEntity(contentDocument.getId(), command.contentType(), category);
        contentRepository.save(content);

        // Open Search 에 Content Search Data 저장
        ContentSearchDocument searchDocument = ContentSearchDocument.createdByContents(content, contentDocument);
        contentSearchRepository.saveContent(searchDocument);
    }

    @Override
    public void modifyContentStatus(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        content.updateStatus(
            content.getContentStatus() == ContentStatus.ACTIVATED ?
                this.deactivateContent(contentId) :
                this.activateContent(contentId)
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
    public void reflectContentLevel(Set<Long> contentIdSet) {
        for (Long contentId : contentIdSet) {
            ContentEntity content = contentRepository.findById(contentId)
                .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

            ContentLevelFeedbackDataMart contentLevelFeedbackDataMart =
                contentLevelFeedbackDataMartRepository.findById(contentId)
                    .orElseThrow(() -> new CommonException(CONTENT_LEVEL_FEEDBACK_DATA_MART_NOT_FOUND));

            ContentLevel contentLevel = this.calculateContentLevel(contentLevelFeedbackDataMart);

            content.updateContentLevel(contentLevel);
        }
    }

    @Override
    public void initializeOpenSearch() {
        // 컨텐츠 데이터 저장
        List<ContentEntity> contents = contentRepository.findAll();
        for (ContentEntity content : contents) {
            ContentDocument document = contentDocumentRepository.findById(new ObjectId(content.getMongoContentId()))
                .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
            contentSearchRepository.saveContent(ContentSearchDocument.createdByContents(content, document));
        }
    }

    @Override
    public void delete() {
        List<Long> contentIds = contentRepository.findAll()
            .stream()
            .map(ContentEntity::getId)
            .toList();
        for (Long id : contentIds) {
            contentSearchRepository.deleteContent(String.valueOf(id));
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

    // 컨텐츠 활성화
    private ContentStatus activateContent(Long contentId) {
        contentSearchRepository.saveContent(
            ContentSearchDocument.createdByContents(
                this.getContentEntity(contentId),
                this.getContentDocument(contentId)
            )
        );
        return ContentStatus.ACTIVATED;
    }

    // 컨텐츠 비활성화
    private ContentStatus deactivateContent(Long contentId) {
        contentSearchRepository.deleteContent(String.valueOf(contentId));
        return ContentStatus.DEACTIVATED;
    }

    private ContentEntity getContentEntity(Long contentId) {
        return contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }

    private ContentDocument getContentDocument(Long contentId) {
        String documentId = contentCustomRepository.findMongoIdByContentId(contentId);

        return contentDocumentRepository.findContentDocumentById(new ObjectId(documentId))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }

    // 난이도 계산 로직
    private ContentLevel calculateContentLevel(ContentLevelFeedbackDataMart contentLevelFeedbackDataMart) {
        Long feedbackTotalCount = contentLevelFeedbackDataMart.getFeedbackTotalCount();

        if (feedbackTotalCount < MINIMUM_CONTENT_LEVEL_FEEDBACK_COUNT) {
            return null;
        }

        Long levelHighCount = contentLevelFeedbackDataMart.getLevelHighCount();
        Long levelMediumCount = contentLevelFeedbackDataMart.getLevelMediumCount();
        Long levelLowCount = contentLevelFeedbackDataMart.getLevelLowCount();

        double levelHighRatio = (double)levelHighCount / feedbackTotalCount;
        double levelMediumRatio = (double)levelMediumCount / feedbackTotalCount;
        double levelLowRatio = (double)levelLowCount / feedbackTotalCount;

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
