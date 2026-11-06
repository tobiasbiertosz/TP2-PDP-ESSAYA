(ns simulador.core
  (:require [simulador.sim.materials :as m]))

(defn -main [& _args]
  (println "Materiales cargados:" (count m/materials)))