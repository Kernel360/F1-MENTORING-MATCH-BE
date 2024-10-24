package com.biengual.userapi.scrap.domain;

public interface ScrapStore {
	void createScrap(ScrapCommand.Create command);

	void deleteScrap(ScrapCommand.Delete command);
}
