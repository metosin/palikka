(ns palikka.components.env
  "Wraps static config value inside a component."
  (:require [com.stuartsierra.component :as component]))

(defrecord Env [config]
  component/Lifecycle
  (start [this]
    this)
  (stop [this]
    this))

(defn create [config]
  (map->Env {:config config}))
