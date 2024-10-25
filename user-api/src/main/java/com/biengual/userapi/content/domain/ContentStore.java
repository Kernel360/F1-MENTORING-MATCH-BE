package com.biengual.userapi.content.domain;

public interface ContentStore {
	void createContent(ContentCommand.Create command);

	void updateContent(ContentCommand.Modify command);

	void deactivateContent(Long id);
}
