(ns palikka.components.datomic
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [datomic.api :as d]
            [palikka.coerce :as c]))

(s/defschema Config
  {:uri s/Str})

(defrecord Datomic [config conn db]
  component/Lifecycle
  (start [component]
    (log/infof "Connecting to datomic on %s" (:uri config))
    (if-not conn
      (let [db (d/create-database (:uri config))
            conn (d/connect (:uri config))]
        (assoc component :conn conn :db #(d/db conn)))
      this))
  (stop [component]
    (assoc component :conn nil :db nil)))

(defn create [config]
  (map->Datomic {:config (c/env-coerce Config config)}))
