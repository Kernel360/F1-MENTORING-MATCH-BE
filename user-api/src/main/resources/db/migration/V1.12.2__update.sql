-- Step 2: Update the new content_id column with data from learning_history table
UPDATE category_learning_history clh
    JOIN learning_history lh ON clh.user_id = lh.user_id AND clh.learning_time = lh.learning_time
SET clh.content_id = lh.content_id
WHERE clh.content_id IS NULL;

-- Step 3: Make the content_id column NOT NULL after populating it
ALTER TABLE category_learning_history
    MODIFY COLUMN content_id BIGINT NOT NULL;