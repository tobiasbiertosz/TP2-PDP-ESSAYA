(ns simulador.ui.lienzo
  (:require [simulador.sim.materiales :as m])
  (:import [javafx.scene.canvas Canvas GraphicsContext]
           [javafx.scene.paint Color]))

(def tamano-celda
  "Lado de cada celda en píxeles de pantalla."
  5)

(defn- argb->color
  "Convierte un entero ARGB (por ejemplo 0xFFE0C068) en un Color de JavaFX."
  [argb]
  (let [canal (fn [desplazamiento]
                (/ (bit-and (bit-shift-right argb desplazamiento) 0xFF) 255.0))]
    (Color/color (double (canal 16))    ;
                 (double (canal 8))     ;
                 (double (canal 0))     ;
                 (double (canal 24))))) ;

(def ^:private colores
  "Mapa material -> Color. Se calcula una sola vez, no en cada frame."
  (into {}
        (map (fn [material] [material (argb->color (m/color material))]))
        (keys m/materiales)))

(defn crear-lienzo
  "Crea un Canvas del tamaño justo para una grilla de ancho x alto celdas."
  [ancho alto]
  (Canvas. (double (* ancho tamano-celda))
           (double (* alto tamano-celda))))

(defn dibujar!
  "Dibuja la grilla completa en el lienzo. Tiene que llamarse desde el
   hilo de JavaFX."
  [^Canvas lienzo {:keys [ancho alto celdas]}]
  (let [^GraphicsContext gc (.getGraphicsContext2D lienzo)
        lado (double tamano-celda)]
    ;; Primero se pinta todo de color aire, y después solo lo que no es aire.
    (.setFill gc (colores :aire))
    (.fillRect gc 0.0 0.0 (.getWidth lienzo) (.getHeight lienzo))
    (doseq [y (range alto)
            x (range ancho)]
      (let [material (nth celdas (+ (* y ancho) x))]
        (when-not (= material :aire)
          (.setFill gc (colores material))
          (.fillRect gc (* x lado) (* y lado) lado lado))))))
