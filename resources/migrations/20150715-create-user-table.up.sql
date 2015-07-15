CREATE TABLE users(
  user_id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(50) NOT NULL
);

CREATE TABLE tokens (
  token_id SERIAL PRIMARY KEY,
  header json,
  claims json,
  issued timestamp,
  authority varchar(20)
);

ALTER TABLE collections ADD COLUMN user_id INT;

ALTER TABLE collections
ADD FOREIGN KEY (user_id) REFERENCES users(user_id);
