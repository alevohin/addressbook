CREATE TABLE person (
  id           BIGINT AUTO_INCREMENT,
  name         VARCHAR(50) NOT NULL,
  phone_number VARCHAR(50),
  email        VARCHAR(255),
  username     VARCHAR(20),
  PRIMARY KEY (id)
);

CREATE TABLE user (
  id       BIGINT AUTO_INCREMENT,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(64) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (username)
);