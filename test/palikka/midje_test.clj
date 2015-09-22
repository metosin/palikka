(ns palikka.midje-test
  (:require [midje.sweet :refer :all]
            [com.stuartsierra.component :as component :refer [system-map using]]
            [palikka.core :refer [create-context]]
            [palikka.test-utils :refer :all]
            [clj-http.client :as client]
            [palikka.example.system :refer [base-system]]))

(defonce system (base-system {:http {:port 9999} :test "hello world"}))
(background (around :facts (system-background #'system ?form)))

(defn ctx [] (create-context system))

(facts system-test
  (fact "foo"
    (get-in system [:http-kit :http-kit])) => truthy
  (fact "bar"
    (:body (client/get "http://localhost:9999")) => "hello world"))

(facts context
  (instance? clojure.lang.Atom (:db (ctx))) => truthy)

(facts system-test-2
  (let [db (:db (:db system))]
    @db => {}
    (swap! db assoc :foo true)
    @db => {:foo true}))
