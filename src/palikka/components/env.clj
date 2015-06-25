(ns palikka.components.env
  (:require [com.stuartsierra.component :as component]
            [maailma.core :refer [read-config!]]))

(defrecord Env [prefix config override]
  component/Lifecycle
  (start [this]
    (assoc this :config (read-config! prefix override)))
  (stop [this]
    (assoc this :config nil)))

(defn create [prefix override]
  (map->Env {:prefix prefix
             :override override}))
