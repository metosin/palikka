(ns palikka.components.env
  (:require [com.stuartsierra.component :as component]
            [maailma.core :refer [read-config!]]
            [palikka.context-provider :as cp]))

(defrecord Env [prefix config]
  component/Lifecycle
  (start [this]
    (assoc this :config (read-config! prefix config)))
  (stop [this]
    (assoc this :config nil))

  cp/ContextProvider
  (provide-context [this]
    {:config config}))

(defn create [prefix config]
  (map->Env {:prefix prefix
             :config config}))
