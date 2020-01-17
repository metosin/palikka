(ns palikka.components.flyway-test
  (:require [clojure.test :as t :refer [deftest is testing]]
            [palikka.components.flyway :as sut]))

(deftest constructor-test
  (is (sut/->Flyway nil {:connect-retries 5}))
  (is (sut/->Flyway nil {:connectRetries 5}))
  (is (sut/->Flyway nil {:schemas ["foo" "bar"]}))
  (is (thrown-with-msg? Exception #"Options contains duplicate keys:"
                        (sut/->Flyway nil {:connect-retries 5
                                           :connectRetries 10})))
  (is (thrown-with-msg? Exception #"No such Flyway option: foo \(\[Ljava\.lang\.String;\)"
                        (sut/->Flyway nil {:foo ["bar"]}))) )
