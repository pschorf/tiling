(ns tiling.auth.token
  (:require [clj-jwt.core :as jwt]
            [clj-jwt.key :refer [private-key]]
            [clj-time.core :refer [now plus days]]
            [tiling.db.core :as db]
            [clj-time.format :as fmt]
            [clj-jwt.intdate :refer :all]))

(def prv-key (private-key "/etc/tiling/jwt-private.pem" ""))

(defn grant-token [user-id authority]
  (let [issued (now)
        exp (plus issued (days 7))
        issuer (str "tiling-" (name authority))
        subject (str "user-" user-id)
        claim {:iss issuer :sub subject :exp exp :iat issued}
        token (-> claim jwt/jwt (jwt/sign :RS512 prv-key))]
    (db/grant-token<! {:header (:header token)
                       :claims (:claims token)
                       :issued (fmt/unparse (:date-time fmt/formatters) issued)
                       :authority (name authority)})
    token))

(defn get-cookie [token]
  (let [exp (-> token :claims :exp intdate->joda-time)]
    {:value (jwt/to-str token)
     :http-only true
     :path "/"
     :expires exp}))
