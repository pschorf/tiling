(ns tiling.auth.users
  (:require [tiling.db.core :as db])
  (:import [org.mindrot.jbcrypt BCrypt]))

(defn -hash-password [password]
  (BCrypt/hashpw password (BCrypt/gensalt)))

(defn add-user [email password]
  (let [hashed-password (-hash-password password)]
    (db/add-user<! {:email email :password hashed-password})))

(defn get-user [email password]
  (when-let [users (seq (db/get-user-email {:email email}))]
    (let [user (first users)]
      (if (BCrypt/checkpw password (:password user))
        user
        nil))))
