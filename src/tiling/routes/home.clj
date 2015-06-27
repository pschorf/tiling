(ns tiling.routes.home
  (:require [tiling.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [taoensso.timbre :as timbre]
            [ring.util.response :refer [redirect-after-post]]
            [tiling.db.core :as db]
            [ring.middleware.multipart-params :as mp]
            [tiling.files.core :refer [store-file serve-file]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn collections
  ([]
   (let [cols (db/get-collections)
         id (if (empty? cols) -1 (:collection_id (first cols)))]
     (collections id)))
  ([id]
   (let [collections (db/get-collections)
         coll (first (filter #(= (:collection_id %) id) collections))
         tiles (db/get-tiles {:collection id})]
     (layout/render "collection.html" {:tiles tiles
                                       :collections collections
                                       :collection coll
                                       :xsrf (anti-forgery-field)}))))

(defn add-collection [name]
  (let [new-id (:collection_id (db/add-collection<! {:name name}))]
    (redirect-after-post (str "/collection/" new-id))))

(defn add-tile [req]
  (let [params (:params req)
        collection (Integer/parseInt (:collection params))
        url (:url params)]
    (db/add-tile<! {:collection_id collection
                    :url url})
    (redirect-after-post (str "/collection/" collection))))

(defn handle-upload [file]
  (redirect-after-post (store-file (:filename file)
                                   (:content-type file)
                                   (:tempfile file)
                                   (:size file))))
(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/upload" [] (layout/render "upload.html" {:xsrf (anti-forgery-field)}))
  (mp/wrap-multipart-params
   (POST "/file" {params :multipart-params} (handle-upload (get params "file"))))
  (GET "/files/:id" [id] (serve-file (Integer/parseInt id)))
  (GET "/collection/:id" [id] (collections (Integer/parseInt id)))
  (GET "/collection" [] (collections))
  (POST "/add-collection" req (add-collection (:name (:params req))))
  (POST "/add-tile" req (add-tile req)))

