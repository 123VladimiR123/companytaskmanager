ALTER TABLE attachments DROP PRIMARY KEY;
ALTER TABLE attachments ADD PRIMARY KEY (post_task_comment_id, path);