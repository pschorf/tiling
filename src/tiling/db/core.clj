(ns tiling.db.core
  (:require [yesql.core :refer [defqueries]]))

(def db-spec {:subprotocol "postgresql"
              :subname "//localhost/tiling"
              :user "towner"
              :password "tiling"})

(defqueries "sql/tiling.sql" {:connection db-spec})

