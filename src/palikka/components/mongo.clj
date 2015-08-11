(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [palikka.core :refer [injections]]
            [schema.core :as s]))

(s/defschema Config
  {:url String})

(defrecord Mongo [conn db gfs]
  component/Lifecycle
  (start [this]
    (let [{:keys [config]}  (injections this)
          {:keys [url]}     (s/validate Config config)
          {:keys [conn db]} (m/connect-via-uri url)]
      (println (format "Connected to mongo on %s" url))
      (assoc this
             :conn conn
             :db db
             :gfs (m/get-gridfs conn (.getName db)))))
  (stop [this]
    (if conn
      (m/disconnect conn))
    (assoc this :conn nil :db nil :gfs nil)))

(defn create
  []
  (map->Mongo {}))
