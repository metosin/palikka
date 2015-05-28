(ns palikka.components.nrepl
  (:require [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [maailma.core :refer [env-get]]))

(s/defschema Config
  {:port s/Num
   s/Keyword s/Any})

(defrecord Nrepl [env]
  component/Lifecycle
  (start [this]
    (let [{:keys [port]} (env-get (:config env) [:nrepl] Config)]
      (println "Starting nrepl on port" port)
      (assoc this :nrepl (nrepl/start-server :port port))))
  (stop [this]
    (if-let [nrepl (:nrepl this)] (nrepl/stop-server nrepl))
    (assoc this :nrepl nil)))

(defn create
  []
  (map->Nrepl {}))

