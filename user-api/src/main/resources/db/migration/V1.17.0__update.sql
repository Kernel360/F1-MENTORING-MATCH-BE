UPDATE user
SET nickname = CONCAT(
        CASE
            WHEN RAND() < 0.2 THEN '용감한 강아지 '
            WHEN RAND() < 0.4 THEN '빠른 고양이 '
            WHEN RAND() < 0.6 THEN '행복한 도마뱀 '
            WHEN RAND() < 0.8 THEN '친절한 다람쥐 '
            ELSE '빛나는 여우 '
            END,
        LPAD(FLOOR(RAND() * 10000), 4, '0')
               )
WHERE CHAR_LENGTH(nickname) < 4 || CHAR_LENGTH(nickname) > 12;

ALTER TABLE `user`
    MODIFY COLUMN nickname VARCHAR(12) NOT NULL;