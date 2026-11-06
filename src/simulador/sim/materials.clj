(ns simulador.sim.materials)

;; Tabla de materiales (sección 5.2 del enunciado).
;; Cada material es un MAPA: {clave valor, clave valor, ...}
;; Las claves que empiezan con ":" se llaman keywords.
;; Si una propiedad no aparece, se usa el valor por defecto (ver funciones abajo).

(def materials
  {:aire   {:simbolo \. :color 0xFF000000 :tipo :vacio}
   :pared  {:simbolo \# :color 0xFF808080 :tipo :fijo}
   :piedra {:simbolo \p :color 0xFF555560 :tipo :fijo}
   :vidrio {:simbolo \v :color 0xFF9EDCE8 :tipo :fijo}
   :madera {:simbolo \m :color 0xFF8B5A2B :tipo :fijo}

   :arena  {:simbolo \a :color 0xFFE0C068 :tipo :polvo   :densidad 4}
   :agua   {:simbolo \g :color 0xFF2060E0 :tipo :liquido :densidad 2}
   :lava   {:simbolo \l :color 0xFFE85010 :tipo :liquido :densidad 3
            :periodo-mov 3}

   :vapor  {:simbolo \^ :color 0xFFC8C8D8 :tipo :gas :densidad -1
            :decaimiento {:destino :agua :n 100}}
   :fuego  {:simbolo \f :color 0xFFFFC020 :tipo :gas :densidad -2
            :decaimiento {:destino :aire :n 10}}})

;; Orden en que aparecen en el selector de la interfaz (sección 3).
(def material-order
  [:aire :pared :arena :agua :vapor :lava :fuego :madera :piedra :vidrio])

;; --- Funciones para consultar propiedades ---

(defn tipo
  "Devuelve el tipo del material: :vacio, :fijo, :polvo, :liquido o :gas."
  [mat]
  (get-in materials [mat :tipo]))

(defn densidad
  "Densidad del material. Si no la define, es 0."
  [mat]
  (get-in materials [mat :densidad] 0))

(defn periodo-mov
  "Período de movimiento. Si no lo define, es 1."
  [mat]
  (get-in materials [mat :periodo-mov] 1))

(defn decaimiento
  "Devuelve {:destino ... :n ...} o nil si el material no decae."
  [mat]
  (get-in materials [mat :decaimiento]))

(defn color [mat]
  (get-in materials [mat :color]))

(defn fijo?  [mat] (= :fijo   (tipo mat)))
(defn vacio? [mat] (= :vacio  (tipo mat)))
(defn gas?   [mat] (= :gas    (tipo mat)))
(defn liquido? [mat] (= :liquido (tipo mat)))

;; --- Útiles para los tests (permiten escribir grillas como texto) ---

(def simbolo->material
  "Mapa inverso: \\a -> :arena, \\. -> :aire, etc."
  (into {} (map (fn [[k v]] [(:simbolo v) k]) materials)))