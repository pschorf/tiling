CREATE EXTENSION hstore;

CREATE TABLE users(
  user_id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(128) NOT NULL
);

CREATE TABLE tokens (
  token_id SERIAL PRIMARY KEY,
  header hstore,
  claims hstore,
  issued timestamptz,
  authority varchar(20)
);

ALTER TABLE collections ADD COLUMN user_id INT;

ALTER TABLE collections
ADD FOREIGN KEY (user_id) REFERENCES users(user_id);
