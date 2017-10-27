(defproject metosin/palikka "0.5.3-SNAPSHOT"
  :description "Metosin palikka"
  :url "https://github.com/metosin/palikka"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[potemkin "0.4.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [prismatic/schema "1.1.3"]
                 [metosin/schema-tools "0.9.0"]
                 [com.stuartsierra/component "0.3.1"]]
  :plugins [[lein-codox "0.10.0"]]

  :codox {:source-uri "http://github.com/metosin/palikka/blob/master/{filepath}#L{line}"}

  :profiles {:dev {:plugins [[jonase/eastwood "0.2.3"]
                             [lein-midje "3.2.1"]]
                   :source-paths ["examples"]
                   :resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.8.0"]
                                  [midje "1.9.0-alpha5"]
                                  [clj-http "3.3.0"]
                                  [reloaded.repl "0.2.3"]
                                  [metosin/maailma "0.2.0"]

                                  ; Components
                                  [com.novemberain/monger "3.1.0"]
                                  [hikari-cp "1.7.4"]
                                  [http-kit "2.2.0"]
                                  [postgresql/postgresql "9.3-1102.jdbc41"]
                                  [org.flywaydb/flyway-core "4.0.3"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [com.datomic/datomic-free "0.9.5394" :exclusions [org.slf4j/slf4j-nop joda-time]]
                                  [com.novemberain/langohr "3.6.1"]
                                  [aleph "0.4.2-alpha8"]

                                  ; Tests
                                  [com.h2database/h2 "1.4.192"]
                                  [org.clojure/java.jdbc "0.6.1"]

                                  ; Logging
                                  [metosin/lokit "0.1.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha13"]]}}
  :aliases {"all" ["with-profile" "dev:dev,1.7:dev,1.9"]
            "test-clj"  ["all" "do" ["midje"] ["check"]]})
