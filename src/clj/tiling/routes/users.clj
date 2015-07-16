(ns tiling.routes.users
  (:require [compojure.core :refer [defroutes GET POST context]]
            [ring.util.response :refer [redirect-after-post]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [tiling.auth.users :as users]
            [tiling.layout :as layout]))

(defn -create-user [email password]
  (users/add-user email password)
  (redirect-after-post "/collection"))

(defroutes user-routes
  (context "/user" []
    (GET "/create" [] (layout/render "create-account.html"
                                     {:xsrf (anti-forgery-field)}))
    (POST "/create" {{email :email
                      password :password} :params}
          (-create-user email password))))
