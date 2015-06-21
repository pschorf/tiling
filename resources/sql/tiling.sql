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
