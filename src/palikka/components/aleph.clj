(ns palikka.components.aleph
  (:require [aleph.http :as aleph]
            [com.stuartsierra.component :as component]
            [suspendable.core :as suspendable]
            [schema.core :as s]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  {:port s/Int
   s/Keyword s/Any})

(defrecord Aleph [config handler aleph]
  component/Lifecycle
  (start [this]
    (log/infof "Aleph listening at "
               (.getHostAddress (java.net.Inet4Address/getLocalHost))
               ":"
               (:port config))
    (if-not aleph
      (let [handler (cond
                      (map? handler) ((:fn handler) this)
                      :else handler)]
        (assoc this :aleph (aleph/start-server handler config)))
      this))
  (stop [this]
    (if aleph
      (.close aleph))
    (assoc this :aleph nil))

  suspendable/Suspendable
  (suspend [this]
    this)
  (resume [this old-component]
    (if (= config (:config old-component))
      (assoc this :aleph (:aleph old-component))
      (do (component/stop old-component)
          (component/start this)))))

(defn create
  "Create aleph component. `handler` should be handler function, a var
  pointing to such function or a map with property `fn`. If `handler`
  is a map, the value of `fn` property will be called with a single
  argument (the system) to construct the handler function."
  [config handler]
  {:pre [(or (fn? handler)
             (var? handler)
             (and (map? handler) (fn? (:fn handler))))]}
  (map->Aleph {:config (c/env-coerce Config config) :handler handler}))
