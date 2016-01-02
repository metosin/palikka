(ns palikka.components.database
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [schema.core :as s]
            [schema-tools.core :as st]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  (assoc (st/optional-keys hikari/ConfigurationOptions)
         :port-number s/Num
         s/Keyword s/Str))

(defrecord Database [component-config db]
  component/Lifecycle
  (start [this]
    (log/infof "Connecting to database on %s:%s" (:server-name component-config) (:port-number component-config))
    (if-not db
      (assoc this :db {:datasource (hikari/make-datasource component-config)})
      this))
  (stop [this]
    (when-let [ds (:datasource db)]
      (try
        (hikari/close-datasource ds)
        (catch Throwable t
          (log/warn t "Error when closing JDBC connection"))))
    (assoc this :db nil)))

(defn create [config]
  (map->Database {:component-config (c/env-coerce Config config)}))
