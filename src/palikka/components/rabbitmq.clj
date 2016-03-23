(ns palikka.components.rabbitmq
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [langohr.core :as rmq]
            [palikka.coerce :as c])
  (:import [com.rabbitmq.client Connection]))

(s/defschema Config
  {(s/optional-key :host) s/Str
   (s/optional-key :port) s/Int
   (s/optional-key :username) s/Str
   (s/optional-key :password) s/Str
   (s/optional-key :vhost) s/Str
   (s/optional-key :ssl) s/Bool
   (s/optional-key :uri) s/Str
   s/Keyword s/Str})

(defrecord RabbitMQ [^Connection mq component-config]
  component/Lifecycle
  (start [this]
    (log/infof "Connecting to RabbitMQ on %s:%d" (:host component-config) (:port component-config))
    (if-not mq
      (assoc this :mq (rmq/connect component-config))
      this))
  (stop [this]
    (when (and mq (.isOpen mq))
      (log/info "Closing RabbitMQ connection")
      (try
        (.close mq)
        (catch Throwable t
          (log/warn t "Error when closing RabbitMQ collection"))))
    (assoc this :mq nil)))

(defn create [config]
  (map->RabbitMQ {:component-config (c/env-coerce Config config)}))
