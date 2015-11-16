(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {:url String})

(defrecord Mongo [config conn db gfs]
  component/Lifecycle
  (start [this]
    (let [{:keys [url]}     config]
      (log/infof "Connected to mongo at %s" url)
      (if-not conn
        (let [{:keys [conn db]} (m/connect-via-uri url)]
          (assoc this
                 :conn conn
                 :db db
                 :gfs (m/get-gridfs conn (.getName db))))
        this)))
  (stop [this]
    (when conn
      (m/disconnect conn))
    (assoc this :conn nil :db nil :gfs nil)))

(s/defn create [config]
  (map->Mongo {:config (c/env-coerce Config config)}))
