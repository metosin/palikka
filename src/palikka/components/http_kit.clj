(ns palikka.components.http-kit
  (:require [org.httpkit.server :as http-kit]
            [com.stuartsierra.component :as component]))

(defrecord Http-kit []
  component/Lifecycle
  (start [this]
    (let [config  (get-in component [:env :config :http])
          handler (get-in component [:handler :handler])]
      (println "Starting http-kit on port" (:port config))
      (assoc component :http-kit (http-kit/run-server handler config))))
  (stop [this]
    (if-let [http-kit (:http-kit this)]
      (http-kit))
    (assoc component :http-kit nil)))

(defn create []
  (->Http-kit))
