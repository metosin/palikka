(ns palikka.components.mongo
  (:require [monger.core :as m]
            [com.stuartsierra.component :as component]
            [maailma.core :refer [env-get]]
            [palikka.context-provider :as cp]))

(defrecord Mongo [env conn db gfs]
  component/Lifecycle
  (start [this]
    (let [url (env-get (:config env) [:mongo :url])
          {:keys [conn db]} (m/connect-via-uri url)]
      (println (format "Connected to mongo on %s" url))
      (assoc this
             :conn conn
             :db db
             :gfs (m/get-gridfs conn (.getName db)))))
  (stop [this]
    (if conn
      (m/disconnect conn))
    (assoc this :conn nil :db nil :gfs nil))

  cp/ContextProvider
  (provide-context [this]
    {:db (:db this) :gfs (:fs this)}))

(defn create-mongo-connection
  []
  (map->Mongo {}))
