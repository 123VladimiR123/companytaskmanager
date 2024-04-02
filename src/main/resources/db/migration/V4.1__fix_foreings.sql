ALTER TABLE user_info DROP foreign key user_info_ibfk_1;
ALTER TABLE user_info ADD foreign key (username) references users(username)
    ON UPDATE CASCADE;

ALTER TABLE comments ADD FOREIGN KEY (from_user) references users(username)
    ON UPDATE CASCADE;

ALTER TABLE departments DROP FOREIGN KEY departments_ibfk_1;
ALTER TABLE departments ADD FOREIGN KEY (main_user) REFERENCES users(username)
    ON UPDATE CASCADE;
ALTER TABLE departments ADD FOREIGN KEY (department_higher) REFERENCES departments(department_name)
    ON UPDATE CASCADE;

ALTER TABLE posts ADD FOREIGN KEY (from_user) references users(username)
    ON UPDATE CASCADE;

ALTER TABLE tasks DROP FOREIGN KEY tasks_ibfk_1;
ALTER TABLE tasks ADD FOREIGN KEY (for_department) REFERENCES departments(department_name)
    ON UPDATE CASCADE;
ALTER TABLE tasks ADD FOREIGN KEY (from_user) REFERENCES users(username)
    ON UPDATE CASCADE;