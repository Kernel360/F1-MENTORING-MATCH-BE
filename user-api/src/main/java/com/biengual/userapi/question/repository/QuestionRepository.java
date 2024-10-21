package com.biengual.userapi.question.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.biengual.userapi.question.domain.entity.QuestionDocument;

public interface QuestionRepository extends MongoRepository<QuestionDocument, ObjectId> {

}
