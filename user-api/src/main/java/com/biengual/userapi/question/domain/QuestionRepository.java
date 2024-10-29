package com.biengual.userapi.question.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.biengual.userapi.core.entity.question.QuestionDocument;

public interface QuestionRepository extends MongoRepository<QuestionDocument, ObjectId> {
}
