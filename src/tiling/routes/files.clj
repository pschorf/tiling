(ns tiling.routes.files
  (:require [compojure.core :refer [defroutes GET POST]]
            [ring.middleware.multipart-params :as mp]
            [ring.util.response :refer [redirect-after-post]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [tiling.files.core :refer [store-file serve-file]]
            [tiling.layout :as layout]))

(defn handle-upload [file]
  (redirect-after-post (store-file (:filename file)
                                   (:content-type file)
                                   (:tempfile file)
                                   (:size file))))

(defroutes file-routes
  (mp/wrap-multipart-params
   (POST "/files" {params :multipart-params} (handle-upload (get params "file"))))
  (GET "/files/:id" [id] (serve-file (Integer/parseInt id)))
  (GET "/upload" [] (layout/render "upload.html" {:xsrf (anti-forgery-field)})))

