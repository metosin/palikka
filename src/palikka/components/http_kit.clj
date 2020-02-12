(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {(s/optional-key :port) s/Int
   (s/optional-key :ip) s/Str
   s/Keyword s/Any})

(defrecord Http-kit [component-config component-handler http-kit]
  component/Lifecycle
  (start [this]
    (if-not http-kit
      (let [handler (cond
                     (map? component-handler) ((:fn component-handler) this)
                     :else component-handler)
            server (http-kit/run-server handler component-config)]
        (log/infof "Http-kit listening at http://%s:%d"
                   (or (:ip component-config) "0.0.0.0")
                   (:local-port (meta server)))
        (assoc this :http-kit server))
      this))
  (stop [this]
    (when http-kit
      (try
        (http-kit)
        (catch Throwable t
          (log/warn t "Error when stopping http-kit"))))
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
  (map->Http-kit {:component-config (c/env-coerce Config config) :component-handler handler}))
