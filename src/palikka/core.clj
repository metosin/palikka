(ns palikka.core
  (:require [com.stuartsierra.component :as component]))

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

(defn create-context
  [system]
  (reduce
    (fn [context component]
      (if-let [ctx (+provides+ (meta component))]
        (reduce-kv (fn [context k x]
                     (assoc context k (-get x component)))
                   context
                   ctx)
        context))
    {}
    (vals system)))

(extend-protocol component/Lifecycle
  clojure.lang.IPersistentMap
  (start [this]
    ((:start this)))
  (stop [this]
    ((:stop this))))

(defn http-component [state config]
  {:start (fn []
            (println "start" config)
            (http-component {:running true} config))
   :stop (fn []
           (if-not (:running state)
             (println "trying to stop not running http-server")
             (println "stop" config))
           (http-component nil config))})

(comment
  (component/stop (component/start (http-component nil {:port 8080})))
  (component/stop (http-component nil {:port 8080})))
