package com.biengual.userapi.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.biengual.core.domain.entity.pointdatamart.PointDataMart;
import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.userapi.pointdatamart.domain.PointDataMartRepository;
import com.biengual.userapi.pointhistory.domain.PointHistoryRepository;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class PointDataMartJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final PointHistoryRepository pointHistoryRepository;
    private final PointDataMartRepository pointDataMartRepository;

    /**
     * Job 생성 : pointDataMartJob
     * Step 포함 : pointDataMartStep
     */
    @Bean
    public Job pointDataMartJob(Step pointDataMartStep) {
        return new JobBuilder("pointDataMartJob", jobRepository)
            .start(pointDataMartStep)
            .build();
    }

    /**
     * chunk 기반 Step 처리
     * 한 번에 1000개의 아이템 처리
     */
    @Bean
    public Step pointDataMartStep(
        RepositoryItemReader<PointHistoryEntity> pointHistoryReader,
        ItemProcessor<PointHistoryEntity, PointDataMart> pointDataMartProcessor,
        JpaItemWriter<PointDataMart> pointDataMartWriter
    ) {
        return new StepBuilder("pointDataMartStep", jobRepository)
            .<PointHistoryEntity, PointDataMart>chunk(1000, transactionManager)
            .reader(pointHistoryReader)
            .processor(pointDataMartProcessor)
            .writer(pointDataMartWriter)
            .build();
    }

    /**
     * pointHistoryRepository.findByCreatedAtAfter 를 사용하여 PointHistoryEntity 조회
     * 처리 되지 않은 포인트 내역(지정된 createdAt 이후인 항목) 조회
     */
    @Bean
    @StepScope
    public RepositoryItemReader<PointHistoryEntity> pointHistoryReader(
        @Value("#{jobParameters['createdAt']}") String createdAtParam
    ) {
        LocalDateTime createdAt = LocalDateTime.parse(createdAtParam);
        return new RepositoryItemReaderBuilder<PointHistoryEntity>()
            .name("pointHistoryReader")
            .repository(pointHistoryRepository)
            .methodName("findByCreatedAtAfter")
            .pageSize(100)
            .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
            .arguments(Collections.singletonList(createdAt))
            .build();
    }

    /**
     * PointHistoryEntity 에 대한 PointDataMart 조회 및 업데이트(00시 이후 항목)
     */
    @Bean
    public ItemProcessor<PointHistoryEntity, PointDataMart> pointDataMartProcessor() {
        return history -> {
            PointDataMart dataMart = pointDataMartRepository.findByUserId(history.getUser().getId())
                .orElseGet(() -> PointDataMart.createPointDataMart(history.getUser().getId()));

            // 어제 00 시 부터 오늘 00시 사이의 24시간 history에 데해서만 업데이트
            if (
                history.getCreatedAt().isAfter(
                    LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0))
                ) && (
                    history.getCreatedAt().isBefore(
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0))
                    )
                )
            ) {
                dataMart.updateByPointHistory(history);
            }

            return dataMart;
        };
    }

    /**
     * JpaItemWriter 를 이용해 PointDataMart 저장
     */
    @Bean
    public JpaItemWriter<PointDataMart> pointDataMartWriter() {
        return new JpaItemWriterBuilder<PointDataMart>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    /**
     * JpaItemWriter 를 이용해 PointHistoryEntity 저장 (필요 시 사용)
     */
    @Bean
    public JpaItemWriter<PointHistoryEntity> pointHistoryWriter() {
        return new JpaItemWriterBuilder<PointHistoryEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }
}