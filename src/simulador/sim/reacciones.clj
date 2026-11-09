(ns simulador.sim.reacciones)

;; Tabla de reacciones .
;; Clave: un SET con los dos materiales (el orden no importa).
;; Valor: {:n período, :produce {material -> en qué se convierte}}

(def reacciones
  {#{:fuego :agua}  {:n 2  :produce {:fuego :aire,   :agua :vapor}}
   #{:agua :lava}   {:n 2  :produce {:agua :vapor,   :lava :piedra}}
   #{:arena :lava}  {:n 20 :produce {:arena :vidrio, :lava :lava}}
   #{:fuego :madera} {:n 20 :produce {:fuego :fuego, :madera :fuego}}
   #{:lava :madera} {:n 5  :produce {:lava :lava,    :madera :fuego}}})

(defn buscar
  "Devuelve la reacción entre los materiales a y b (en cualquier orden),
   o nil si no hay ninguna."
  [a b]
  (get reacciones (hash-set a b)))

(defn periodo
  "Período N de una reacción."
  [reaccion]
  (:n reaccion))

(defn resultado
  "En qué se convierte el material dado al participar en la reacción."
  [reaccion material]
  (get-in reaccion [:produce material]))