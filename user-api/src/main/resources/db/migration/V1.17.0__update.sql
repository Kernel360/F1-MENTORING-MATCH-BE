UPDATE user
SET nickname = LEFT(nickname, 12)
WHERE CHAR_LENGTH(nickname) > 12;

ALTER TABLE user
    MODIFY COLUMN nickname VARCHAR(12) NOT NULL;