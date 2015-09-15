(ns palikka.example.system
  (:require [maailma.core :refer [read-config!]]
            [palikka.sweet :refer [system-map providing using]]
            [palikka.components.env :as env]
            [palikka.components.http-kit :as http-kit]
            [palikka.components.handler :as handler]
            [palikka.example.db :as db]))

(defn base-system
  ([] (base-system nil))
  ([override]
   (let [env (read-config! "palikka" override)]
     (system-map
       ; Env component is available so that the configuration map is available from
       ; system, e.g. for http handlers.
       :env         (-> (env/create env)
                        (providing {:config :config}))
       :db          (-> (db/create (:db env))
                        (providing {:db :db}))
       :handler     (-> (handler/create 'palikka.example.handler/app)
                        (using [:db]))
       :http-kit    (-> (http-kit/create (:http env))
                        (using [:handler]))))))
