(ns palikka.components.datomic
  (:require [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [datomic.api :as d]
            [palikka.coerce :as c]))

(s/defschema Config
  {:uri s/Str})

(defrecord Datomic [component-config conn db]
  component/Lifecycle
  (start [component]
    (log/infof "Connecting to datomic on %s" (:uri component-config))
    (if-not conn
      (let [db (d/create-database (:uri component-config))
            conn (d/connect (:uri component-config))]
        (assoc component :conn conn :db #(d/db conn)))
      this))
  (stop [component]
    (assoc component :conn nil :db nil))

  suspendable/Suspendable
  (suspend [this]
    this)
  (resume [this old-component]
    (if (= component-config (:component-config old-component))
      (assoc this
             :conn (:conn old-component)
             :db (:db old-component))
      (do (component/stop old-component)
          (component/start this)))))

(defn create [config]
  (map->Datomic {:component-config (c/env-coerce Config config)}))
