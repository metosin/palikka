(defproject metosin/palikka "0.1.0-SNAPSHOT"
  :description "Metosin palikka"
  :url "https://github.com/metosin/palikka"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/clojure "1.7.0-beta3"]
                 [prismatic/schema "0.4.2"]
                 [metosin/maailma "0.1.0-SNAPSHOT"]
                 [com.stuartsierra/component "0.2.3"]]
  :plugins [[codox "0.8.10"]]

  :codox {:src-dir-uri "http://github.com/metosin/palikka/blob/master/"
          :src-linenum-anchor-prefix "L"}

  :profiles {:dev {:plugins [[jonase/eastwood "0.2.1"]]
                   :resource-paths ["test-resources"]
                   :dependencies [[midje "1.7.0-beta1"]
                                  [clj-http-lite "0.2.1"]

                                  ; Components
                                  [com.novemberain/monger "2.0.1"]
                                  [hikari-cp "1.2.1"]
                                  [http-kit "2.1.18"]
                                  [clj-http "1.1.0"]
                                  [hikari-cp "1.2.1"]
                                  [postgresql/postgresql "9.3-1102.jdbc41"]
                                  [org.flywaydb/flyway-core "3.2.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.6"]
            "test-clj"  ["all" "do" ["test"] ["check"]]})
