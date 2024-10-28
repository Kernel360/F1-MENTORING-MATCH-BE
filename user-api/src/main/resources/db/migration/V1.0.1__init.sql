CREATE TABLE IF NOT EXISTS `bookmark`
(
    `id`                   bigint NOT NULL AUTO_INCREMENT,
    `created_at`           datetime(6)  DEFAULT NULL,
    `updated_at`           datetime(6)  DEFAULT NULL,
    `description`          varchar(255) DEFAULT NULL,
    `detail`               varchar(512) DEFAULT NULL,
    `script_index`         bigint NOT NULL,
    `sentence_index`       bigint       DEFAULT NULL,
    `start_time_in_second` double       DEFAULT NULL,
    `user_id`              bigint       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 164
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `category`
(
    `id`   bigint       NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `content`
(
    `hits`             int                              NOT NULL DEFAULT '0',
    `category_id`      bigint                                    DEFAULT NULL,
    `created_at`       datetime(6)                               DEFAULT NULL,
    `id`               bigint                           NOT NULL AUTO_INCREMENT,
    `updated_at`       datetime(6)                               DEFAULT NULL,
    `mongo_content_id` varchar(255)                     NOT NULL,
    `pre_scripts`      varchar(255)                     NOT NULL,
    `thumbnail_url`    varchar(255)                              DEFAULT NULL,
    `title`            varchar(255)                     NOT NULL,
    `url`              varchar(255)                     NOT NULL,
    `content_status`   enum ('ACTIVATED','DEACTIVATED') NOT NULL,
    `content_type`     enum ('LISTENING','READING')     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK8m15gchnf9465da7uk71o1du2` (`mongo_content_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `refresh_token`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT,
    `user_id`       bigint       NOT NULL,
    `refresh_token` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKf95ixxe7pa48ryn1awmh2evt7` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 115
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `scrap`
(
    `content_id` bigint      DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `user_id`    bigint      DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 72
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `user`
(
    `birth`        date                                                  DEFAULT NULL,
    `created_at`   datetime(6)                                           DEFAULT NULL,
    `id`           bigint                                                                                                NOT NULL AUTO_INCREMENT,
    `updated_at`   datetime(6)                                           DEFAULT NULL,
    `email`        varchar(255)                                                                                          NOT NULL,
    `nickname`     varchar(255)                                                                                          NOT NULL,
    `password`     varchar(255)                                          DEFAULT NULL,
    `phone_number` varchar(255)                                          DEFAULT NULL,
    `provider`     varchar(255)                                                                                          NOT NULL,
    `provider_id`  varchar(255)                                                                                          NOT NULL,
    `username`     varchar(255)                                                                                          NOT NULL,
    `gender`       enum ('GENDER_FEMALE','GENDER_MALE','GENDER_UNKNOWN') DEFAULT NULL,
    `role`         enum ('ROLE_ADMIN','ROLE_DEVELOPER','ROLE_USER','ROLE_USER_UNCERTIFIED')                              NOT NULL,
    `user_status`  enum ('USER_STATUS_ACTIVATE','USER_STATUS_CREATED','USER_STATUS_DEACTIVATED','USER_STATUS_SUSPENDED') NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 30
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

