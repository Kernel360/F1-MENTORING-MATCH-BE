package com.biengual.userapi.content.domain;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.biengual.core.domain.document.content.ContentDocument;

/**
 * ContentDocument의 Repository 계층의 인터페이스
 *
 * @author 김영래
 */
public interface ContentDocumentRepository extends MongoRepository<ContentDocument, ObjectId> {

	Optional<ContentDocument> findById(ObjectId id);

	Optional<ContentDocument> findByScripts(List<String> script);

	Optional<ContentDocument> findContentDocumentById(ObjectId mongoContentId);

}
