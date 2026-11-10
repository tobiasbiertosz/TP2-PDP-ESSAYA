(ns simulador.core
  (:require [simulador.sim.grilla :as g]
            [simulador.ui.ventana :as ventana])
  (:import [javafx.application Platform]))

(defn -main
  "Punto de entrada del programa."
  [& _argumentos]
  (ventana/iniciar!)
  ;; Al ejecutar el programa de verdad, cerrar la ventana debe terminarlo.
  (Platform/setImplicitExit true)
  (ventana/abrir-ventana! (g/grilla-vacia 160 120)))