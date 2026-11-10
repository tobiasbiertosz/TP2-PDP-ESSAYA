(ns simulador.ui.ventana
  (:require [simulador.ui.lienzo :as lienzo])
  (:import [javafx.application Platform]
           [javafx.scene Scene]
           [javafx.scene.layout BorderPane]
           [javafx.stage Stage]))

(defn iniciar!
  "Arranca el toolkit de JavaFX. Se puede llamar más de una vez: si ya
   estaba iniciado, no hace nada."
  []
  (try
    (Platform/startup (fn []))
    ;; Con 'false', cerrar la ventana no apaga JavaFX. Sirve para poder
    ;; abrir otra ventana desde el REPL sin reiniciar todo.
    (Platform/setImplicitExit false)
    (catch IllegalStateException _
      nil)))

(defn abrir-ventana!
  "Abre una ventana que muestra la grilla dada (versión de prueba)."
  [grilla]
  (Platform/runLater
    (fn []
      (let [canvas    (lienzo/crear-lienzo (:ancho grilla) (:alto grilla))
            raiz      (BorderPane. canvas)
            escena    (Scene. raiz)
            escenario (Stage.)]
        (lienzo/dibujar! canvas grilla)
        (.setTitle escenario "Simulador de materiales")
        (.setScene escenario escena)
        (.show escenario)))))
