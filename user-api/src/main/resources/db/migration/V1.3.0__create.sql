-- Create mission table
CREATE TABLE IF NOT EXISTS `mission`
(
    `id`             BIGINT                           NOT NULL,
    `one_content`    BOOLEAN                          NOT NULL DEFAULT false,
    `memo`           BOOLEAN                          NOT NULL DEFAULT false,
    `quiz`           BOOLEAN                          NOT NULL DEFAULT false,
    `mission_date`   DATETIME                         NOT NULL,
    `mission_status` enum ('IN_PROGRESS', 'COMPLETE') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Populate mission table for existing users
INSERT INTO `mission` (id, one_content, memo, quiz, mission_date, mission_status)
SELECT u.id, false, false, false, CURRENT_TIME(), 'IN_PROGRESS'
FROM `user` u
WHERE u.id NOT IN (SELECT id FROM `mission`);