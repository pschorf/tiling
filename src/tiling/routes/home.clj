(ns tiling.routes.home
  (:require [tiling.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [ring.util.http-response :refer [ok]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.response :refer [redirect-after-post]]
            [tiling.db.core :as db]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn collections [id]
  (let [collections (db/get-collections)
        coll (first (filter #(= (:collection_id %) id) collections))
        tiles (db/get-tiles {:collection id})]
    (layout/render "collection.html" {:tiles tiles
                                      :collections collections
                                      :collection coll
                                      :xsrf (anti-forgery-field)})))

(defn add-collection [name]
  (let [new-id (:id (db/add-collection<! {:name name}))]
    (collections new-id)))

(defn add-tile [req]
  (let [params (:params req)
        collection (Integer/parseInt (:collection params))
        url (:url params)]
    (db/add-tile<! {:collection_id collection
                    :url url})
    (redirect-after-post (str "/collection/" collection))))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/collection/:id" [id] (collections (Integer/parseInt id)))
  (POST "/add-collection" req (add-collection (:name (:params req))))
  (POST "/add-tile" req (add-tile req)))

