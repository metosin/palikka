(ns palikka.test-utils
  (:require [com.stuartsierra.component :as component]))

(defn down? [test-system]
  (let [c (::count (meta test-system))]
    (or (nil? c) (zero? c))))

(defn update-meta [m k f]
  (with-meta m (update-in (meta m) [k] f)))

(defn system-up! [test-system]
  (alter-var-root
    test-system
    (fn [system]
      (-> system
          (cond-> (down? system) component/start)
          (update-meta ::count (fnil inc 0))))))

(defn system-down! [test-system]
  (alter-var-root
    test-system
    (fn [system]
      (if (down? system)
        system
        (-> system
            (cond-> (= 1 (::count (meta system))) component/stop)
            (update-meta ::count dec))))))

;;
;; Clojure test
;;

(defn system-fixture [system]
  (fn [f]
    (system-up! system)
    (f)
    (system-down! system)))

;;
;; Midje
;;

(defmacro system-background [system & body]
  `(try
     (system-up! ~system)
     (do ~@body)
     (finally
       (system-down! ~system))))
