package com.biengual.userapi.content.domain;

import java.util.List;

import com.biengual.core.domain.document.content.ContentSearchDocument;

public interface ContentSearchRepository {
    List<ContentSearchDocument> searchByFields(String keyword);

    void saveContent(ContentSearchDocument document);

    void deleteContent(String id);
}
