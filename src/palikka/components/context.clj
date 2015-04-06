(ns palikka.components.context
  (:require [com.stuartsierra.component :as component]
            [palikka.context-provider :as cp]))

(defn context-provider? [component]
  (satisfies? cp/ContextProvider component))

(defprotocol Context
  (extract-context [component]))

(defrecord ContextComoponent []
  component/Lifecycle
  (start [component]
    component)
  (stop [component]
    component)

  Context
  (extract-context [component]
    (->> (vals component)
         (filter context-provider?)
         (map cp/provide-context)
         (apply merge))))

(defn create []
  (->ContextComoponent))
