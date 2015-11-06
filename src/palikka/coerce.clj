(ns palikka.coerce
  (:require [schema.coerce :as sc]
            [schema.utils :as su]))

(defn env-coercer [schema]
  (sc/coercer schema sc/+string-coercions+))

(defn env-coerce
  "Coerces and validates value against given schema. Throws if
  coercion of validation fails."
  [schema v]
  (let [coercer (env-coercer schema)
        result (coercer v)]
    (if (su/error? result)
      (throw (ex-info "Component options validation failed" (su/error-val result)))
      result)))
