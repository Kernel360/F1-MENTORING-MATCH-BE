package com.biengual.userapi.scrap.infrastructure;

import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.ScrapErrorCode.*;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapCustomRepository;
import com.biengual.userapi.scrap.domain.ScrapEntity;
import com.biengual.userapi.scrap.domain.ScrapRepository;
import com.biengual.userapi.scrap.domain.ScrapStore;
import com.biengual.userapi.scrap.presentation.ScrapDtoMapper;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ScrapStoreImpl implements ScrapStore {
	private final ScrapRepository scrapRepository;
	private final ScrapCustomRepository scrapCustomRepository;
	private final ScrapDtoMapper scrapDtoMapper;
	private final ContentRepository contentRepository;

	@Override
	public void createScrap(ScrapCommand.Create command) {
		if (scrapCustomRepository.existsScrap(command.userId(), command.contentId())) {
			throw new CommonException(SCRAP_ALREADY_EXISTS);
		}

		ScrapEntity scrap = scrapDtoMapper.buildEntity(
			command.userId(),
			contentRepository.findById(command.contentId())
				.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND))
		);

		scrapRepository.save(scrap);
	}

	@Override
	public void deleteScrap(ScrapCommand.Delete command) {
		if(!scrapCustomRepository.deleteScrap(command)){
			throw  new CommonException(SCRAP_NOT_FOUND);
		}
	}
}
