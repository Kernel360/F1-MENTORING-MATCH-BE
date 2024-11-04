package com.biengual.userapi.content.domain;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.biengual.core.domain.document.content.ContentAccessDocument;

@Repository
public interface ContentAccessDocumentRepository extends MongoRepository<ContentAccessDocument, Long> {
    Optional<ContentAccessDocument> findByContentId(Long contentId);
}
