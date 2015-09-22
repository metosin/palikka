(ns palikka.example.handler)

(defn app [req]
  {:status 200
   :body (-> req :palikka/env :test)})

(defn create-handler [system]
  (-> #'app))
