(ns simulador.core
  (:require [simulador.sim.materiales :as m]))

(defn -main [& _args]
  (println "Materiales cargados:" (count m/materials)))