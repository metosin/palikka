(ns palikka.components.database-test
  (:require [com.stuartsierra.component :as component]
            [palikka.components.database :as db]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer [deftest is]]))

(deftest basic-connection-test
  (let [db (component/start (db/create {:adapter "h2"
                                        :url     "jdbc:h2:~/test"}))]
    (is (= [{:x "lol"}] (jdbc/query (:db db) ["SELECT 'lol' as x"])))
    (component/stop db)))

(deftest jdbc-url-test
  (let [db (component/start (db/create {:jdbc-url "jdbc:h2:~/test"}))]
    (is (= [{:x "lol"}] (jdbc/query (:db db) ["SELECT 'lol' as x"])))
    (component/stop db)))
