(ns simulador.sim.pintar
  (:require [simulador.sim.grilla :as g]))

(defn celdas-del-disco
  "Celdas [x y] del disco de centro (cx, cy) y radio dado (sección 3):
   (x - cx)^2 + (y - cy)^2 <= radio^2. Puede incluir celdas fuera de la grilla."
  [cx cy radio]
  (for [y (range (- cy radio) (+ cy radio 1))
        x (range (- cx radio) (+ cx radio 1))
        :when (<= (+ (* (- x cx) (- x cx))
                     (* (- y cy) (- y cy)))
                  (* radio radio))]
    [x y]))

(defn puntos-del-segmento
  "Puntos [x y] enteros a lo largo del segmento de (x0, y0) a (x1, y1),
   incluyendo ambos extremos. Dos puntos consecutivos siempre son vecinos
   (nunca quedan huecos)."
  [x0 y0 x1 y1]
  (let [dx (- x1 x0)
        dy (- y1 y0)
        n  (max (Math/abs (long dx)) (Math/abs (long dy)))]
    (if (zero? n)
      [[x0 y0]]
      (for [i (range (inc n))]
        [(Math/round (double (+ x0 (/ (* dx i) n))))
         (Math/round (double (+ y0 (/ (* dy i) n))))]))))

(defn pintar-celdas
  "Devuelve una grilla nueva con todas las celdas dadas ([x y] ...)
   reemplazadas por material. Las que caen fuera de la grilla se ignoran."
  [grilla celdas material]
  (reduce (fn [gr [x y]] (g/poner-celda gr x y material))
          grilla
          celdas))

(defn pintar-disco
  "Pinta un disco de radio dado alrededor de (cx, cy)."
  [grilla cx cy radio material]
  (pintar-celdas grilla (celdas-del-disco cx cy radio) material))

(defn pintar-segmento
  "Pinta un disco de radio dado en cada punto del segmento que une
   (x0, y0) con (x1, y1). Es lo que se usa al arrastrar el mouse."
  [grilla x0 y0 x1 y1 radio material]
  (let [celdas (into #{}
                     (mapcat (fn [[x y]] (celdas-del-disco x y radio))
                             (puntos-del-segmento x0 y0 x1 y1)))]
    (pintar-celdas grilla celdas material)))
