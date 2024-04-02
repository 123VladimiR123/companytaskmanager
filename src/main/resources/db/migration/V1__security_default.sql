CREATE TABLE users(
    username varchar(255) not null primary key,
    password varchar(255) not null,
    enabled boolean not null
);

CREATE TABLE authorities(
    username varchar(255) not null,
    authority varchar(255) not null,
    foreign key (username) references users(username)
);

CREATE unique index ix_auth_username on authorities (username, authority);