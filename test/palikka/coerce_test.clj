(ns palikka.coerce-test
  (:require [clojure.test :refer [deftest testing is use-fixtures]]
            [schema.core :as s]
            [palikka.coerce :refer :all]))

(s/defschema Config {(s/optional-key :port) s/Int
                     (s/optional-key :ssl?) s/Bool
                     (s/optional-key :schemas) [s/Str]
                     (s/optional-key :ports) [s/Int]})

(deftest env-coerce-test
  (is (= {:port 3000} (env-coerce Config {:port "3000"})))
  (is (= {:ssl? true} (env-coerce Config {:ssl? "true"})))
  (is (= {:schemas ["flyway" "app"]} (env-coerce Config {:schemas "flyway:app"})))
  (is (= {:ports [123 234]} (env-coerce Config {:ports "123:234"})))
  (is (thrown-with-msg? IllegalArgumentException #"Component options validation failed: "
                        (env-coerce Config {:port :3000}))))
