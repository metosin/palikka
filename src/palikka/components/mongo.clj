(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]
            [palikka.coerce :as c]))

(s/defschema Config
  {:url String})

(defrecord Mongo [config conn db gfs]
  component/Lifecycle
  (start [this]
    (let [{:keys [url]}     config
          {:keys [conn db]} (m/connect-via-uri url)]
      (info (format "Connected to mongo at %s" url))
      (assoc this
             :conn conn
             :db db
             :gfs (m/get-gridfs conn (.getName db)))))
  (stop [this]
    (when conn
      (m/disconnect conn))
    (assoc this :conn nil :db nil :gfs nil)))

(s/defn create [config]
  (map->Mongo {:config (c/env-coerce Config config)}))
