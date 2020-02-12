(ns palikka.components.immutant-web
  (:require [immutant.web :as web]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {(s/optional-key :port) s/Int
   (s/optional-key :host) s/Str
   s/Keyword s/Any})

(defrecord Http [component-config component-handler server]
  component/Lifecycle
  (start [this]
    (if-not server
      (let [handler (cond
                     (map? component-handler) ((:fn component-handler) this)
                     :else component-handler)
            server (web/run handler component-config)]
        (log/infof "Immutant web listening at http://%s:%d" (:host server) (:port server))
        (assoc this :server server))
      this))
  (stop [this]
    (when server
      (try
        (web/stop server)
        (catch Throwable t
          (log/warn t "Error when stopping Immutant"))))
    (assoc this :server nil)))

(defn create
  [config handler]
  {:pre [(or (fn? handler)
             (var? handler)
             (and (map? handler) (fn? (:fn handler))))]}
  (map->Http {:component-config (c/env-coerce Config config) :component-handler handler}))

