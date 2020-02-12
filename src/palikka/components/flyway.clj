(ns palikka.components.flyway
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [clojure.string :as str])
  (:import [java.util Map HashMap]
           [java.lang.reflect Method]
           [org.flywaydb.core Flyway]
           [org.flywaydb.core.api FlywayException]
           [org.flywaydb.core.api.configuration FluentConfiguration]))

(defn snake->camel [s]
  (str/replace s #"-([a-zA-Z])" (fn [[_ c]]
                                  (str/upper-case c))))

(comment
  (snake->camel "foo-bar"))

;; TODO: Add
(defn ->Flyway
  "Check https://flywaydb.org/documentation/api/javadoc/org/flywaydb/core/api/configuration/FluentConfiguration.html
  for available options.

  Options keys can be provided in snake-case and converted to camelCase, or directly as camelCase.
  If value is a Clojure vector, it is converted to Java Array, and Clojure map are converted to
  Java HashMaps. Other values are used as is."
  [ds options]
  (let [options
        (reduce-kv (fn [acc k v]
                             (let [k (snake->camel (name k))]
                               (when (contains? acc k)
                                 (throw (ex-info (str "Options contains duplicate keys: " k) {:key k})))
                               (assoc acc k v)))
                           {}
                           options)

        ^FluentConfiguration config
        (-> (Flyway/configure)
            (.dataSource ds)
            (as-> $ (reduce (fn [c [k v]]
                              (let [[j-v ^Class v-class] (cond
                                                           (boolean? v) [(boolean v) Boolean/TYPE]
                                                           (integer? v) [(int v) Integer/TYPE]
                                                           (vector? v) (let [v (into-array (class (first v)) v)]
                                                                         [v (class v)])
                                                           (map? v) [(HashMap. ^Map v) HashMap]
                                                           :else [v (class v)])
                                    ^Method method (try
                                                     (.getDeclaredMethod FluentConfiguration k (into-array Class [v-class]))
                                                     (catch NoSuchMethodException _
                                                       (throw (ex-info (str "No such Flyway option: " k " (" (.getName v-class) ")") {:k k :class v-class :v v :options options}))) )]
                                (.invoke method c (into-array Object [j-v]))))
                            $
                            ;; Remove palikka local options
                            (dissoc options :rethrow-exceptions?))))]
    (.load config)))

(defn migrate!
  "Runs migrations"
  [^Flyway flyway {:keys [rethrow-exceptions?]}]
  (try
    (.migrate flyway)
    (catch FlywayException e
      (log/warnf e "WARNING: There were problems running the migrations: %s" (.getMessage e))
      (if rethrow-exceptions?
        (throw e)))))

(defn check-migration-status!
  "Checks if there are pending migrations."
  [^Flyway flyway]
  (try
    (let [info (.info flyway)
          pending (map bean (.pending info))]
      (if (seq pending)
        (log/warnf "WARNING: %d migrations pending!" (count pending))))
    (catch FlywayException e
      (log/warnf e "WARNING: There were problems checking the migration status: %s" (.getMessage e))
      (throw e))))

(defrecord FlywayComponent [db opts migrate?]
  component/Lifecycle
  (start [this]
    (let [flyway (->Flyway (-> db :db :datasource) opts)]
      (if migrate?
        (migrate! flyway opts)
        (check-migration-status! flyway)))
    this)
  (stop [this]
    this))

(defn check
  "Checks the migration status when starting the system
  so you'll know when to run the migrations."
  [opts]
  (map->FlywayComponent {:opts opts}))

(defn migrate
  "Runs the migrations."
  [opts]
  (map->FlywayComponent {:opts opts
                         :migrate? true}))
