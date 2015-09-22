(ns palikka.example.system
  (:require [maailma.core :refer [read-config!]]
            [palikka.sweet :refer [system-map providing using]]
            [palikka.components.http-kit :as http-kit]
            [palikka.example.db :as db]
            [palikka.handler :refer [wrap-env wrap-context]]
            [palikka.example.handler :refer [create-handler]]))

(defn base-system
  ([] (base-system nil))
  ([override]
   (let [env (read-config! "palikka" override)
         create-handler (fn [system]
                          (-> (create-handler system)
                              (wrap-context system)
                              (wrap-env (select-keys env [:test]))))]
     (system-map
       :db          (-> (db/create (:db env))
                        (providing {:db :db}))
       :http-kit    (-> (http-kit/create (:http env) create-handler)
                        ; For wrap-context
                        (using [:db]))))))
