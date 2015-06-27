(ns tiling.blob.db
  (:require [tiling.db.core :as db]
            [clojure.java.jdbc :as jdbc]
            [tiling.blob.core :refer [BlobStore]])
  (:import [java.net URI]
           [java.sql Connection]
           [java.io ByteArrayInputStream]
           [tiling.db BinaryObjectStore]))

(defrecord SQLBlobStore [host db]
  BlobStore
  (binary-store [this stream size]
    (with-open [conn (jdbc/get-connection (db/db-spec))]
      (let [id (BinaryObjectStore/store conn stream size)]
        (URI. (str "blob://" host "/" db "/blobs/" id))))))

(defn blob-store []
  (let [spec (db/db-spec)
        [_ host db] (re-find #"//([^/]+)/(.*)" (:subname spec))]
    (SQLBlobStore. host db)))    
