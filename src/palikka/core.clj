(ns palikka.core
  (:require [com.stuartsierra.component :as component])
  (:import [java.io Writer]))

(defprotocol GetProperty
  (-get [this component]))

(extend-protocol GetProperty
  clojure.lang.IFn
  (-get [f component]
    (f component))
  clojure.lang.PersistentVector
  (-get [ks component]
    (get-in component ks)))

(def +provides+ ::provides)

(defn providing
  "Associates metadata with component describing what values
   the component provides for others to use. Provides are specified
   as a map keys. Keys in the map correspond to key in context map.
   Values in the map are they keys in the component where provieded
   value is. The value can be a IFn so if you need to retrieve
   nested value you can use e.g. `(comp :foo :bar)`.
   Alternatively, if the keys are the same in both the component and
   context, they map be specified as vector of keys."
  [component spec]
  (vary-meta
    component
    update-in [+provides+] (fnil merge {})
    (cond
      (map? spec)    spec
      (vector? spec) (into {} (map (fn [x] [x x]) spec))

      :else
      (throw (ex-info "Provides spec must be a map or vector"
                      {:reason ::invalid-provides-spec
                       :component component
                       :dependencies spec})))))

;; Use record to control print-method of the context map.
;; This will ensure that secrets from context map (passwords from env)
;; aren't accidentally exposed when printing exceptions or such.
(defrecord Context [])

(defmethod print-method Context
  [v ^Writer w]
  (.write w "<<Context>>"))

(defn create-context
  "Create Context record (map) from system, containing values
  specified by providing calls.

  Context record has print-method which hides the real contents."
  [system]
  (reduce
    (fn [context component]
      (if-let [ctx (+provides+ (meta component))]
        (reduce-kv (fn [context k x]
                     (assoc context k (-get x component)))
                   context
                   ctx)
        context))
    (Context.)
    (vals system)))
