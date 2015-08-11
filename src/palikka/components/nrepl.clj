(ns palikka.components.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]
            [palikka.core :refer [injections]]
            [schema.core :as s]))

(s/defschema Config
  {:port s/Num
   s/Keyword s/Any})

(defrecord Nrepl []
  component/Lifecycle
  (start [this]
    (let [{:keys [config]} (injections this)
          {:keys [port]}   (s/validate Config config)]
      (println "Starting nrepl on port" port)
      (assoc this :nrepl (nrepl/start-server :port port))))
  (stop [this]
    (if-let [nrepl (:nrepl this)] (nrepl/stop-server nrepl))
    (assoc this :nrepl nil)))

(defn create
  []
  (map->Nrepl {}))

