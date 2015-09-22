(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :refer [info]]))

(s/defschema Config
  {(s/optional-key :port) s/Num
   (s/optional-key :ip) s/Str
   s/Keyword s/Any})

(defrecord Http-kit [config handler]
  component/Lifecycle
  (start [this]
    (info (format "Http-kit listening at http://%s:%d" (or (:ip config) "0.0.0.0") (or (:port config) 8090)))
    (assoc this :http-kit (http-kit/run-server (:handler handler) config)))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc this :http-kit nil)))

(s/defn ^:always-validate create
  [config :- Config]
  (map->Http-kit {:config config}))
