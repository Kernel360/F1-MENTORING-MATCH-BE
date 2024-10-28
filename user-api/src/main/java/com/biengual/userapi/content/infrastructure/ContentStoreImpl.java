package com.biengual.userapi.content.infrastructure;

import static com.biengual.userapi.message.error.code.CategoryErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.category.domain.CategoryEntity;
import com.biengual.userapi.category.repository.CategoryRepository;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentDocument;
import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.content.domain.ContentScriptRepository;
import com.biengual.userapi.content.domain.ContentStatus;
import com.biengual.userapi.content.domain.ContentStore;
import com.biengual.userapi.message.error.exception.CommonException;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ContentStoreImpl implements ContentStore {
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public void createContent(ContentCommand.Create command) {
		ContentDocument contentDocument = command.toDocument();
		contentScriptRepository.save(contentDocument);

		CategoryEntity category = getCategoryEntity(command);

		ContentEntity content = command.toEntity(contentDocument.getId(), command.contentType(), category);
		contentRepository.save(content);
	}

	@Override
	public void modifyContent(Long contentId) {
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
