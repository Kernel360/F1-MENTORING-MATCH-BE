package com.biengual.userapi.question.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.biengual.userapi.core.domain.entity.question.document.QuestionDocument;

public interface QuestionRepository extends MongoRepository<QuestionDocument, ObjectId> {
}
