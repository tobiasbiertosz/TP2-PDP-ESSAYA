(ns simulador.ui.entrada
  (:require [simulador.app.estado :as estado]
            [simulador.ui.lienzo :as lienzo])
  (:import [javafx.scene.canvas Canvas]
           [javafx.scene.input MouseEvent]))

(defn- pixel->celda
  "Convierte una coordenada en píxeles a coordenada de celda."
  [pixel]
  (long (Math/floor (/ pixel lienzo/tamano-celda))))

(defn conectar-mouse!
  "Conecta los eventos del mouse del lienzo con el pincel.
   Al presionar pinta un disco; al arrastrar pinta el segmento desde la
   posición anterior, así no quedan huecos."
  [^Canvas lienzo-fx]
  (let [anterior (atom nil)] ; [x y] de la última celda, o nil
    (.setOnMousePressed
      lienzo-fx
      (fn [^MouseEvent e]
        (let [x (pixel->celda (.getX e))
              y (pixel->celda (.getY e))]
          (reset! anterior [x y])
          (estado/pintar-segmento! x y x y))))
    (.setOnMouseDragged
      lienzo-fx
      (fn [^MouseEvent e]
        (let [x (pixel->celda (.getX e))
              y (pixel->celda (.getY e))
              [x0 y0] (or @anterior [x y])]
          (reset! anterior [x y])
          (estado/pintar-segmento! x0 y0 x y))))
    (.setOnMouseReleased
      lienzo-fx
      (fn [_] (reset! anterior nil)))))
