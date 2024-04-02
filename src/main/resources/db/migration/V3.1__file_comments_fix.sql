ALTER TABLE attachments ADD COLUMN (
    attachment_name varchar(255) not null
    );

ALTER TABLE comments DROP COLUMN
    attachment_path;

ALTER TABLE comments ADD COLUMN (
    comment_id varchar(255) not null
    );

ALTER TABLE attachments RENAME COLUMN post_or_task_id TO post_task_comment_id;