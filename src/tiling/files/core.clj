(ns tiling.files.core
  (:require [clojure.java.io :as io]
            [tiling.db.core :as db]
            [taoensso.timbre :as timbre]
            [tiling.blob.core :refer [binary-store]]
            [ring.util.response :refer [response content-type not-found]]
            [tiling.blob.db :as bdb])
  (:import [java.net URI]
           [java.io ByteArrayInputStream]))

(defn store-file [name mime file size]
  (with-open [stream (io/input-stream file)]
    (let [store (bdb/blob-store)
          uri (binary-store store stream size)
          file (db/add-file<! {:name name
                              :mime mime
                              :location (.toString uri)})]
      (str "/files/" (:file_id file)))))

(defn -get-bytes [location]
  (let [[_ id] (re-find #"/(\d+)$" (.getPath location))
        blob (first (db/get-blob {:id (Integer/parseInt id)}))]
    (:content blob)))

(defn serve-file [id]
  (let [files (db/get-file {:id id})]
    (if (empty? files)
      (not-found (str "File " id " not found"))
      (let [file (first files)
            bytes (-get-bytes (URI. (:location file)))]
        (-> (response (ByteArrayInputStream. bytes))
            (content-type (:mime_type file)))))))
      

