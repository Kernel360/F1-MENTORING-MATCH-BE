package com.biengual.userapi.content.repository;

import com.biengual.userapi.content.domain.entity.ContentDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ContentScriptRepository extends MongoRepository<ContentDocument, ObjectId> {

	Optional<ContentDocument> findById(ObjectId id);

	Optional<ContentDocument> findByScripts(List<String> script);

	Optional<ContentDocument> findContentDocumentById(ObjectId mongoContentId);

}
