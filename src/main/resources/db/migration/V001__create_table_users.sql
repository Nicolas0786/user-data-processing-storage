CREATE TABLE users(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    email varchar(80) NOT NULL,
    source varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
