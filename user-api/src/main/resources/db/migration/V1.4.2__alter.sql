-- V1__Add_last_login_time_and_current_point_to_users.sql

-- 1. 컬럼 추가 (기본값 없이)
ALTER TABLE `user`
    ADD COLUMN last_login_time DATETIME(6),
    ADD COLUMN current_point   BIGINT      NOT NULL DEFAULT 100;

-- 2. 기존 레코드에 현재 타임스탬프 할당
UPDATE `user`
SET last_login_time = CURRENT_TIMESTAMP
WHERE last_login_time IS NULL;

ALTER TABLE `user`
    MODIFY COLUMN last_login_time TIMESTAMP NOT NULL;