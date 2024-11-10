package com.biengual.userapi.concurrency;

import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.content.ContentEntityWithVersion;
import com.biengual.userapi.config.TestJpaConfig;
import com.biengual.userapi.config.TestQueryDslConfig;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.repository.OptimisticLockContentRepository;
import com.biengual.userapi.repository.PessimisticLockContentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.biengual.core.domain.entity.content.ContentDomainFixture.TestContentEntity.createContentEntity;
import static com.biengual.core.domain.entity.content.ContentDomainFixture.TestContentEntityWithVersion.createContentEntityWithVersion;
import static com.biengual.core.domain.entity.content.QContentEntity.contentEntity;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({TestJpaConfig.class, TestQueryDslConfig.class})
@DisplayName("컨텐츠 조회수 동시성 테스트 - 기본적으로 100 스레드에서 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SimpleConcurrencyTest {

    Logger log = LoggerFactory.getLogger(SimpleConcurrencyTest.class);

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private PessimisticLockContentRepository pessimisticLockContentRepository;

    @Autowired
    private OptimisticLockContentRepository optimisticLockContentRepository;

    @Autowired
    private ContentCustomRepository contentCustomRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    private int threadCount;

    private Long initContentId;
    private Integer initHits;

    private Long initContentIdWithVersion;
    private Integer initHitsWithVersion;

    @BeforeEach
    void init() throws InterruptedException {
        queryFactory = new JPAQueryFactory(entityManager);

        // 동시에 실행할 스레드 수
        threadCount = 100;

        // 테스트에 필요한 초기 Entity들 생성
        createExecutorService(1, this::createInitContentEntity);
        createExecutorService(1, this::createInitContentEntityWithVersion);
    }

    @AfterEach
    void rollback() throws InterruptedException {
        createExecutorService(1, this::deleteAll);
    }

    // TODO: 실제 contentCustomRepository에서 구현한 QueryDsl은 데이터베이스 내에 원자적으로 실행이 되는 듯한데,
    //  동시성 보장을 해야 하는가?
    @Test
    @DisplayName("실제 조회수 증가 로직 동시성 테스트 - 통과하면 동시성 이슈 발생하지 않았음")
    void ActualHitsIncreaseConcurrencyTest() throws InterruptedException {
        StopWatch stopWatch = StopWatch.createStarted();
        createExecutorService(threadCount, this::increaseHits);
        stopWatch.stop();

        ContentEntity afterContent = contentRepository.findById(initContentId).orElseThrow();
        log.info("실제 - 예상 조회수: {}, 트랜잭션 종료 후 조회수: {}, 실행 스레드 수: {}, 걸린 시간: {} ms",
            initHits + threadCount, afterContent.getHits(), threadCount, stopWatch.getTime(TimeUnit.MILLISECONDS));

        assertThat(afterContent.getHits()).isEqualTo(initHits + threadCount);
    }

    // 동시성 이슈가 생기게끔 구현된 로직
    @Test
    @DisplayName("가상 조회수 증가 로직 동시성 테스트 - 통과하면 동시성 이슈 발생했음")
    void virtualHitsIncreaseConcurrencyTest() throws InterruptedException {
        StopWatch stopWatch = StopWatch.createStarted();
        createExecutorService(threadCount, this::increaseHitsForConcurrencyIssue);
        stopWatch.stop();

        ContentEntity afterContent = contentRepository.findById(initContentId).orElseThrow();
        log.info("가상 - 예상 조회수: {}, 트랜잭션 종료 후 조회수: {}, 실행 스레드 수: {}, 걸린 시간: {} ms",
            initHits + threadCount, afterContent.getHits(), threadCount, stopWatch.getTime(TimeUnit.MILLISECONDS));

        assertThat(afterContent.getHits()).isLessThan(initHits + threadCount);
    }

    /*
     비관적 락을 건 동시성 테스트
     - 10 스레드 기준 640 ms
     - 1000 스레드 기준 6618 ms
     */
    @Test
    @DisplayName("가상 조회수 증가 로직에 비관적 락을 건 동시성 테스트 - 통과하면 동시성 이슈 발생하지 않았음")
    void virtualHitsIncreaseConcurrencyTestWithPessimisticLock() throws InterruptedException {
        StopWatch stopWatch = StopWatch.createStarted();
        createExecutorService(threadCount, this::increaseHitsForConcurrencyIssueWithPessimisticLock);
        stopWatch.stop();

        ContentEntity afterContent = contentRepository.findById(initContentId).orElseThrow();
        log.info("비관 - 예상 조회수: {}, 트랜잭션 종료 후 조회수: {}, 실행 스레드 수: {}, 걸린 시간: {} ms",
            initHits + threadCount, afterContent.getHits(), threadCount, stopWatch.getTime(TimeUnit.MILLISECONDS));

        assertThat(afterContent.getHits()).isEqualTo(initHits + threadCount);
    }

    /*
     낙관적 락을 건 동시성 테스트
     - 10 스레드 기준 294 ms
     - 1000 스레드 기준 13638 ms, 오히려 비관적 락보다 느렸음.
     */
    @Test
    @DisplayName("가상 조회수 증가 로직에 낙관적 락을 건 동시성 테스트 - 통과하면 동시성 이슈 발생하지 않았음")
    void virtualHitsIncreaseConcurrencyTestWithOptimisticLock() throws InterruptedException {
        // 낙관적 락에서 사용할 최대 재시도 횟수 설정
        int maxRetries = 50;

        StopWatch stopWatch = StopWatch.createStarted();
        createExecutorService(threadCount, () -> increaseHitsForConcurrencyIssueWithOptimisticLock(maxRetries));
        stopWatch.stop();

        ContentEntityWithVersion afterContent = optimisticLockContentRepository.findById(initContentIdWithVersion).orElseThrow();
        log.info("낙관 - 예상 조회수: {}, 트랜잭션 종료 후 조회수: {}, 실행 스레드 수: {}, 걸린 시간: {} ms",
            initHits + threadCount, afterContent.getHits(), threadCount, stopWatch.getTime(TimeUnit.MILLISECONDS));

        assertThat(afterContent.getHits()).isEqualTo(initHitsWithVersion + threadCount);
    }



    // Internal Method =================================================================================================

    // ExecutorService 캡슐화
    private void createExecutorService(int threadCount, Runnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    task.run();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }

    // 초기 ContentEntity 생성
    private void createInitContentEntity() {
        ContentEntity content = createContentEntity().build().get();
        ContentEntity save = contentRepository.save(content);

        initContentId = save.getId();
        initHits = save.getHits();
    }

    // 초기 ContentEntityWithVersion 생성
    private void createInitContentEntityWithVersion() {
        ContentEntityWithVersion content = createContentEntityWithVersion().build().get();
        ContentEntityWithVersion save = optimisticLockContentRepository.save(content);

        initContentIdWithVersion = save.getId();
        initHitsWithVersion = save.getHits();
    }

    // 모든 초기 Entity 삭제
    private void deleteAll() {
        contentRepository.deleteAll();
        optimisticLockContentRepository.deleteAll();
    }

    // 실제 조회수 증가 기능의 content 관련 쿼리
    private void increaseHits() {
        transactionTemplate.execute(status -> {
            try {
                contentRepository.findById(initContentId).orElseThrow();
                contentCustomRepository.increaseHitsByContentId(initContentId);
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    // 동시성 이슈를 발생시키기 위한 쿼리
    private void increaseHitsForConcurrencyIssue() {
        transactionTemplate.execute(status -> {
            try {
                ContentEntity content = contentRepository.findById(initContentId).orElseThrow();

                queryFactory
                    .update(contentEntity)
                    .set(contentEntity.hits, content.getHits() + 1)
                    .where(contentEntity.id.eq(content.getId()))
                    .execute();

                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    // findById에 비관적 락을 건 쿼리
    private void increaseHitsForConcurrencyIssueWithPessimisticLock() {
        transactionTemplate.execute(status -> {
            try {
                ContentEntity content = pessimisticLockContentRepository.findById(initContentId).orElseThrow();

                queryFactory
                    .update(contentEntity)
                    .set(contentEntity.hits, content.getHits() + 1)
                    .where(contentEntity.id.eq(content.getId()))
                    .execute();

                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    // 낙관적 락을 건 쿼리
    private void increaseHitsForConcurrencyIssueWithOptimisticLock(int maxRetries) {
        int attempt = 0;
        while (true) {
            try {
                transactionTemplate.execute(status -> {
                    try {
                        ContentEntityWithVersion content = optimisticLockContentRepository.findById(initContentIdWithVersion).orElseThrow();
                        content.updateHits();
                        return null;
                    } catch (Exception e) {
                        status.setRollbackOnly();
                        throw e;
                    }
                });
                break; // 성공 시 재시도 종료
            } catch (ObjectOptimisticLockingFailureException e) {
                attempt++;

                if (attempt >= maxRetries) {
                    throw e; // 재시도 초과 시 예외를 발생시킴
                }

                try {
                    Thread.sleep(100); // 100ms 대기한 후 다시 시도
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("재시도 대기 중 인터럽트 발생", ie);
                }
            }
        }
    }
}
