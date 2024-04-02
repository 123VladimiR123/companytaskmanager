CREATE TABLE posts (
    post_id varchar(255) not null primary key,
    from_user varchar(255) not null,
    message text not null,
    date_published datetime not null
);

CREATE TABLE comments (
    post_or_task_id varchar(255) not null primary key,
    from_user varchar(255) not null,
    attachment_path varchar(255),
    date_published datetime not null
);

CREATE TABLE attachments (
    post_or_task_id varchar(255) not null primary key,
    path varchar(255)
);

CREATE TABLE tasks (
    task_id varchar(255) not null primary key,
    for_department varchar(255) not null,
    from_user varchar(255) not null,
    message text not null,
    date_published datetime not null,
    foreign key (for_department) references departments(department_name)
);