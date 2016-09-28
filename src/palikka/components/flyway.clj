(ns palikka.components.flyway
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]
            [schema.core :as s]
            [palikka.coerce :as c])
  (:import [org.flywaydb.core Flyway]
           [org.flywaydb.core.api FlywayException]))

(s/defschema Config {:schemas [s/Str]
                     :locations [s/Str]})

(defn ->Flyway [ds {:keys [schemas locations]}]
  (doto (Flyway.)
    (.setDataSource ds)
    (.setSchemas    (into-array String schemas))
    (.setLocations  (into-array String (or locations ["/db/migration"])))))

(defn migrate!
  "Runs migrations"
  [^Flyway flyway {:keys [rethrow-exceptions?] :as opts}]
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
  (map->FlywayComponent {:opts (c/env-coerce Config opts)}))

(defn migrate
  "Runs the migrations."
  [opts]
  (map->FlywayComponent {:opts (c/env-coerce Config opts)
                         :migrate? true}))
