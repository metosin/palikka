(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]
            [palikka.coerce :as c]))

(s/defschema Config
  {(s/optional-key :port) s/Int
   (s/optional-key :ip) s/Str
   s/Keyword s/Any})

(defrecord Http-kit [config handler http-kit]
  component/Lifecycle
  (start [this]
    (info (format "Http-kit listening at http://%s:%d" (or (:ip config) "0.0.0.0") (or (:port config) 8090)))
    (let [handler (handler this)]
      (assoc this :http-kit (http-kit/run-server handler config))))
  (stop [this]
    (when http-kit
      (http-kit))
    (assoc this :http-kit nil)))

(defn create
  [config handler]
  (map->Http-kit {:config (c/env-coerce Config config) :handler handler}))
