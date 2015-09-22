(ns palikka.components.flyway
  "Checks the migration status when starting the system
   so you'll know when to run migration function."
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :refer [warn]])
  (:import [org.flywaydb.core Flyway]
           [org.flywaydb.core.api FlywayException]))

;; TODO: create clj-flyway

(defn ->Flyway [ds schemas]
  (doto (Flyway.)
    (.setDataSource ds)
    (.setSchemas    (into-array String schemas))
    (.setLocations  (into-array String ["/db/migration"]))))

(defn check-migration-status!
  "Checks if there are pending migrations."
  [ds schemas]
  (try
    (let [flyway (->Flyway ds schemas)
          info (.info flyway)
          pending (map bean (.pending info))]
      (if (seq pending)
        (warn (str "WARNING: " (count pending) " migrations pending!"))))
    (catch FlywayException e
      (warn "WARNING: There were problems checking the migration status!" (.getMessage e)))))

(defrecord CheckFlyway [db schemas]
  component/Lifecycle
  (start [this]
    (check-migration-status! (-> db :db :datasource) schemas)
    this)
  (stop [this]
    this))

(defn check-flyway [{:keys [schemas]}]
  (map->CheckFlyway {:schemas schemas}))
