(ns simulador.sim.bloque
  (:require [simulador.sim.materiales :as m]
            [simulador.sim.decision :as d]
            [simulador.sim.reacciones :as r]))

;; Un bloque es un vector de 4 materiales: [p0 p1 p2 p3]
;;   0 1
;;   2 3

(defn- intercambiar
  "Devuelve el bloque con los contenidos de las posiciones i y j intercambiados."
  [bloque i j]
  (assoc bloque i (nth bloque j)
                j (nth bloque i)))

;; ---------------------------------------------------------------
;; Etapa 1: decaimiento
;; ---------------------------------------------------------------

(defn- decaer
  "Recorre las posiciones 0 a 3. Si el material decae y se cumple la
   decisión (k = número de posición), la posición pasa a su destino."
  [bloque fase]
  (reduce
    (fn [actual i]
      (let [decae (m/decaimiento (nth actual i))]
        (if (and decae (d/se-cumple? fase i (:n decae)))
          (assoc actual i (:destino decae))
          actual)))
    bloque
    (range 4)))

;; ---------------------------------------------------------------
;; Etapa 2: reacciones
;; ---------------------------------------------------------------

;; El número de par q es la posición en este vector.
(def ^:private pares [[0 1] [2 3] [0 2] [1 3] [0 3] [1 2]])

(defn- reaccionar
  "Recorre los 6 pares en orden. Cada par usa los materiales ACTUALES
   (los que dejaron los pares anteriores). Decisión: k = 4 + q."
  [bloque fase]
  (reduce
    (fn [actual [q [i j]]]
      (let [a        (nth actual i)
            b        (nth actual j)
            reaccion (r/buscar a b)]
        (if (and reaccion
                 (d/se-cumple? fase (+ 4 q) (r/periodo reaccion)))
          (assoc actual i (r/resultado reaccion a)
                        j (r/resultado reaccion b))
          actual)))
    bloque
    (map-indexed vector pares)))

;; ---------------------------------------------------------------
;; Etapa 3: movimiento
;; ---------------------------------------------------------------

(defn- permisos
  "Vector de 4 booleanos: ¿puede moverse la posición i en este paso?
   Se calcula UNA vez, antes de mover nada. Pertenece a la posición."
  [bloque fase]
  (vec (map-indexed
         (fn [i material]
           (and (not (m/fijo? material))
                (d/se-cumple? fase (+ 10 i) (m/periodo-movimiento material))))
         bloque)))

(defn- hundir
  "Intercambia 'arriba' y 'abajo' si ambas tienen permiso y la de arriba
   es estrictamente más densa que la de abajo."
  [permiso bloque [arriba abajo]]
  (if (and (nth permiso arriba)
           (nth permiso abajo)
           (> (m/densidad (nth bloque arriba))
              (m/densidad (nth bloque abajo))))
    (intercambiar bloque arriba abajo)
    bloque))

(defn- se-desplaza?
  "Gas: se desplaza en cualquier fila. Líquido: solo en la fila inferior."
  [material fila-inferior?]
  (or (m/gas? material)
      (and fila-inferior? (m/liquido? material))))

(defn- deslizar
  "Intercambia las posiciones vecinas i y j (de la misma fila) si ambas
   tienen permiso, se cumple la decisión de período 2 con el k dado, y
   una contiene algo que se desplaza y la otra es de tipo vacío."
  [permiso fase k fila-inferior? bloque [i j]]
  (let [a (nth bloque i)
        b (nth bloque j)]
    (if (and (nth permiso i)
             (nth permiso j)
             (d/se-cumple? fase k 2)
             (or (and (se-desplaza? a fila-inferior?) (m/vacio? b))
                 (and (m/vacio? a) (se-desplaza? b fila-inferior?))))
      (intercambiar bloque i j)
      bloque)))

(defn- mover [bloque fase]
  (let [permiso    (permisos bloque fase)
        diagonales (if (d/se-cumple? fase 14 2)
                     [[0 3] [1 2]]
                     [[1 2] [0 3]])
        pares-caida (concat [[0 2] [1 3]] diagonales)
        tras-caer   (reduce (partial hundir permiso) bloque pares-caida)
        tras-fila-1 (deslizar permiso fase 15 false tras-caer [0 1])]
    (deslizar permiso fase 16 true tras-fila-1 [2 3])))

;; ---------------------------------------------------------------
;; Función pública: la regla completa del bloque
;; ---------------------------------------------------------------

(defn procesar-bloque
  "Dado un bloque (vector de 4 materiales) y su fase, devuelve el bloque
   nuevo aplicando decaimiento, reacciones y movimiento (sección 4.4)."
  [bloque fase]
  (-> bloque
      (decaer fase)
      (reaccionar fase)
      (mover fase)))