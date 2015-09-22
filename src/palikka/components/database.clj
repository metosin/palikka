(ns palikka.components.database
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [schema.core :as s]
            [schema-tools.core :as st]
            [clojure.tools.logging :refer [info]]))

(s/defschema Config
  (assoc (st/optional-keys hikari/ConfigurationOptions)
         :port-number s/Num
         s/Keyword s/Str))

(defrecord Database [config db]
  component/Lifecycle
  (start [this]
    (let [{:keys [server-name port-number] :as c} Config]
      (info (format "Connecting to database on %s:%s" server-name port-number))
      (assoc this :db {:datasource (hikari/make-datasource c)})))
  (stop [this]
    (when-let [ds (-> this :db :datasource)]
      (hikari/close-datasource ds)
      (.shutdown ds))
    (assoc this :db nil)))

(s/defn ^:always-validate create
  [config :- Config]
  (map->Database {:config config}))
