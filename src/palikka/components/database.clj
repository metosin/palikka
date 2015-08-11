(ns palikka.components.database
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [schema.core :as s]
            [palikka.core :refer [injections]]
            [schema-tools.core :as st]))

(s/defschema Config
  (assoc (st/optional-keys hikari/ConfigurationOptions)
         :port-number s/Num
         s/Keyword s/Str))

(defrecord Database [db]
  component/Lifecycle
  (start [this]
    (let [{:keys [config]} (injections this)
          {:keys [server-name port-number] :as c}
          (s/validate Config config)]
      (println (format "Connecting to database on %s:%s" server-name port-number))
      (assoc this :db {:datasource (hikari/make-datasource c)})))
  (stop [this]
    (when-let [ds (-> this :db :datasource)]
      (hikari/close-datasource ds)
      (.shutdown ds))
    (assoc this :db nil)))

(defn create []
  (map->Database {}))
