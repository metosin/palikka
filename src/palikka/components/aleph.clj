(ns palikka.components.aleph
  (:require [aleph.http :as aleph]
            [aleph.netty :as netty]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c])
  (:import [java.net InetSocketAddress]
           [java.io Closeable]))

(s/defschema Config
  {:port s/Int
   (s/optional-key :host) s/Str
   s/Keyword s/Any})

(defrecord Aleph [component-config component-handler aleph]
  component/Lifecycle
  (start [this]
    (if-not aleph
      (let [handler (cond
                     (map? component-handler) ((:fn component-handler) this)
                     :else component-handler)
            socket-address (if (:host component-config)
                             (InetSocketAddress. ^String (:host component-config) ^int (:port component-config)))
            component-config (assoc component-config :socket-address socket-address)
            server (aleph/start-server handler component-config)]
        (log/infof "Aleph listening at http://%s:%s"
                   (or (:host component-config) "0.0.0.0")
                   (netty/port server))
        (assoc this :aleph server))
      this))
  (stop [this]
    (if aleph
      (try
        (.close ^Closeable aleph)
        (catch Throwable t
          (log/warn t "Error when stopping Aleph"))))
    (assoc this :aleph nil)))

(defn create
  "Create aleph component. `handler` should be handler function, a var
  pointing to such function or a map with property `fn`. If `handler`
  is a map, the value of `fn` property will be called with a single
  argument (the system) to construct the handler function."
  [config handler]
  {:pre [(or (fn? handler)
             (var? handler)
             (and (map? handler) (fn? (:fn handler))))]}
  (map->Aleph {:component-config (c/env-coerce Config config) :component-handler handler}))
