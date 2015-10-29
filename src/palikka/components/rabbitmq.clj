(ns palikka.components.rabbitmq
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [schema.core :as s]
            [langohr.core :as rmq])
  (:import [com.rabbitmq.client Connection]))

(set! *warn-on-reflection* true)

(defrecord RabbitMQ [^Connection mq config]
  component/Lifecycle
  (start [this]
    (log/infof "Connecting to RabbitMQ on %s:%d" (:host config) (:port config))
    (assoc this :mq (rmq/connect config)))
  (stop [this]
    (when (and mq (.isOpen mq))
      (log/info "Closing RabbitMQ connection")
      (.close mq))
    (assoc this :mq nil)))

(defn create [config]
  (map->RabbitMQ {:config config}))
