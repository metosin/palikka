(ns palikka.coerce
  "Provides Schema coercion for component configuration values.

  This is useful when options are read from string only sources like
  envinronment variables or system properties. Coercion is based on
  schema.coerce/string-coercion-matcher but additionally includes
  following coercions:

  - Single string to collection by splitting the string on \":\""
  (:require [schema.coerce :as sc]
            [schema.utils :as su]
            [clojure.string :as string]))

(defn string-collection-matcher [schema]
  (if (and (coll? schema) (not (map? schema)))
    (fn [v]
      (if (string? v)
        (into (empty schema) (string/split v #":"))
        v))))

(defn env-coercion-matcher [schema]
  (or (sc/string-coercion-matcher schema)
      (string-collection-matcher schema)))

(defn env-coercer [schema]
  (sc/coercer schema env-coercion-matcher))

(defn env-coerce
  "Coerces and validates value against given schema. Throws if
  coercion of validation fails."
  [schema v]
  (let [coercer (env-coercer schema)
        result (coercer v)]
    (if (su/error? result)
      (throw (IllegalArgumentException. (str "Component options validation failed: " (pr-str (su/error-val result)))))
      result)))
