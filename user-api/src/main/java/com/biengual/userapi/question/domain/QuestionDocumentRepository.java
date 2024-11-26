package com.biengual.userapi.question.domain;

import com.biengual.core.domain.document.question.QuestionDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuestionDocumentRepository extends MongoRepository<QuestionDocument, ObjectId> {
    @Query("{ '_id' : { $in: ?0 } }")
    List<QuestionDocument> findByIds(List<ObjectId> ids);
}
