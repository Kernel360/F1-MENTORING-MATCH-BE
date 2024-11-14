package com.biengual.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisDistributedLock {

    String key(); // Lock의 이름 (고유값)
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    long waitTime() default 5L; // Lock 획득을 시도하는 최대 시간 (ms)
    long leaseTime() default -1; // Lock 획득 후, 점유하는 최대 시간 (ms)
    int maxRetries() default 6; // Lock 획득 실패 시 최대 재시도 횟수
}
