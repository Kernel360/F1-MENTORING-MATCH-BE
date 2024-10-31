package com.biengual.userapi.question.domain;

import com.biengual.core.domain.document.question.QuestionDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<QuestionDocument, ObjectId> {

}
