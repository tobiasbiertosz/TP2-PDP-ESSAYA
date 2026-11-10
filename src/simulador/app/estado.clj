(ns simulador.app.estado
  (:require [simulador.sim.grilla :as g]
            [simulador.sim.paso :as p]
            [simulador.sim.pintar :as pi]))

(def ancho-por-defecto 160)
(def alto-por-defecto 120)

;; --- Los átomos

(def mundo
  "El mundo de la simulación: {:grilla ... :paso ...}."
  (atom (p/crear-mundo (g/grilla-vacia ancho-por-defecto alto-por-defecto))))

(def en-pausa?
  "¿Está la simulación detenida?"
  (atom false))

(def material-elegido
  "Material con el que se pinta (keyword)."
  (atom :arena))

(def radio-pincel
  "Radio del pincel, en celdas."
  (atom 3))

;; --- Operaciones sobre el estado (cada una cambia un átomo) ---

(defn avanzar-un-paso!
  "Avanza el mundo exactamente un paso."
  []
  (swap! mundo p/avanzar))

(defn limpiar!
  "Reemplaza la grilla por una vacía. Conserva el número de paso."
  []
  (swap! mundo update :grilla
         (fn [{:keys [ancho alto]}] (g/grilla-vacia ancho alto))))

(defn pintar-segmento!
  "Pinta un segmento con el material y el radio elegidos."
  [x0 y0 x1 y1]
  (let [material @material-elegido
        radio    @radio-pincel]
    (swap! mundo update :grilla
           pi/pintar-segmento x0 y0 x1 y1 radio material)))

(defn alternar-pausa!
  "Pausa si estaba corriendo, reanuda si estaba en pausa."
  []
  (swap! en-pausa? not))
