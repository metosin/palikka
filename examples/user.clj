(ns user
  (:require [reloaded.repl :refer [system start stop go reset]]
            [palikka.core :as palikka]))

(reloaded.repl/set-init!
  (fn []
    (require 'palikka.example.system)
    ((resolve 'palikka.example.system/base-system))))

(defn context []
  (palikka/create-context system))
