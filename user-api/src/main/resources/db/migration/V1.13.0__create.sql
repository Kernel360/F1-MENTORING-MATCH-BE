CREATE TABLE IF NOT EXISTS `content_level_feedback_history`
(
    `id`            BIGINT                         NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT                         NOT NULL,
    `content_id`    BIGINT                         NOT NULL,
    `content_level` ENUM ('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    `created_at`    DATETIME(6) DEFAULT NULL,
    `updated_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;