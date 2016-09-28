(ns palikka.components.datomic
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [datomic.api :as d]
            [palikka.coerce :as c]))

(s/defschema Config
  {:uri s/Str})

(defrecord Datomic [component-config conn db]
  component/Lifecycle
  (start [this]
    (log/infof "Connecting to datomic on %s" (:uri component-config))
    (if-not conn
      (let [db (d/create-database (:uri component-config))
            conn (d/connect (:uri component-config))]
        (assoc this :conn conn :db #(d/db conn)))
      this))
  (stop [this]
    (assoc this :conn nil :db nil)))

(defn create [config]
  (map->Datomic {:component-config (c/env-coerce Config config)}))
