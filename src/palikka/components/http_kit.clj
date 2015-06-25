(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]
            [palikka.core :refer [injections]]
            [schema.core :as s]))

(s/defschema Config
  {:port s/Num
   s/Keyword s/Any})

(defrecord Http-kit [handler]
  component/Lifecycle
  (start [this]
    (let [{:keys [config]} (injections this)
          config (s/validate Config config)]
      (println "Starting http-kit on port" (:port config))
      (assoc this :http-kit (http-kit/run-server (:handler handler) config))))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc this :http-kit nil)))

(defn create []
  (map->Http-kit {}))
