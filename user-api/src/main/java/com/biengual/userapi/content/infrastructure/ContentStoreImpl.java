package com.biengual.userapi.content.infrastructure;

import static com.biengual.core.response.error.code.CategoryErrorCode.*;
import static com.biengual.core.response.error.code.ContentErrorCode.*;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.content.domain.ContentStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ContentStoreImpl implements ContentStore {
    private final ContentCustomRepository contentCustomRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final CategoryRepository categoryRepository;

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

    // Internal Methods=================================================================================================

    private CategoryEntity getCategoryEntity(ContentCommand.Create command) {
        if (categoryRepository.existsByName(command.category())) {
            return categoryRepository.findByName(command.category())
                .orElseThrow(() -> new CommonException(CATEGORY_NOT_FOUND));
        }

        return categoryRepository.save(command.toCategoryEntity());
    }
}
