(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {:url String})

(defrecord Mongo [component-config conn db gfs]
  component/Lifecycle
  (start [this]
    (let [{:keys [url]} component-config]
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
      (try
        (m/disconnect conn)
        (catch Throwable t
          (log/warn t "Error when closing Mongo connection"))))
    (assoc this :conn nil :db nil :gfs nil))

  suspendable/Suspendable
  (suspend [this]
    this)
  (resume [this old-component]
    (if (= component-config (:component-config old-component))
      (assoc this
             :conn (:conn old-component)
             :db (:db old-component)
             :gfs (:gfs old-component))
      (do (component/stop old-component)
          (component/start this)))))

(s/defn create [config]
  (map->Mongo {:component-config (c/env-coerce Config config)}))
