CREATE TABLE collections (
  collection_id SERIAL PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE tiles (
  tile_id SERIAL PRIMARY KEY,
  title VARCHAR(255),
  url VARCHAR(255),
  collection_id INT REFERENCES collections(collection_id)
);
