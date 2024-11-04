CREATE TABLE IF NOT EXISTS `user_content_learning_history`
(
    `id`                bigint        NOT NULL AUTO_INCREMENT,
    `user_id`           bigint        NOT NULL,
    `content_id`        bigint        NOT NULL,
    `learning_rate`     decimal(5, 2) NOT NULL DEFAULT 0,
    `learningStartTime` datetime(6)   NOT NULL,
    `updated_at`        datetime(6)            DEFAULT NULL,
    `created_at`        datetime(6)            DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;