CREATE TABLE IF NOT EXISTS `user_category_progress`
(
    `user_id`               BIGINT NOT NULL,
    `category_id`           BIGINT NOT NULL,
    `total_learn_count`     BIGINT NOT NULL DEFAULT 0,
    `completed_learn_count` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`, `category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;