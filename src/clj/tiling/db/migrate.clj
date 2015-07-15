(ns tiling.db.migrate
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [tiling.db.core :refer [db-spec]])
  (:gen-class))

(defn config []
  {:database (jdbc/sql-database (db-spec))
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (config)))

(defn rollback []
  (repl/rollback (config)))

(defn -main []
  (migrate))
