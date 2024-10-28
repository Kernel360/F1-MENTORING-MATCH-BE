package com.biengual.userapi.scrap.infrastructure;

import java.util.List;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapCustomRepository;
import com.biengual.userapi.scrap.domain.ScrapEntity;
import com.biengual.userapi.scrap.domain.ScrapInfo;
import com.biengual.userapi.scrap.domain.ScrapReader;
import com.biengual.userapi.scrap.domain.ScrapRepository;
import com.biengual.userapi.scrap.presentation.ScrapDtoMapper;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ScrapReaderImpl implements ScrapReader {
	private final ScrapRepository scrapRepository;
	private final ScrapCustomRepository scrapCustomRepository;
	private final ScrapDtoMapper scrapDtoMapper;

	@Override
	public List<ScrapInfo.View> findAllByUserId(Long userId) {
		List<ScrapEntity> scraps = scrapCustomRepository.findAllByUserId(userId);
		return scraps.stream()
			.map(scrapDtoMapper::buildView)
			.toList();
	}

	@Override
	public boolean existsScrap(ScrapCommand.GetByContents command) {
		return scrapCustomRepository.existsScrap(command.userId(), command.contentId());
	}
}
