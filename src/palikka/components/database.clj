(ns palikka.components.database
  (:require [com.stuartsierra.component :as component]
            [hikari-cp.core :as hikari]
            [schema.core :as s]
            [schema-tools.core :as st]
            [clojure.tools.logging :as log]
            [palikka.coerce :as c]))

(s/defschema Config
  ; Options that should be coerced from string to something
  ; Hikari takes care of validation itself
  (-> {:auto-commit        s/Bool
       :read-only          s/Bool
       :register-mbeans    s/Bool
       :connection-timeout s/Num
       :validation-timeout s/Num
       :idle-timeout       s/Num
       :max-lifetime       s/Num
       :minimum-idle       s/Num
       :maximum-pool-size  s/Num
       :leak-detection-threshold s/Num
       :port-number        s/Num}
      st/optional-keys
      (assoc s/Keyword s/Any)))

(defrecord Database [component-config db]
  component/Lifecycle
  (start [this]
    (log/infof "Connecting to database on %s" (cond
                                                (:jdbc-url component-config) (:jdbc-url component-config)
                                                (:url component-config)      (:url component-config)
                                                :else (str (:server-name component-config) ":" (:port-number component-config))))
    (if-not db
      (assoc this :db {:datasource (hikari/make-datasource component-config)})
      this))
  (stop [this]
    (when-let [ds (:datasource db)]
      (try
        (hikari/close-datasource ds)
        (catch Throwable t
          (log/warn t "Error when closing JDBC connection"))))
    (assoc this :db nil)))

(defn create [config]
  (map->Database {:component-config (c/env-coerce Config config)}))
