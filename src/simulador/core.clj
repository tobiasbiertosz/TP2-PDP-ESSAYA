(ns simulador.core
  (:require [simulador.app.simulacion :as simulacion]
            [simulador.ui.ventana :as ventana])
  (:import [javafx.application Platform]))

(defn -main
  "Punto de entrada del programa."
  [& _argumentos]
  (ventana/iniciar!)
  ;; En el programa real, cerrar la ventana termina el programa.
  (Platform/setImplicitExit true)
  (simulacion/iniciar!)
  (ventana/abrir-ventana!))