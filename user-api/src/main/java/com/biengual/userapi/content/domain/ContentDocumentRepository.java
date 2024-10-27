package com.biengual.userapi.content.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContentDocumentRepository extends MongoRepository<ContentDocument, ObjectId> {

	Optional<ContentDocument> findById(ObjectId id);

	Optional<ContentDocument> findByScripts(List<String> script);

	Optional<ContentDocument> findContentDocumentById(ObjectId mongoContentId);

}
