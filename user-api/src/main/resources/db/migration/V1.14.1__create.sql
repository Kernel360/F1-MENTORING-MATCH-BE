CREATE TABLE IF NOT EXISTS `content_level_feedback_data_mart`
(
    `id`                   BIGINT      NOT NULL,
    `level_low_count`      BIGINT      NOT NULL,
    `level_medium_count`   BIGINT      NOT NULL,
    `level_high_count`     BIGINT      NOT NULL,
    `feedback_total_count` BIGINT      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;