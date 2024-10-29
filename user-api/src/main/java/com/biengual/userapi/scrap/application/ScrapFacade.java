package com.biengual.userapi.scrap.application;

import com.biengual.core.annotation.Facade;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapInfo;
import com.biengual.userapi.scrap.domain.ScrapService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class ScrapFacade {
	private final ScrapService scrapService;

	public ScrapInfo.ViewInfo getAllScraps(Long userId) {
		return scrapService.getAllScraps(userId);
	}

	public boolean existsScrap(ScrapCommand.GetByContents command) {
		return scrapService.existsScrap(command);
	}

	public void createScrap(ScrapCommand.Create command) {
		scrapService.createScrap(command);
	}

	public void deleteScrap(ScrapCommand.Delete command) {
		scrapService.deleteScrap(command);
	}
}
