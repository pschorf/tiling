(ns tiling.db.core
  (:require [yesql.core :refer [defqueries]]
            [clojure.string :refer [split]]
            [clojure.java.io :refer [file reader]]
            [clojure.data.json :as json]))

(defn db-spec []
  (let [f (file "/etc/tiling/database.json")]
    (if (.exists f)
      (with-open [rdr (reader "/etc/tiling/database.json")]
        (let [conf (json/read rdr)]
          {:subprotocol "postgresql"
           :subname (str "//" (get conf "host") "/" (get conf "database"))
           :user (get conf "user")
           :password (get conf "pass")}))
      {})))

(defqueries "sql/tiling.sql" {:connection (db-spec)})

