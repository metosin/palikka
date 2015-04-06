(ns palikka.components.database
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [schema.core :as s]
            [maailma.core :refer [env-get]]
            [palikka.context-provider :as cp]))

(s/defschema Config
  (assoc hikari/ConfigurationOptions
         :port-number s/Num))

(defrecord Database [env db]
  component/Lifecycle
  (start [this]
    (let [{:keys [server-name port-number] :as c}
          (env-get (:config env) [:db] Config)]
      (println (format "Connecting to database on %s:%s" server-name port-number))
      (assoc this :db {:datasource (hikari/make-datasource c)})))
  (stop [this]
    (when-let [ds (-> this :db :datasource)]
      (hikari/close-datasource ds)
      (.shutdown ds))
    (assoc this :db nil))

  cp/ContextProvider
  (provide-context [this]
    {:db db}))

(defn create []
  (map->Database {}))
