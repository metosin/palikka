(ns palikka.components.aleph
  (:require [aleph.http :as aleph]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {:port s/Int
   s/Keyword s/Any})

(defrecord Aleph [component-config component-handler aleph]
  component/Lifecycle
  (start [this]
    (log/infof "Aleph listening at http://%s:%s" "0.0.0.0" (:port component-config))
    (if-not aleph
      (let [handler (cond
                     (map? component-handler) ((:fn component-handler) this)
                     :else component-handler)]
        (assoc this :aleph (aleph/start-server handler component-config)))
      this))
  (stop [this]
    (if aleph
      (try
        (.close aleph)
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
