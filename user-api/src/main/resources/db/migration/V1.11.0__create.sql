CREATE TABLE IF NOT EXISTS `question_history`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT       NOT NULL,
    `question_id` VARCHAR(255) NOT NULL,
    `first_try`   BOOLEAN      NOT NULL,
    `final_try`   BOOLEAN      NOT NULL,
    `count`       BIGINT       NOT NULL,
    `created_at`  DATETIME(6)  DEFAULT NULL,
    `updated_at`  DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;