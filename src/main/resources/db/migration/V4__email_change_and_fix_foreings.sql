CREATE TABLE change_email (
    old_username varchar(255),
    new_username varchar(255),
    expired_time timestamp,
    verification_key varchar(255),
    foreign key (old_username) references users(username)
    ON UPDATE CASCADE
);

ALTER TABLE user_info DROP foreign key user_info_ibfk_1;
ALTER TABLE user_info ADD foreign key (username) references users(username)
    ON UPDATE CASCADE;

ALTER TABLE authorities DROP FOREIGN KEY authorities_ibfk_1;
ALTER TABLE authorities ADD FOREIGN KEY (username) REFERENCES users(username)
    ON UPDATE CASCADE;