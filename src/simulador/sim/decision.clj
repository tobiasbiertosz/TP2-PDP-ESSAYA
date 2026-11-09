(ns simulador.sim.decision)

(defn fase
  "Fase de un bloque en un paso dado.
   paso: número de paso. bx, by: índice del bloque."
  [paso bx by]
  (+ (quot paso 4)
     (* 3 bx)
     (* 5 by)))

(defn se-cumple?
  "¿Se cumple la decisión de período n, con número de decisión k,
   para un bloque con esa fase?"
  [fase k n]
  (zero? (mod (+ fase (* 7 k)) n)))