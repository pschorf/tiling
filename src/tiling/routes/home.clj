(ns tiling.routes.home
  (:require [tiling.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [tiling.db.core :as db]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defn collections [id]
  (layout/render "collection.html" {:tiles (db/get-tiles {:collection id})
                                    :collection {:name "Restaurants"}}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/collection/:id" [id] (collections (Integer/parseInt id))))

