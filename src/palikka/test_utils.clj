(ns palikka.test-utils
  (:require [com.stuartsierra.component :as component]))

(defn system-fixture [system]
  (fn [f]
    ; No-op if already stoped
    (alter-var-root system component/stop)
    (alter-var-root system component/start)
    (f)
    (alter-var-root system component/stop)))
