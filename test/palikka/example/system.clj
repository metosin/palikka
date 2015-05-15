(ns palikka.example.system
  (:require [com.stuartsierra.component :as component :refer [using]]
            [palikka.components.env :as env]
            [palikka.components.http-kit :as http-kit]
            [palikka.components.handler :as handler]
            [palikka.example.db :as db]))

(defn base-system [config]
  (component/system-map
    :env         (env/create "example" config)
    :db          (db/create)
    :handler     (using (handler/create 'palikka.example.handler/app) [:env :db])
    :http-kit    (using (http-kit/create) [:env :handler])))
