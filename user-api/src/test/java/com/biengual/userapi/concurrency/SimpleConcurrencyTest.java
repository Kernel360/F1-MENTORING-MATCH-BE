package com.biengual.userapi.concurrency;

import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.userapi.config.JpaConfig;
import com.biengual.userapi.config.TestQueryDslConfig;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.*;

import static com.biengual.core.domain.entity.content.ContentDomainFixture.TestContentEntity.createContentEntity;
import static com.biengual.core.domain.entity.content.QContentEntity.contentEntity;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, TestQueryDslConfig.class})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class SimpleConcurrencyTest {

    Logger log = LoggerFactory.getLogger(SimpleConcurrencyTest.class);

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentCustomRepository contentCustomRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    private Long initContentId;

    private Integer initHits;

    @BeforeEach
    void init() throws ExecutionException, InterruptedException {
        queryFactory = new JPAQueryFactory(entityManager);

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<ContentEntity> submit = executorService.submit(() -> {
            ContentEntity content = createContentEntity().build().get();
            return contentRepository.save(content);
        });

        ContentEntity initContent = submit.get();

        initContentId = initContent.getId();
        initHits = initContent.getHits();
    }

    // TODO: 실제 contentCustomRepository에서 구현한 QueryDsl은 데이터베이스 내에 원자적으로 실행이 되는 듯한데,
    //  동시성 보장을 해야 하는가?
    @Test
    @DisplayName("실제 조회수 증가 로직 동시성 테스트 - 통과하면 동시성 이슈 발생하지 않았음")
    void ActualHitsIncreaseConcurrencyTest() throws InterruptedException {

        int threadCount = 100; // 동시 실행할 스레드 수

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When: 여러 스레드가 동시에 조회수를 증가
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    long randomDelay = (long) (Math.random() * 100);  // 0~100 밀리초 사이의 값
                    Thread.sleep(randomDelay);
                    transactionTemplate.execute(status -> {
                        ContentEntity content = contentRepository.findById(initContentId).orElseThrow();
                        contentCustomRepository.increaseHitsByContentId(content.getId());
                        return null;
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        ContentEntity afterContent = contentRepository.findById(initContentId).orElseThrow();

        log.info("예상 조회수: {}, 트랜잭션 종료 후 조회수: {}", initHits + threadCount, afterContent.getHits());

        assertThat(afterContent.getHits()).isEqualTo(initHits + threadCount);
    }

    // 동시성 이슈가 생기게끔 구현된 로직
    @Test
    @DisplayName("가상 조회수 증가 로직 동시성 테스트 - 통과하면 동시성 이슈 발생했음")
    void virtualHitsIncreaseConcurrencyTest() throws InterruptedException {

        int threadCount = 100; // 동시 실행할 스레드 수

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When: 여러 스레드가 동시에 조회수를 증가
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    long randomDelay = (long) (Math.random() * 1000);  // 0~1000 밀리초 사이의 값
                    Thread.sleep(randomDelay);
                    transactionTemplate.execute(status -> {
                        increaseHitsForConcurrencyIssue(initContentId);
                        return null;
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        ContentEntity afterContent = contentRepository.findById(initContentId).orElseThrow();

        log.info("예상 조회수: {}, 트랜잭션 종료 후 조회수: {}", initHits + threadCount, afterContent.getHits());

        assertThat(afterContent.getHits()).isLessThan(initHits + threadCount);
    }

    // Internal Method =================================================================================================

    // 동시성 이슈를 발생시키기 위한 쿼리
    private void increaseHitsForConcurrencyIssue(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId).orElseThrow();

        queryFactory
            .update(contentEntity)
            .set(contentEntity.hits, content.getHits() + 1)
            .where(contentEntity.id.eq(contentId))
            .execute();
    }
}
