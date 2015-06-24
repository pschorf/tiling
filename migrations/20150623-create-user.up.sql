CREATE TABLE users(
  user_id SERIAL PRIMARY KEY,
  email VARCHAR(255),
  password CHAR(100),
  anonymous BOOLEAN NOT NULL
);
