package com.biengual.userapi.common.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ExecutorServiceUtil {

    // 단일 입력 파라미터에 대한 ExecutorService
    public static void createExecutorService(int threadCount, Runnable task) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {

            int finalI = i;
            executorService.submit(() -> {
                try {
                    task.run();
                } finally {
                    System.out.println("순서: " + finalI);
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }

    // 여러 입력 파라미터에 대한 ExecutorService
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
