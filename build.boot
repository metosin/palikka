(task-options!
  pom {:project 'metosin/palikka
       :version "0.1.0-SNAPSHOT"})

(def modules
  {:core     {}
   :database {:dependencies '[[hikari-cp "1.3.1"]]}
   :flyway   {:dependencies '[[org.flywaydb/flyway-core "3.2.1"]]}
   :http-kit {:dependencies '[[http-kit "2.1.19"]]}
   :mongo    {:dependencies '[[com.novemberain/monger "3.0.0"]]}
   :env      {:dependencies '[[metosin/maailma "0.1.0-SNAPSHOT"]]}})

(set-env!
  :resource-paths #{"src"}
  :dependencies '[[org.clojure/clojure        "1.7.0" :scope "test"]
                  [prismatic/schema           "1.0.1"]
                  [metosin/schema-tools       "0.6.0"]
                  [com.stuartsierra/component "0.2.3"]])

(defn filter-fileset [fileset p]
  (update
    fileset :tree
    (fn [tree]
      (reduce-kv (fn [tree path x]
                   (if (p x)
                     (assoc tree path x)
                     tree))
                 (empty tree)
                 tree))))

(deftask tag-files []
  (fn [next-handler]
    (fn [fileset]
      (next-handler (add-meta fileset {"palikka/components/mongo.clj"    {:module :mongo}
                                       "palikka/components/http_kit.clj" {:module :http-kit}
                                       "palikka/components/flyway.clj"   {:module :flyway}
                                       "palikka/components/database.clj" {:module :database}
                                       "palikka/components/env.clj"      {:module :env}})))))

(deftask build-module
  [m module MODULE kw ""]
  (comp
    (tag-files)
    (let [pom-middleware (pom :project (if (= :core module)
                                         'metosin/palikka
                                         (symbol "metosin" (str "palikka." (name module)))))]
      (fn [next-handler]
        (fn [fileset]
          (let [deps (get-env :dependencies)
                restore-deps-handler (fn [fileset]
                                       ; (println "restore-deps" module)
                                       (set-env! :dependencies deps)
                                       (next-handler fileset))
                pom-handler (pom-middleware restore-deps-handler)]
            ; (println "set-deps" module)
            (set-env! :dependencies #(into % (:dependencies (get modules module))))
            (pom-handler fileset)))))
    (let [jar-middleware (jar)]
      (fn [next-handler]
        (fn [fileset]
          (let [fileset' fileset
                merge-fileset-handler (fn [fileset]
                                        ; (println "merge fileset" module)
                                        (next-handler (commit! (update fileset :tree #(merge (:tree fileset') %)))))
                jar-handler (jar-middleware merge-fileset-handler)]
            ; (println "filter fileset" module)
            (jar-handler (filter-fileset fileset (fn [x]
                                                   ; (println (:path x) (:module x)  (.startsWith (:path x) "META-INF/maven/metosin/palikka/"))
                                                   (if (= :core module)
                                                     (or (.startsWith (:path x) "META-INF/maven/metosin/palikka/")
                                                         (and (not (.startsWith (:path x) "META-INF"))
                                                              (not (.endsWith (:path x) ".jar"))
                                                              (nil? (:module x))))
                                                     (or (.startsWith (:path x) (str "META-INF/maven/metosin/palikka." (name module)))
                                                         (= module (:module x)))))))))))))

(deftask build-modules []
  (comp
    (let [middlewares (map (fn [k] (build-module :module k)) (keys modules))]
      (fn [next-handler]
        (fn [fileset]
          ((reduce (fn [next-handler mw]
                     (mw next-handler))
                   next-handler
                   middlewares)
           fileset))))
    (install)))

(deftask dev []
  (comp
    (watch)
    (repl :server true)
    (build-modules)))
