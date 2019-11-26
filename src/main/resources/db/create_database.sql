DROP DATABASE IF EXISTS board;
DROP USER IF EXISTS board_user;

USE mysql;
CREATE DATABASE board DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
CREATE USER 'board_user'@'%' IDENTIFIED BY 'pass';
GRANT ALL ON board.* TO 'board_user'@'%';
FLUSH PRIVILEGES;
