CREATE TABLE IF NOT EXISTS `learning_history`
(
    `id`            BIGINT        NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT        NOT NULL,
    `content_id`    BIGINT        NOT NULL,
    `learning_rate` DECIMAL(5, 2) NOT NULL,
    `learning_time` DATETIME(6)   NOT NULL,
    `created_at`    DATETIME(6) DEFAULT NULL,
    `updated_at`    DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;