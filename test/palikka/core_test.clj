(ns palikka.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [com.stuartsierra.component :as component :refer [system-map using]]
            [palikka.core :refer :all]
            [palikka.test-utils :refer :all]
            [clj-http.client :as client]
            [palikka.example.system :refer [base-system]]))

(defonce system (base-system {:http {:port 9999}, :test "hello world" :db {:foo :bar}}))
(use-fixtures :once (system-fixture #'system))

(defn ctx [] (create-context system))

(deftest system-test
  (is (some? (get-in system [:http-kit :http-kit])))
  (is (= "hello world"
         (:body (client/get "http://localhost:9999")))))

(deftest context-test
  (is (instance? clojure.lang.Atom (:db (ctx))))
  (is (= "<<Context>>" (pr-str (ctx))))
  (is (= "<<Context>>" (pr-str (merge (ctx) {:foo 1})))))

(comment
  (system-up! #'system)
  (meta system)
  (:env system)
  (system-down! #'system)
  (do (system-down! #'system)
      (ns-unmap *ns* 'system)))
