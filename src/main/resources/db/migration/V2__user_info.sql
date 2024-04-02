CREATE TABLE user_info (
    username varchar(255) not null,
    phone_number varchar(255) unique not null,
    department varchar(255) not null,
    role varchar(255) not null,
    birth_date date not null,
    foreign key (username) references users(username)
);

CREATE TABLE departments (
    department_name varchar(255) not null primary key,
    main_user varchar(255) not null,
    department_higher varchar(255)
);