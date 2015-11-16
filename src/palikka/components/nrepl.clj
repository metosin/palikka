(ns palikka.components.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]
            [palikka.coerce :as c]))

(s/defschema Config
  {:port s/Int
   s/Keyword s/Any})

(defrecord Nrepl [config nrepl]
  component/Lifecycle
  (start [this]
    (if-not nrepl
      (let [{:keys [port]} config]
        (info (format "Starting nrepl server on nrepl://%s:%s" "localhost" port))
        (assoc this :nrepl (nrepl/start-server :port port)))
      this))
  (stop [this]
    (when nrepl
      (nrepl/stop-server nrepl))
    (assoc this :nrepl nil)))

(defn create [config]
  (map->Nrepl {:config (c/env-coerce Config config)}))
