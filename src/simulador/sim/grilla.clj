(ns simulador.sim.grilla
  (:require [simulador.sim.materiales :as m]))

(defn grilla-vacia
  "Grilla de ancho x alto llena de aire."
  [ancho alto]
  {:ancho  ancho
   :alto   alto
   :celdas (vec (repeat (* ancho alto) :aire))})

(defn dentro?
  "¿Está la celda (x, y) dentro de la grilla?"
  [{:keys [ancho alto]} x y]
  (and (<= 0 x) (< x ancho)
       (<= 0 y) (< y alto)))

(defn- indice
  "Índice en el vector plano de la celda (x, y)."
  [{:keys [ancho]} x y]
  (+ (* y ancho) x))

(defn obtener-celda
  "Material de la celda (x, y). Si está fuera de la grilla, devuelve nil."
  [grilla x y]
  (when (dentro? grilla x y)
    (nth (:celdas grilla) (indice grilla x y))))

(defn poner-celda
  "Devuelve una grilla nueva con la celda (x, y) reemplazada por material.
   Si (x, y) está fuera de la grilla, devuelve la grilla sin cambios."
  [grilla x y material]
  (if (dentro? grilla x y)
    (assoc-in grilla [:celdas (indice grilla x y)] material)
    grilla))

(defn contar-particulas
  "Cantidad de celdas que no son aire."
  [grilla]
  (count (remove m/vacio? (:celdas grilla))))

(defn desde-textos
  "Arma una grilla desde un vector de textos, usando los símbolos del
   enunciado. Pensada para los tests.
   (desde-textos [\".a.\" \"###\"]) -> grilla de 3x2"
  [filas]
  (let [alto  (count filas)
        ancho (count (first filas))]
    {:ancho  ancho
     :alto   alto
     :celdas (vec (map m/simbolo->material (apply str filas)))}))

(defn a-textos
  "Inversa de desde-textos: devuelve un vector de textos."
  [{:keys [ancho celdas]}]
  (->> celdas
       (map m/simbolo)
       (partition ancho)
       (mapv #(apply str %))))