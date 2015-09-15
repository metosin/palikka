(ns palikka.sweet
  (:require [potemkin :refer [import-vars]]
            palikka.core
            com.stuartsierra.component))

(import-vars
  [com.stuartsierra.component

   dependencies
   start
   start-system
   stop
   stop-system
   system-map
   using
   system-using]

  [palikka.core

   providing
   create-context])
