(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]))

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
    (if conn
      (m/disconnect conn))
    (assoc this :conn nil :db nil :gfs nil)))

(s/defn create
  ^:always-validate
  [config :- Config]
  (map->Mongo {:config config}))
