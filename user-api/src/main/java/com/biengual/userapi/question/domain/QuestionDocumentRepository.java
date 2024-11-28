package com.biengual.userapi.question.domain;

import com.biengual.core.domain.document.question.QuestionDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionDocumentRepository extends MongoRepository<QuestionDocument, ObjectId> {
    List<QuestionInfo.Detail> findByIdIn(List<ObjectId> ids);
}
