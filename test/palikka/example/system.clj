(ns palikka.example.system
  (:require [com.stuartsierra.component :as component :refer [using]]
            [palikka.core :refer [providing injecting]]
            [palikka.components.env :as env]
            [palikka.components.http-kit :as http-kit]
            [palikka.components.handler :as handler]
            [palikka.example.db :as db]))

(defn base-system [config]
  (component/system-map
    :env         (-> (env/create "example" config)
                     ; provides spec: using vector
                     (providing [:config]))
    :db          (-> (db/create)
                     (using [:env])
                     ; injections spec: using function
                     ; Note: references the system / component directly, maybe
                     ; should go through provides?
                     (injecting {:config #(-> % :env :config :db)})
                     ; provides spec: map
                     (providing {:db :db, :gfs :gfs}))
    :handler     (-> (handler/create 'palikka.example.handler/app)
                     (using [:env :db]))
    :http-kit    (-> (http-kit/create)
                     (using [:env :handler])
                     ; injections spec: using keyword vector
                     (injecting {:config [:env :config :http]}))))
