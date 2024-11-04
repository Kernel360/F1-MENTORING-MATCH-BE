-- Create point table
CREATE TABLE IF NOT EXISTS `point`
(
    `id`            BIGINT NOT NULL AUTO_INCREMENT,
    `current_point` BIGINT NOT NULL DEFAULT 0,
    `created_at`    DATETIME(6)     DEFAULT NULL,
    `updated_at`    DATETIME(6)     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Create point_history table
CREATE TABLE IF NOT EXISTS `point_history`
(
    `id`            BIGINT                                                                                                                                NOT NULL AUTO_INCREMENT,
    `user_id`       BIGINT                                                                                                                                NOT NULL,
    `point_change`  BIGINT                                                                                                                                NOT NULL,
    `point_balance` BIGINT                                                                                                                                NOT NULL,
    `reason`        ENUM ('FIRST_SIGN_UP', 'DAILY_QUIZ', 'DAILY_MISSION', 'DAILY_CONTENT', 'VIEW_RECENT_CONTENT', 'VIEW_QUIZ_HINT', 'FIRST_DAILY_LOG_IN') NOT NULL,
    `created_at`    DATETIME(6)                                                                                                                                    DEFAULT NULL,
    `updated_at`    DATETIME(6)                                                                                                                                    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Create point_data_mart table
CREATE TABLE IF NOT EXISTS `point_data_mart`
(
    `id`                     BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`                BIGINT NOT NULL,
    `total_points_earned`    BIGINT NOT NULL DEFAULT 0,
    `total_points_spent`     BIGINT NOT NULL DEFAULT 0,
    `last_processed_balance` BIGINT NOT NULL DEFAULT 0,
    `created_at`             DATETIME(6)     DEFAULT NULL,
    `updated_at`             DATETIME(6)     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Populate point table for existing users
INSERT INTO `point` (id, current_point, created_at, updated_at)
SELECT u.id, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM `user` u
         LEFT JOIN `point` p ON u.id = p.id
WHERE p.id IS NULL;

-- Populate point_history table for existing users (initial grant of points for sign up)
INSERT INTO `point_history` (user_id, point_change, point_balance, reason, created_at, updated_at)
SELECT u.id, 100, 100, 'FIRST_SIGN_UP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM `user` u
         LEFT JOIN `point_history` ph ON u.id = ph.user_id AND ph.reason = 'FIRST_SIGN_UP'
WHERE ph.id IS NULL;

-- Populate point_data_mart table for existing users
INSERT INTO `point_data_mart` (user_id, total_points_earned, total_points_spent, last_processed_balance,
                               created_at, updated_at)
SELECT u.id, 100, 0, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM `user` u
         LEFT JOIN `point_data_mart` pdm ON u.id = pdm.user_id
WHERE pdm.id IS NULL;