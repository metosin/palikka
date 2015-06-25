(ns palikka.example.db
  (:require [com.stuartsierra.component :as component]
            [palikka.core :refer [injections]]))

(defrecord Db [db]
  component/Lifecycle
  (start [this]
    (assoc this :db (atom {})))
  (stop [this]
    (assoc this :db nil)))

(defn create
  []
  (map->Db {}))
