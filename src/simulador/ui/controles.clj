(ns simulador.ui.controles
  (:require [simulador.app.estado :as estado]
            [simulador.sim.materiales :as m])
  (:import [javafx.geometry Insets]
           [javafx.scene.control Button Label RadioButton Slider ToggleGroup]
           [javafx.scene.layout VBox]))

(def ^:private nombres
  "Texto que se muestra en el selector para cada material."
  {:aire "aire" :pared "pared" :arena "arena" :agua "agua" :vapor "vapor"
   :lava "lava" :fuego "fuego" :madera "madera" :piedra "piedra"
   :vidrio "vidrio"})

(defn- crear-selector-material
  "Lista de RadioButtons, uno por material"
  []
  (let [grupo (ToggleGroup.)
        cajas (for [material m/orden-materiales]
                (let [boton (RadioButton. (nombres material))]
                  (.setToggleGroup boton grupo)
                  (.setSelected boton (= material @estado/material-elegido))
                  (.setOnAction boton
                                (fn [_] (reset! estado/material-elegido material)))
                  boton))]
    (doto (VBox. 4.0)
      (.. getChildren (add (Label. "Material")))
      (.. getChildren (addAll ^java.util.Collection (vec cajas))))))

(defn- crear-slider-radio []
  (let [slider (Slider. 1.0 12.0 (double @estado/radio-pincel))]
    (.setShowTickLabels slider true)
    (.setMajorTickUnit slider 11.0)
    (.setBlockIncrement slider 1.0)
    (.. slider valueProperty
        (addListener
          (reify javafx.beans.value.ChangeListener
            (changed [_ _ _ nuevo]
              (reset! estado/radio-pincel (long (Math/round (double nuevo))))))))
    (doto (VBox. 4.0)
      (.. getChildren (add (Label. "Radio")))
      (.. getChildren (add slider)))))

(defn crear-controles
  "Panel derecho completo. Devuelve un mapa con el panel y los labels de
   los indicadores, para que la ventana los actualice en cada frame."
  []
  (let [boton-pausa    (Button. "Pausa")
        boton-paso     (Button. "Paso")
        boton-limpiar  (Button. "Limpiar")
        etiqueta-paso  (Label. "paso: 0")
        etiqueta-part  (Label. "partículas: 0")]
    (.setOnAction boton-pausa
                  (fn [_]
                    (estado/alternar-pausa!)
                    (.setText boton-pausa
                              (if @estado/en-pausa? "Reanudar" "Pausa"))))
    (.setOnAction boton-paso
                  (fn [_]
                    ;; Solo tiene efecto con la simulación en pausa.
                    (when @estado/en-pausa?
                      (estado/avanzar-un-paso!))))
    (.setOnAction boton-limpiar (fn [_] (estado/limpiar!)))
    {:panel (doto (VBox. 12.0)
              (.setPadding (Insets. 10.0))
              (.. getChildren
                  (addAll ^java.util.Collection
                          [(crear-selector-material)
                           (crear-slider-radio)
                           boton-pausa boton-paso boton-limpiar
                           etiqueta-paso etiqueta-part])))
     :etiqueta-paso      etiqueta-paso
     :etiqueta-particulas etiqueta-part}))
