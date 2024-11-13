CREATE TABLE IF NOT EXISTS `category_learning_history`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT      NOT NULL,
    `category_id` BIGINT      NOT NULL,
    `learning_time` DATETIME(6) NOT NULL,
    `created_at` DATETIME(6) DEFAULT null,
    `updated_at` DATETIME(6) DEFAULT null,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;