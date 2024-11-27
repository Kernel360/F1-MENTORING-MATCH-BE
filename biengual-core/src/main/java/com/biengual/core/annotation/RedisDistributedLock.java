package com.biengual.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 분산 락을 위한 어노테이션
 *
 * @author 문찬욱
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisDistributedLock {

    String key(); // Lock의 이름 (고유값)
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    long waitTime() default 5L; // Lock 획득을 시도하는 최대 시간 (ms)
    // TODO: -1 이면 unLock 하기전에 무한으로 점유하게 되는데, 갑자기 서버가 죽는 경우에 대한 대비를 해야할 것 같다는 생각이 들고,
    //  아니면 시간을 정하되 핵심 작업이 끝나기 전에 점유 시간이 지나는 경우에 대한 대비를 해야할 것 같습니다.
    long leaseTime() default -1L; // Lock 획득 후, 점유하는 최대 시간 (ms)
}
