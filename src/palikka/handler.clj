(ns palikka.handler
  (:require [palikka.core :as palikka]))

(defn wrap-context [handler system]
  (let [c (palikka/create-context system)]
    (fn [req]
      (handler (assoc req :palikka/context c)))))

(defn wrap-env [handler env]
  (fn [req]
    (handler (assoc req :palikka/env env))))
