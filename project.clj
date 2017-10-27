(defproject metosin/palikka "0.5.3-SNAPSHOT"
  :description "Metosin palikka"
  :url "https://github.com/metosin/palikka"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[potemkin "0.4.4"]
                 [org.clojure/tools.logging "0.4.0"]
                 [prismatic/schema "1.1.7"]
                 [metosin/schema-tools "0.9.1"]
                 [com.stuartsierra/component "0.3.2"]]
  :plugins [[lein-codox "0.10.0"]]

  :codox {:source-uri "http://github.com/metosin/palikka/blob/master/{filepath}#L{line}"}

  :profiles {:dev {:plugins [[jonase/eastwood "0.2.3"]
                             [lein-midje "3.2.1"]]
                   :source-paths ["examples"]
                   :resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.8.0"]
                                  [midje "1.9.0-alpha5"]
                                  [clj-http "3.7.0"]
                                  [reloaded.repl "0.2.4"]
                                  [metosin/maailma "0.2.0"]

                                  ; Components
                                  [com.novemberain/monger "3.1.0"]
                                  [hikari-cp "1.8.1"]
                                  [http-kit "2.2.0"]
                                  [org.postgresql/postgresql "42.1.4"]
                                  [org.flywaydb/flyway-core "4.2.0"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.datomic/datomic-free "0.9.5561.62" :exclusions [org.slf4j/slf4j-nop joda-time]]
                                  [com.novemberain/langohr "4.1.0"]
                                  [aleph "0.4.3"]

                                  ; Tests
                                  [com.h2database/h2 "1.4.196"]
                                  [org.clojure/java.jdbc "0.7.3"]

                                  ; Logging
                                  [metosin/lokit "0.1.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha13"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.7:dev,1.9"]
            "test-clj"  ["all" "do" ["midje"] ["check"]]})
