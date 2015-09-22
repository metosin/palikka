(ns palikka.components.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]))

(s/defschema Config
  {:port s/Num
   s/Keyword s/Any})

(defrecord Nrepl [config]
  component/Lifecycle
  (start [this]
    (let [{:keys [port]} config]
      (info (format "Starting nrepl server on nrepl://%s:%s" "localhost" port))
      (assoc this :nrepl (nrepl/start-server :port port))))
  (stop [this]
    (if-let [nrepl (:nrepl this)] (nrepl/stop-server nrepl))
    (assoc this :nrepl nil)))

(s/defn ^:always-validate create
  [config :- Config]
  (map->Nrepl {:config config}))
