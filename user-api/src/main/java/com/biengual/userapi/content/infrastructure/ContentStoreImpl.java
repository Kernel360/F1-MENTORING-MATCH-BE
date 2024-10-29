package com.biengual.userapi.content.infrastructure;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.core.entity.category.CategoryEntity;
import com.biengual.userapi.category.domain.CategoryRepository;
import com.biengual.userapi.content.domain.*;
import com.biengual.userapi.core.common.enums.ContentStatus;
import com.biengual.userapi.core.entity.content.ContentDocument;
import com.biengual.userapi.core.entity.content.ContentEntity;
import com.biengual.userapi.core.message.error.exception.CommonException;
import lombok.RequiredArgsConstructor;

import static com.biengual.userapi.core.message.error.code.CategoryErrorCode.CATEGORY_NOT_FOUND;
import static com.biengual.userapi.core.message.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

@DataProvider
@RequiredArgsConstructor
public class ContentStoreImpl implements ContentStore {
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

	// Internal Methods=================================================================================================

	private CategoryEntity getCategoryEntity(ContentCommand.Create command) {
		if (categoryRepository.existsByName(command.category())) {
			return categoryRepository.findByName(command.category())
				.orElseThrow(() -> new CommonException(CATEGORY_NOT_FOUND));
		}

		return categoryRepository.save(command.toCategoryEntity());
	}
}
