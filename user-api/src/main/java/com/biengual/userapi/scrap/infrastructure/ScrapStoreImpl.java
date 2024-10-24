package com.biengual.userapi.scrap.infrastructure;

import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.ScrapErrorCode.*;
import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapEntity;
import com.biengual.userapi.scrap.domain.ScrapRepository;
import com.biengual.userapi.scrap.domain.ScrapStore;
import com.biengual.userapi.scrap.presentation.ScrapDtoMapper;

import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ScrapStoreImpl implements ScrapStore {
	private final UserRepository userRepository;
	private final ScrapRepository scrapRepository;
	private final ContentRepository contentRepository;
	private final ScrapDtoMapper scrapDtoMapper;

	@Override
	public void createScrap(ScrapCommand.Create command) {
		UserEntity user = userRepository.findById(command.userId())
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		if (user.hasContent(command.contentId())) {
			throw new CommonException(SCRAP_ALREADY_EXISTS);
		}

		ScrapEntity scrap = scrapDtoMapper.buildEntity(
			contentRepository.findById(command.contentId())
				.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND))
		);

		scrapRepository.save(scrap);
		user.getScraps().add(scrap);
	}

	@Override
	public void deleteScrap(ScrapCommand.Delete command) {
		scrapRepository.deleteScrap(command.userId(), command.contentId());
	}
}
