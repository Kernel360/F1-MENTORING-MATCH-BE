CREATE TABLE IF NOT EXISTS `aggregation_metadata`
(
    `id`                 bigint                                               NOT NULL AUTO_INCREMENT,
    `table_name`         varchar(100)                                         NOT NULL,
    `aggregate_end_time` datetime(6)                                          NOT NULL,
    `interval_type`      enum ('YEARLY', 'MONTHLY', 'DAILY', 'HOURLY', 'ALL') NOT NULL,
    `interval_number`    tinyint                                              NOT NULL,
    `created_at`         datetime(6) DEFAULT NULL,
    `updated_at`         datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKqlsjiwn68bedqaaomcee26hznvxki4` (`table_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;