(ns simulador.sim.materiales)

;; Tabla de materiales (sección 5.2 del enunciado).
;; Cada material es un MAPA: {clave valor, clave valor, ...}
;; Las claves que empiezan con ":" se llaman keywords.
;; Si una propiedad no aparece, se usa el valor por defecto (ver funciones abajo).

(def materiales
  {:aire   {:simbolo \. :color 0xFF000000 :tipo :vacio}
   :pared  {:simbolo \# :color 0xFF808080 :tipo :fijo}
   :piedra {:simbolo \p :color 0xFF555560 :tipo :fijo}
   :vidrio {:simbolo \v :color 0xFF9EDCE8 :tipo :fijo}
   :madera {:simbolo \m :color 0xFF8B5A2B :tipo :fijo}

   :arena  {:simbolo \a :color 0xFFE0C068 :tipo :polvo   :densidad 4}
   :agua   {:simbolo \g :color 0xFF2060E0 :tipo :liquido :densidad 2}
   :lava   {:simbolo \l :color 0xFFE85010 :tipo :liquido :densidad 3
            :periodo-movimiento 3}

   :vapor  {:simbolo \^ :color 0xFFC8C8D8 :tipo :gas :densidad -1
            :decaimiento {:destino :agua :n 100}}
   :fuego  {:simbolo \f :color 0xFFFFC020 :tipo :gas :densidad -2
            :decaimiento {:destino :aire :n 10}}})

;; Orden en que aparecen en el selector de la interfaz (sección 3).
(def orden-materiales
  [:aire :pared :arena :agua :vapor :lava :fuego :madera :piedra :vidrio])

;; --- Funciones para consultar propiedades ---

(defn tipo
  "Devuelve el tipo del material: :vacio, :fijo, :polvo, :liquido o :gas."
  [material]
  (get-in materiales [material :tipo]))

(defn densidad
  "Densidad del material. Si no la define, es 0."
  [material]
  (get-in materiales [material :densidad] 0))

(defn periodo-movimiento
  "Período de movimiento. Si no lo define, es 1."
  [material]
  (get-in materiales [material :periodo-movimiento] 1))

(defn decaimiento
  "Devuelve {:destino ... :n ...} o nil si el material no decae."
  [material]
  (get-in materiales [material :decaimiento]))

(defn color
  "Color ARGB del material."
  [material]
  (get-in materiales [material :color]))

(defn simbolo
  "Carácter que representa al material en los textos de la sección 6."
  [material]
  (get-in materiales [material :simbolo]))

(defn vacio?   [material] (= :vacio   (tipo material)))
(defn fijo?    [material] (= :fijo    (tipo material)))
(defn polvo?   [material] (= :polvo   (tipo material)))
(defn liquido? [material] (= :liquido (tipo material)))
(defn gas?     [material] (= :gas     (tipo material)))



(def simbolo->material
  "Mapa inverso: \\a -> :arena, \\. -> :aire, etc."
  (into {} (map (fn [[k v]] [(:simbolo v) k]) materiales)))