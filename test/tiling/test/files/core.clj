(ns tiling.test.files.core
  (:require [clojure.test :refer :all]
            [tiling.db.core :as db]
            [tiling.files.core :as files]
            [ring.util.response :refer [response? get-header]]))

(deftest serve-file
  (testing "valid file"
    (let [file-id 12
          blob-id 13
          content "testcontent"]
      (with-redefs [db/get-file (fn [m] (do
                                          (is (= file-id (:id m)))
                                          [{:location (str "blob://fake/db/blobs/" blob-id)
                                            :mime_type "fake/file"}]))
                    db/get-blob (fn [m] (do
                                          (is (= blob-id (:id m)))
                                          [{:content (.getBytes content)}]))]
        (let [resp (files/serve-file 12)]
          (is (response? resp))
          (is (= "fake/file" (get-header resp "Content-Type")))
          (is (= content (slurp (:body resp))))))))
  (testing "missing file"
    (with-redefs [db/get-file (fn [_] [])]
      (let [resp (files/serve-file 1)]
        (is (response? resp))
        (= 404 (:status resp))))))
      
