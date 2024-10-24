package com.biengual.userapi.scrap.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapInfo;
import com.biengual.userapi.scrap.domain.ScrapReader;
import com.biengual.userapi.scrap.domain.ScrapService;
import com.biengual.userapi.scrap.domain.ScrapStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {
	private final ScrapReader scrapReader;
	private final ScrapStore scrapStore;

	@Override
	@Transactional(readOnly = true)
	public ScrapInfo.ViewInfo getAllScraps(Long userId) {
		return ScrapInfo.ViewInfo.of(scrapReader.findAllByUserId(userId));
	}

	@Override
	@Transactional
	public void createScrap(ScrapCommand.Create command) {
		scrapStore.createScrap(command);
	}

	@Override
	@Transactional
	public void deleteScrap(ScrapCommand.Delete command) {
		scrapStore.deleteScrap(command);
	}

	@Override
	public boolean existsScrap(ScrapCommand.GetByContents command) {
		return scrapReader.existsScrap(command);
	}
}
