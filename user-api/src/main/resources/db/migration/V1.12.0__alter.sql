-- Step 1: Add contentId column to category_learning_history table
ALTER TABLE category_learning_history
    ADD COLUMN content_id BIGINT;
