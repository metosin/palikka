(ns palikka.example.db
  (:require [com.stuartsierra.component :as component]
            [palikka.context-provider :as cp]))

(defrecord Db [db]
  component/Lifecycle
  (start [this]
    (assoc this :db (atom {})))
  (stop [this]
    (assoc this :db nil))

  cp/ContextProvider
  (provide-context [this]
    {:db (:db this)}))

(defn create
  []
  (map->Db {}))
