(ns tiling.routes.users
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [redirect-after-post]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [tiling.auth.users :as users]
            [tiling.auth.token :as token]
            [clj-jwt.core :as jwt]
            [tiling.layout :as layout]))

(defn -create-user [email password]
  (let [user (users/add-user email password)
        t (token/grant-token (:user_id user) :password)
        cookie (token/get-cookie t)]
  (assoc-in (redirect-after-post "/collection") [:cookies "token"] cookie)))

(defroutes user-routes
  (context "/user" []
    (GET "/create" [] (layout/render "create-account.html"
                                     {:xsrf (anti-forgery-field)}))
    (POST "/create" {{email :email
                      password :password} :params}
          (-create-user email password))))
