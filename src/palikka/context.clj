(ns palikka.context
  (:require [palikka.context-provider :as cp]))

(defn context-provider? [component]
  (satisfies? cp/ContextProvider component))

(defn create-context [system]
  (->> (vals system)
       (filter context-provider?)
       (map cp/provide-context)
       (apply merge)))
