(defproject metosin/palikka "0.6.0"
  :description "Metosin palikka"
  :url "https://github.com/metosin/palikka"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/tools.logging "0.4.1"]
                 [prismatic/schema "1.1.9"]
                 [metosin/schema-tools "0.10.5"]
                 [com.stuartsierra/component "0.3.2"]]
  :plugins [[lein-codox "0.10.0"]]

  :codox {:source-uri "http://github.com/metosin/palikka/blob/master/{filepath}#L{line}"}

  :profiles {:dev {:plugins [[jonase/eastwood "0.2.3"]
                             [lein-midje "3.2.1"]]
                   :source-paths ["examples"]
                   :resource-paths ["test-resources"]
                   :dependencies [[org.clojure/clojure "1.9.0"]
                                  [midje "1.9.4"]
                                  [clj-http "3.9.1"]
                                  [reloaded.repl "0.2.4"]
                                  [metosin/maailma "1.1.0"]

                                  ; Components
                                  [com.novemberain/monger "3.1.0"]
                                  [hikari-cp "2.6.0"]
                                  [http-kit "2.3.0"]
                                  [org.postgresql/postgresql "42.2.5"]
                                  [org.flywaydb/flyway-core "5.2.1"]
                                  [nrepl "0.4.5"]
                                  [com.datomic/datomic-free "0.9.5697" :exclusions [org.slf4j/slf4j-nop joda-time]]
                                  [com.novemberain/langohr "5.0.0"]
                                  [aleph "0.4.6"]

                                  ; Tests
                                  [com.h2database/h2 "1.4.197"]
                                  [org.clojure/java.jdbc "0.7.8"]

                                  ; Logging
                                  [metosin/lokit "0.1.0"]]}}
  :aliases {"all" ["with-profile" "dev"]
            "test-clj"  ["all" "do" ["midje"] ["check"]]})
