(ns tiling.db.core
  (:require [yesql.core :refer [defqueries]]
            [clojure.string :refer [split]]
            [environ.core :refer [env]])
  (:import java.net.URI))

(def db-spec (let [uri (URI. (env :database-url))
                   user (first (split (.getUserInfo uri) #":"))
                   pass (second (split (.getUserInfo uri) #":"))
                   host (.getHost uri)
                   path (.getPath uri)]
               {:subprotocol "postgresql"
                :subname (str "//" host path)
                :user user
                :password pass}))

(defqueries "sql/tiling.sql" {:connection db-spec})

