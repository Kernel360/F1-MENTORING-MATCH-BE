package com.biengual.userapi.content.domain;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.biengual.core.domain.document.content.ContentDocument;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ContentDocumentCustomRepository {
    private final MongoTemplate mongoTemplate;

    public void updateQuestionIds(ObjectId id, List<String> questionIds) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("questionIds", questionIds);
        mongoTemplate.updateFirst(query, update, ContentDocument.class);
    }

    // TODO: 일회용으로 쓰고 삭제 예정
    public void deleteAllQuestionIds() {
        Query query = new Query();
        Update update = new Update().set("questionIds", new ArrayList<>());
        mongoTemplate.updateMulti(query, update, ContentDocument.class);
    }
}
