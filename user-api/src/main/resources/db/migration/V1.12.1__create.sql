CREATE TABLE IF NOT EXISTS category_recommender
(
    `id`                   BIGINT AUTO_INCREMENT,
    `category_id`          BIGINT       NOT NULL,
    `similar_category_ids` VARCHAR(512) NOT NULL,
    `created_at`         DATETIME(6) DEFAULT NULL,
    `updated_at`         DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;