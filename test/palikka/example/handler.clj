(ns palikka.example.handler)

(defn app [req]
  {:status 200
   :body (-> req :palikka/context :config :test)})
