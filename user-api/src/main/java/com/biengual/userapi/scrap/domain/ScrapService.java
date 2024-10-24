package com.biengual.userapi.scrap.domain;

public interface ScrapService {
	ScrapInfo.ViewInfo getAllScraps(Long userId);

	void createScrap(ScrapCommand.Create command);

	void deleteScrap(ScrapCommand.Delete command);

	boolean existsScrap(ScrapCommand.GetByContents command);
}
