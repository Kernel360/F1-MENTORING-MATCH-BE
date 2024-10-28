package com.biengual.userapi.content.domain;

public interface ContentStore {
	void createContent(ContentCommand.Create command);

	void modifyContent(Long id);
}
