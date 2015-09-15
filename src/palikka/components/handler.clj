(ns palikka.components.handler
  (:require [com.stuartsierra.component :as component]
            [schema.core :as s]
            [palikka.core :as palikka]))

(defn wrap-context [handler system]
  (let [c (palikka/create-context system)]
    (fn [req]
      (handler (assoc req :palikka/context c)))))

(defrecord Handler [sym context]
  component/Lifecycle
  (start [this]
    (require (symbol (namespace sym)) :reload)
    (assoc this :handler (-> (resolve sym)
                             (wrap-context this))))
  (stop [this]
    this))

(s/defn create
  "Construct a ring middleware from given symbol.

   Warning: Created handler uses a Var, each request will deref the var,
   which allows redefining the handler without restarting the system
   but has a small performance penalty."
  [sym :- s/Symbol]
  (map->Handler {:sym sym}))
