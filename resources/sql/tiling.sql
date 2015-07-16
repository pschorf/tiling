--name: get-tiles
-- Gets tiles for a collection
SELECT tiles.*
FROM tiles
WHERE collection_id = :collection

--name: get-collection
-- Gets a collection by id
SELECT *
FROM collections
WHERE collection_id = :collection

--name: get-collections
-- Gets collections
SELECT * FROM collections

--name: add-collection<!
-- Add a new collection to the database
INSERT INTO collections (name)
VALUES (:name)

--name: add-tile<!
-- Add a new tile
INSERT INTO tiles (url, collection_id)
VALUES (:url, :collection_id)

--name: add-file<!
-- Add a new file
INSERT INTO files (file_name, mime_type, location)
VALUES (:name, :mime, :location)

--name: get-file
-- Get a file
SELECT * FROM files
WHERE file_id = :id

--name: get-blob
-- Get a blob
SELECT * FROM blobs
WHERE blob_id = :id

--name: add-user<!
-- Add a user
INSERT INTO users (email, password)
VALUES (:email, :password)

--name: get-user-email
-- Load a user by email
SELECT * FROM users
WHERE email = :email

--name: grant-token<!
-- Add a new token grant
INSERT INTO tokens (header, claims, issued, authority)
VALUES (:header, :claims, :issued::timestamptz, :authority)
