(ns palikka.coerce-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [schema.core :as s]
            [palikka.coerce :refer :all]))

(s/defschema Config {(s/optional-key :port) s/Int
                     (s/optional-key :ssl?) s/Bool})

(deftest env-coerce-test
  (is (= {:port 3000} (env-coerce Config {:port "3000"})))
  (is (= {:ssl? true} (env-coerce Config {:ssl? "true"})))
  (is (thrown-with-msg? clojure.lang.ExceptionInfo #"Component options validation failed"
                        (env-coerce Config {:port :3000}))))
