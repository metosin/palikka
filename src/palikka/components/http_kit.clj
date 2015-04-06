(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [maailma.core :refer [env-get]]))

(s/defschema Config
  {:port s/Num
   s/Keyword s/Any})

(defrecord Http-kit [env handler]
  component/Lifecycle
  (start [this]
    (let [config  (env-get (:config env) [:http] Config)]
      (println "Starting http-kit on port" (:port config))
      (assoc this :http-kit (http-kit/run-server handler config))))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc this :http-kit nil)))

(defn create []
  (->Http-kit))
