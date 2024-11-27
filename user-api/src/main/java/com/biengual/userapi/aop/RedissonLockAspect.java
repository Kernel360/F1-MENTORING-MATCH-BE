package com.biengual.userapi.aop;

import com.biengual.core.annotation.RedisDistributedLock;
import com.biengual.core.util.AopForTransaction;
import com.biengual.core.util.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Pointcut("@annotation(com.biengual.core.annotation.RedisDistributedLock)")
    private void redissonLock() {}

    @Around("redissonLock()")
    public Object redisDistributedLockAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisDistributedLock distributedLock = method.getAnnotation(RedisDistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + method.getDeclaringClass().getName() + ":" + method.getName() + ":" +
            CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

        RLock lock = redissonClient.getLock(key);

        try {
            boolean lockable =
                lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!lockable) {
                log.error("Redisson Rock 획득 실패: {}", key);
                return false;
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
