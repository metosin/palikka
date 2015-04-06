(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [palikka.context :as context]))

(reloaded.repl/set-init (fn []
                          (require 'project.system)
                          ((resolve 'project.system/base-system))))

(defn context []
  (context/create-context system))
