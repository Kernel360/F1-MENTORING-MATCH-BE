CREATE TABLE IF NOT EXISTS `user_category`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `created_at`  datetime(6) DEFAULT NULL,
    `updated_at`  datetime(6) DEFAULT NULL,
    `user_id`     bigint NOT NULL,
    `category_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;