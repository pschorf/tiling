(ns tiling.db.core
  (:require [yesql.core :refer [defqueries]]
            [clojure.string :refer [split]])
  (:import java.net.URI))

(def db-spec (let [uri (URI. (System/getenv "DATABASE_URL"))
                   user (first (split (.getUserInfo uri) #":"))
                   pass (second (split (.getUserInfo uri) #":"))
                   host (.getHost uri)
                   path (.getPath uri)]
               {:subprotocol "postgresql"
                :subname (str "//" host path)
                :user user
                :password pass}))

;; (def db-spec {:subprotocol "postgresql"
;;               :subname "//localhost/tiling"
;;               :user "towner"
;;               :password "tiling"})

(defqueries "sql/tiling.sql" {:connection db-spec})

