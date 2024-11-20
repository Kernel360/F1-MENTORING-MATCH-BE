CREATE TABLE IF NOT EXISTS `bookmark_recommender`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `content_id`    BIGINT       NOT NULL,
    `sentence_id`   BIGINT       NOT NULL,
    `en_detail`     VARCHAR(255) NOT NULL,
    `ko_detail`     VARCHAR(255) NOT NULL,
    `start_of_week` DATETIME(6)  NOT NULL,
    `end_of_week`   DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;