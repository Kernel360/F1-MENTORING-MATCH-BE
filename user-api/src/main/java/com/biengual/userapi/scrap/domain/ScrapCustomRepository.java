package com.biengual.userapi.scrap.domain;

import static com.biengual.core.domain.entity.scrap.QScrapEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.core.domain.entity.scrap.ScrapEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ScrapCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<ScrapEntity> findAllByUserId(Long userId) {
        return queryFactory.select(scrapEntity)
            .from(scrapEntity)
            .orderBy(scrapEntity.createdAt.desc())
            .where(scrapEntity.userId.eq(userId))
            .fetch();
    }

    public List<Long> findAllIdsByUserId(Long userId) {
        return queryFactory
            .select(scrapEntity.id)
            .from(scrapEntity)
            .where(scrapEntity.userId.eq(userId))
            .fetch();
    }

    public void deleteScrap(ScrapCommand.Delete command) {
        queryFactory.delete(scrapEntity)
            .where(scrapEntity.userId.eq(command.userId()))
            .where(scrapEntity.content.id.eq(command.contentId()))
            .execute();

    }

    public boolean existsScrap(Long userId, Long contentId) {
        return queryFactory.select(scrapEntity)
            .from(scrapEntity)
            .where(scrapEntity.userId.eq(userId))
            .where(scrapEntity.content.id.eq(contentId))
            .fetchFirst() != null;
    }
}
