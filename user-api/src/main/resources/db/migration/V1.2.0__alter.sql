SET @dbname = 'echall_local_db';
SET @tablename = 'content';
SET @columnname = 'num_of_quiz';

SET @exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = @dbname
      AND TABLE_NAME = @tablename
      AND COLUMN_NAME = @columnname
);

SET @query = IF(@exists = 0,
                CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' bigint NOT NULL DEFAULT 0'),
                'SELECT "Column already exists"'
             );

PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;