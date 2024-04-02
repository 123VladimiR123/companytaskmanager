ALTER TABLE tasks DROP FOREIGN KEY tasks_ibfk_1;
ALTER TABLE tasks ADD FOREIGN KEY (for_user) references users(username)
ON UPDATE CASCADE;