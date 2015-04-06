(ns palikka.core-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [com.stuartsierra.component :as component :refer [system-map using]]
            [palikka.components.env :as env]
            [palikka.components.handler :as handler]
            [palikka.components.http-kit :as http-kit]
            [palikka.components.context :as context]
            [palikka.test-utils :refer :all]
            [clj-http.client :as client]))

(def app (fn [req]
           (println "req" req)
           {:status 200 :body (-> req :palikka/context :config :test)}))

(defn base-system []
  (system-map
    :env (env/create "palikka" {:http {:port 9999} :test "hello world"})
    :handler (using (handler/create 'palikka.core-test/app) [:env])
    :http-kit (using (http-kit/create) [:env :handler])))

(def system (base-system))

(deftest system-test
  (is (some? (get-in system [:http-kit :http-kit])))
  (is (= "hello world"
         (:body (client/get "http://localhost:9999")))))

(use-fixtures :once (system-fixture #'system))

(comment
  (alter-var-root #'system component/start)
  (:env system)
  (alter-var-root #'system component/stop))
