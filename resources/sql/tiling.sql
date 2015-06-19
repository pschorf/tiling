--name: get-tiles
-- Gets tiles for a collection
SELECT tiles.*
FROM tiles
WHERE collection_id = :collection
