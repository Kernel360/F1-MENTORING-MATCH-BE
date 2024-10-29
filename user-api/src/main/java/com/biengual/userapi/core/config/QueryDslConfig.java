package com.biengual.userapi.core.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDsl을 사용하는 Repository에서 JPAQueryFactory를 주입받기 위한 Configuration
 *
 * @author 문찬욱
 */
@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {
    private final EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
