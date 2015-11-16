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

(defrecord Database [config db]
  component/Lifecycle
  (start [this]
    (let [{:keys [server-name port-number] :as c} config]
      (log/infof "Connecting to database on %s:%s" server-name port-number)
      (if-not db
        (assoc this :db {:datasource (hikari/make-datasource c)})
        this)))
  (stop [this]
    (when-let [ds (:datasource db)]
      (hikari/close-datasource ds))
    (assoc this :db nil)))

(defn create [config]
  (map->Database {:config (c/env-coerce Config config)}))
