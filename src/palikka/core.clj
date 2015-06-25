(ns palikka.core)

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
      (throw (ex-info "Context must be a map or vector"
                      {:reason ::invalid-spec
                       :component component
                       :dependencies spec})))))

(defn create-context
  [system]
  (reduce
    (fn [context component]
      (if-let [ctx (+provides+ (meta component))]
        (reduce-kv (fn [context k v]
                     (assoc context k (v component)))
                   context
                   ctx)
        context))
    {}
    (vals system)))
