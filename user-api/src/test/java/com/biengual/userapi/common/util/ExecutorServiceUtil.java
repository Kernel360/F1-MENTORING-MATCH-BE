package com.biengual.userapi.common.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 테스트에서 사용하는 ExecutorService를 위한 Util 클래스
 *
 * @author 문찬욱
 */
public class ExecutorServiceUtil {

    // 동일한 입력에 대한 ExecutorService
    public static void createExecutorService(int threadCount, Runnable task) throws InterruptedException {
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

    // 다양한 입력에 대한 ExecutorService
    public static <T> void createExecutorService(T[] objects, Consumer<T> task) throws InterruptedException {
        int threadCount = objects.length;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (T object : objects) {
            executorService.submit(() -> {
                try {
                    task.accept(object);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }
}
