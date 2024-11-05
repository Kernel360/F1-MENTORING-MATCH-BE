package com.biengual.userapi.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job pointDataMartJob;

    @Scheduled(cron = "00 00 01 * * ?") // 매일 새벽 1시에 실행
    public void runPointDataMartJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("createdAt", LocalDateTime.now().minusDays(5).toString())
            .addLong("time", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(pointDataMartJob, jobParameters);
    }
}