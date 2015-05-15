(ns palikka.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [com.stuartsierra.component :as component :refer [system-map using]]
            [palikka.components.env :as env]
            [palikka.components.handler :as handler]
            [palikka.components.http-kit :as http-kit]
            [palikka.context :refer [create-context]]
            [palikka.test-utils :refer :all]
            [clj-http.client :as client]
            [palikka.example.system :refer [base-system]]))

(defonce system (base-system {:http {:port 9999}, :test "hello world"}))
(use-fixtures :once (system-fixture #'system))

(deftest system-test
  (is (some? (get-in system [:http-kit :http-kit])))
  (is (= "hello world"
         (:body (client/get "http://localhost:9999")))))

(deftest context-test
  (let [ctx (create-context system)]
    (is (map? (:config ctx)))
    (is (instance? clojure.lang.Atom (:db ctx)))))

(comment
  (system-up! #'system)
  (meta system)
  (:env system)
  (system-down! #'system))
