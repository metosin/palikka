(ns palikka.components.handler
  (:require [com.stuartsierra.component :as component]
            [palikka.components.context :as context]))

(defn wrap-context [handler context]
  (let [c (context/extract-context context)]
    (fn [req]
      (handler (assoc req ::context c)))))

(defrecord Handler [sym context]
  component/Lifecycle
  (start [this]
    (require (symbol (namespace sym)) :reload)
    (assoc this :handler (-> (resolve sym)
                             (wrap-context context))))
  (stop [this]
    this))

(defn create
  "Construct a ring middleware from given symbol.

   Warning: Created handler uses a Var, each request will deref the var,
   which allows redefining the handler without restarting the system
   but has a small performance penalty."
  [sym]
  (map->Handler {:sym sym}))
