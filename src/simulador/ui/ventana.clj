(ns simulador.ui.ventana
  (:require [simulador.app.estado :as estado]
            [simulador.app.simulacion :as simulacion]
            [simulador.sim.grilla :as g]
            [simulador.ui.lienzo :as lienzo]
            [simulador.ui.entrada :as entrada]
            [simulador.ui.controles :as controles])
  (:import [javafx.animation AnimationTimer]
           [javafx.application Platform]
           [javafx.scene Scene]
           [javafx.scene.layout BorderPane]
           [javafx.stage Stage]))

(defn iniciar!
  "Arranca el toolkit de JavaFX. Si ya estaba iniciado, no hace nada."
  []
  (try
    (Platform/startup (fn []))
    (Platform/setImplicitExit false)
    (catch IllegalStateException _
      nil)))

(defn abrir-ventana!
  "Arma y muestra la ventana principal (grilla + controles)."
  []
  (Platform/runLater
    (fn []
      (let [grilla       (:grilla @estado/mundo)
            canvas       (lienzo/crear-lienzo (:ancho grilla) (:alto grilla))
            ctl          (controles/crear-controles)
            raiz         (BorderPane.)
            escenario    (Stage.)
            temporizador (proxy [AnimationTimer] []
                           (handle [_ahora]
                             (let [mundo @estado/mundo]
                               (lienzo/dibujar! canvas (:grilla mundo))
                               (.setText (:etiqueta-paso ctl)
                                         (str "paso: " (:paso mundo)))
                               (.setText (:etiqueta-particulas ctl)
                                         (str "partículas: "
                                              (g/contar-particulas
                                                (:grilla mundo)))))))]
        (entrada/conectar-mouse! canvas)
        (.setCenter raiz canvas)
        (.setRight raiz (:panel ctl))
        (.setTitle escenario "Simulador de materiales")
        (.setScene escenario (Scene. raiz))
        (.setOnCloseRequest escenario (fn [_] (.stop temporizador)))
        (.show escenario)
        (.start temporizador)))))