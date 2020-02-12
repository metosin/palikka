(defproject metosin/palikka "0.7.1"
  :description "Metosin palikka"
  :url "https://github.com/metosin/palikka"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/tools.logging "0.6.0"]
                 [prismatic/schema "1.1.12"]
                 [metosin/schema-tools "0.12.1"]
                 [com.stuartsierra/component "0.4.0"]]
  :plugins [[lein-codox "0.10.0"]]

  :codox {:source-uri "http://github.com/metosin/palikka/blob/master/{filepath}#L{line}"}

  :profiles {:dev {:plugins [[lein-midje "3.2.1"]]
                   :source-paths ["examples"]
                   :resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.10.1"]
                                  [midje "1.9.9"]
                                  [clj-http "3.10.0"]
                                  [reloaded.repl "0.2.4"]
                                  [metosin/maailma "1.1.0"]

                                  ; Components
                                  [com.novemberain/monger "3.5.0"]
                                  [hikari-cp "2.10.0"]
                                  [http-kit "2.3.0"]
                                  [org.postgresql/postgresql "42.2.10"]
                                  [org.flywaydb/flyway-core "6.2.2"]
                                  [nrepl "0.6.0"]
                                  [com.datomic/datomic-free "0.9.5697" :exclusions [org.slf4j/slf4j-nop joda-time]]
                                  [com.novemberain/langohr "5.1.0"]
                                  [aleph "0.4.6"]
                                  [org.immutant/web "2.1.10"]

                                  ; Tests
                                  [com.h2database/h2 "1.4.200"]
                                  [org.clojure/java.jdbc "0.7.11"]

                                  ; Logging
                                  [metosin/lokit "0.1.0"]]}}
  :aliases {"all" ["with-profile" "dev"]
            "test-clj"  ["all" "do" ["midje"] ["check"]]})
