package com.biengual.userapi.scrap.domain;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.userapi.core.entity.scrap.QScrapEntity;
import com.biengual.userapi.core.entity.scrap.ScrapEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScrapCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<ScrapEntity> findAllByUserId(Long userId) {
        QScrapEntity scrapEntity = QScrapEntity.scrapEntity;

        return queryFactory.select(scrapEntity)
            .from(scrapEntity)
            .orderBy(scrapEntity.createdAt.desc())
            .where(scrapEntity.userId.eq(userId))
            .fetch();
    }

    public void deleteScrap(ScrapCommand.Delete command) {
        QScrapEntity scrapEntity = QScrapEntity.scrapEntity;

        queryFactory.delete(scrapEntity)
            .where(scrapEntity.userId.eq(command.userId()))
            .where(scrapEntity.content.id.eq(command.contentId()))
            .execute();

    }

    public boolean existsScrap(Long userId, Long contentId) {
        QScrapEntity scrapEntity = QScrapEntity.scrapEntity;

        return queryFactory.select(scrapEntity)
            .from(scrapEntity)
            .where(scrapEntity.userId.eq(userId))
            .where(scrapEntity.content.id.eq(contentId))
            .fetchFirst() != null;
    }
}
