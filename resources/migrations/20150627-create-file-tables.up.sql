CREATE TABLE files (
  file_id SERIAL PRIMARY KEY,
  file_name TEXT NOT NULL,
  mime_type VARCHAR(50),
  location TEXT NOT NULL
);

CREATE TABLE blobs (
  blob_id SERIAL PRIMARY KEY,
  content BYTEA NOT NULL
);
