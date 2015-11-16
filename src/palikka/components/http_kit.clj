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
    (if-not http-kit
      (let [handler (cond
                      (map? handler) ((:fn handler) this)
                      :else handler)]
        (assoc this :http-kit (http-kit/run-server handler config)))
      this))
  (stop [this]
    (when http-kit
      (http-kit))
    (assoc this :http-kit nil)))

(defn create
  "Create http-kit component. `handler` should be handler function, var
  pointing to such function or map with property `fn`. If handler is function
  the value of `fn` property will be called with single argument, the system,
  to construct the handler function."
  [config handler]
  {:pre [(or (fn? handler)
             (var? handler)
             (and (map? handler) (fn? (:fn handler))))]}
  (map->Http-kit {:config (c/env-coerce Config config) :handler handler}))
