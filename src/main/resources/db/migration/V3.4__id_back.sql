ALTER TABLE comments RENAME COLUMN id TO post_or_task_id;
ALTER TABLE tasks RENAME COLUMN id TO task_id;
ALTER TABLE posts RENAME COLUMN id TO post_id;