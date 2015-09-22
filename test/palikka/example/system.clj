(ns palikka.example.system
  (:require [maailma.core :refer [read-config!]]
            [palikka.sweet :refer [system-map providing using]]
            [palikka.components.http-kit :as http-kit]
            [palikka.example.db :as db]
            [palikka.handler :refer [wrap-env]]))

(defn base-system
  ([] (base-system nil))
  ([override]
   (require 'palikka.example.handler)
   (let [env (read-config! "palikka" override)
         handler (-> (resolve 'palikka.example.handler/app)
                     (wrap-env (select-keys env [:test])))]
     (system-map
       :db          (-> (db/create (:db env))
                        (providing {:db :db}))
       :http-kit    (-> (http-kit/create (:http env) handler)
                        ; For wrap-context
                        (using [:db]))))))
