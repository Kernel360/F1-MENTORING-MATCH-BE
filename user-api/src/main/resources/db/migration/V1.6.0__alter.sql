-- V1__Add_last_login_time_to_users.sql
ALTER TABLE `user`
    ADD COLUMN last_login_time TIMESTAMP;

UPDATE `user`
SET last_login_time = CURRENT_TIMESTAMP
WHERE last_login_time IS NULL;

ALTER TABLE `user`
    MODIFY COLUMN last_login_time TIMESTAMP NOT NULL;