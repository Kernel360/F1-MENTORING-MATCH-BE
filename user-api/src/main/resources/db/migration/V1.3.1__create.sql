-- Create mission_history table
CREATE TABLE IF NOT EXISTS `mission_history`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT      NOT NULL,
    `count`      INT         NOT NULL,
    `one_count`  BOOLEAN     NOT NULL DEFAULT false,
    `bookmark`   BOOLEAN     NOT NULL DEFAULT false,
    `quiz`       BOOLEAN     NOT NULL DEFAULT false,
    `created_at` DATETIME(6) DEFAULT NULL,
    `updated_at` DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

