(ns palikka.components.flyway
  "Checks the migration status when starting the system
   so you'll know when to run migration function."
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :refer [warn]]
            [schema.core :as s])
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
  [^Flyway flyway]
  (try
    (.migrate flyway)
    (catch FlywayException e
      (warn "WARNING: There were problems running the migrations!" (.getMessage e)))))

(defn check-migration-status!
  "Checks if there are pending migrations."
  [^Flyway flyway]
  (try
    (let [info (.info flyway)
          pending (map bean (.pending info))]
      (if (seq pending)
        (warn (str "WARNING: " (count pending) " migrations pending!"))))
    (catch FlywayException e
      (warn "WARNING: There were problems checking the migration status!" (.getMessage e)))))

(defrecord FlywayComponent [db opts migrate?]
  component/Lifecycle
  (start [this]
    (let [flyway (->Flyway (-> db :db :datasource) opts)]
      (if migrate?
        (migrate! flyway)
        (check-migration-status! flyway)))
    this)
  (stop [this]
    this))

(s/defn check [opts :- Config]
  (map->FlywayComponent {:opts opts}))

(s/defn migrate [opts :- Config]
  (map->FlywayComponent {:opts opts :migrate? true}))
