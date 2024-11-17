package com.biengual.userapi.domain.point;

import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.userapi.common.util.ExecutorServiceUtil;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static com.biengual.userapi.domain.user.UserTestFixture.TestUserEntity.createUserEntity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 포인트 도메인에 대한 동시성을 테스트하기 위한 테스트 클래스
 *
 * 현재는 PointManager만 호출하여 테스트하였고,
 * PointManager를 호출하는 메서드에 대한 테스트는 진행하지 않았음
 *
 * @author 문찬욱
 */
@SpringBootTest
@DisplayName("포인트 도메인 동시성 테스트")
public class PointConcurrencyTest {

    private Logger log = LoggerFactory.getLogger(PointConcurrencyTest.class);
    private Long initUserId;
    private Long initCurrentPoint;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestPointHistoryRepository testPointHistoryRepository;

    @Autowired
    private PointManager pointManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void init() {
        UserEntity user = createUserEntity().build().get();

        UserEntity savedUser = userRepository.save(user);

        initUserId = savedUser.getId();
        initCurrentPoint = savedUser.getCurrentPoint();
    }

    @Test
    @DisplayName("컨텍스트 로드 테스트")
    void contextLoads() {
    }

    @Test
    @DisplayName("PointManager의 updateAndSavePoint 메서드 서로 다른 reason으로 동시 호출 시 currentPoint 및 balance 동시성 테스트")
    void updateAndSavePoint_ShouldEnsureConcurrency_WhenCalledSimultaneouslyWithDifferentReasons() throws InterruptedException {
        // given
        PointReason[] reasons = PointReason.values();
        AtomicLong currentPoint = new AtomicLong(initCurrentPoint);

        // when
        ExecutorServiceUtil.createExecutorService(reasons, reason -> {
            updateAndSavePointTransaction(reason, currentPoint);
        });

        // then
        UserEntity user = userRepository.findById(initUserId).orElseThrow();
        PointHistoryEntity pointHistory = testPointHistoryRepository.findTopByOrderByIdDesc().orElseThrow();

        log.info("User의 CurrentPoint: {}", user.getCurrentPoint());
        log.info("PointHistory의 PointBalance: {}", pointHistory.getPointBalance());

        assertThat(currentPoint.get()).isEqualTo(user.getCurrentPoint());
        assertThat(currentPoint.get()).isEqualTo(pointHistory.getPointBalance());
    }

    @Test
    @DisplayName("PointManager의 updateAndSavePoint 메서드 같은 reason으로 동시 호출 시 currentPoint 및 balance 동시성 테스트")
    void updateAndSavePoint_ShouldEnsureConcurrency_WhenCalledSimultaneouslyWithSameReasons() throws InterruptedException {
        // given
        PointReason reason = this.getRandomPointReason();
        int threadCount = 10;
        AtomicLong currentPoint = new AtomicLong(initCurrentPoint);

        // when
        ExecutorServiceUtil.createExecutorService(threadCount, () -> {
            updateAndSavePointTransaction(reason, currentPoint);
        });

        // then
        UserEntity user = userRepository.findById(initUserId).orElseThrow();
        PointHistoryEntity pointHistory = testPointHistoryRepository.findTopByOrderByIdDesc().orElseThrow();

        log.info("InitCurrentPoint: {}", initCurrentPoint);
        log.info("PointReason: {}, PointValue: {}, threadCount: {}", reason, reason.getValue(), threadCount);
        log.info("User의 CurrentPoint: {}", user.getCurrentPoint());
        log.info("PointHistory의 PointBalance: {}", pointHistory.getPointBalance());

        assertThat(currentPoint.get()).isEqualTo(user.getCurrentPoint());
        assertThat(currentPoint.get()).isEqualTo(pointHistory.getPointBalance());
    }

    // Internal Method =================================================================================================

    // updateAndSavePoint를 트랜잭션 단위로 설정 위한 메서드
    private void updateAndSavePointTransaction(PointReason reason, AtomicLong currentPoint) {
        transactionTemplate.execute(status -> {
            pointManager.updateAndSavePoint(reason, initUserId);
            return null;
        });

        currentPoint.addAndGet(reason.getValue());
    }

    // 랜덤한 PointReason을 뽑기 위한 메서드
    private PointReason getRandomPointReason() {
        PointReason[] reasons = PointReason.values();

        Random random = new Random();
        int randomIndex = random.nextInt(reasons.length);

        return reasons[randomIndex];
    }
}
