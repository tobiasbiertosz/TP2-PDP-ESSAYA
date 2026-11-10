(ns simulador.sim.paso
  (:require [simulador.sim.decision :as d]
            [simulador.sim.bloque :as bl]))

;; Un MUNDO es {:grilla grilla, :paso numero-de-paso}

(defn crear-mundo
  "Mundo en el paso 0 a partir de una grilla."
  [grilla]
  {:grilla grilla :paso 0})

(defn desplazamiento
  "Desplazamiento [ox oy] de la partición según el número de paso (4.2)."
  [paso]
  (case (mod paso 4)
    0 [0 0]
    1 [1 1]
    2 [1 0]
    3 [0 1]))

(defn- cantidad-de-bloques
  "Cuántos bloques de 2 entran en 'largo' celdas, empezando en 'desplazamiento'."
  [largo desplazamiento]
  (max 0 (quot (- largo desplazamiento) 2)))

(defn- indices-del-bloque
  "Índices en el vector plano de las 4 celdas del bloque con esquina (x, y).
   Posiciones: 0 1 / 2 3."
  [ancho x y]
  (let [i (+ (* y ancho) x)]
    [i (inc i) (+ i ancho) (+ i ancho 1)]))

(defn- cambios-de-una-fila
  "Procesa todos los bloques de la fila de bloques 'by'.
   Devuelve un vector de pares [indice material-nuevo].
   Lee SOLO de 'celdas' (la grilla del paso anterior)."
  [celdas ancho paso ox oy cant-bx by]
  (let [y (+ oy (* 2 by))]
    (vec
      (mapcat
        (fn [bx]
          (let [x       (+ ox (* 2 bx))
                indices (indices-del-bloque ancho x y)
                bloque  (mapv celdas indices)
                nuevo   (bl/procesar-bloque bloque (d/fase paso bx by))]
            (map vector indices nuevo)))
        (range cant-bx)))))

(defn avanzar
  "Devuelve el mundo del paso siguiente (sección 4)."
  [{:keys [grilla paso]}]
  (let [{:keys [ancho alto celdas]} grilla
        [ox oy]  (desplazamiento paso)
        cant-bx  (cantidad-de-bloques ancho ox)
        cant-by  (cantidad-de-bloques alto oy)
        ;; Cada fila de bloques es una tarea independiente.
        cambios  (apply concat
                        (pmap #(cambios-de-una-fila celdas ancho paso ox oy cant-bx %)
                             (range cant-by)))
        nuevas   (reduce (fn [v [i material]] (assoc v i material))
                         celdas
                         cambios)]
    {:grilla (assoc grilla :celdas nuevas)
     :paso   (inc paso)}))
