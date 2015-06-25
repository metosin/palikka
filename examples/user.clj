(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [palikka.core :as palikka]))

(reloaded.repl/set-init (fn []
                          (require 'project.system)
                          ((resolve 'project.system/base-system))))

(defn context []
  (palikka/create-context system))
