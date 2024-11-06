-- Alter the point_history table to add 'QUIZ_CORRECT_ANSWER' to the reason ENUM
ALTER TABLE `point_history`
    MODIFY COLUMN `reason` ENUM(
        'FIRST_SIGN_UP',
        'DAILY_QUIZ',
        'DAILY_MISSION',
        'DAILY_CONTENT',
        'VIEW_RECENT_CONTENT',
        'VIEW_QUIZ_HINT',
        'FIRST_DAILY_LOG_IN',
        'QUIZ_CORRECT_ANSWER'  -- New enum value
        ) NOT NULL;