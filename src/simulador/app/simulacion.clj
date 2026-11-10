(ns simulador.app.simulacion
  (:require [simulador.app.estado :as estado]))

(def ^:private periodo-ms
  "Objetivo: ~60 pasos por segundo."
  16)

(defonce ^:private hilo (atom nil))

(defn- bucle
  "Corre hasta que el hilo es interrumpido. Si no hay pausa, avanza un
   paso y espera lo que falte para completar el período."
  []
  (try
    (while (not (.isInterrupted (Thread/currentThread)))
      (let [inicio (System/currentTimeMillis)]
        (when-not @estado/en-pausa?
          (estado/avanzar-un-paso!))
        (let [usado (- (System/currentTimeMillis) inicio)]
          (when (< usado periodo-ms)
            (Thread/sleep (long (- periodo-ms usado)))))))
    (catch InterruptedException _
      nil)))

(defn iniciar!
  "Lanza el hilo de simulación (si no estaba corriendo)."
  []
  (when (nil? @hilo)
    (let [h (Thread. ^Runnable bucle "hilo-simulacion")]
      (.setDaemon h true)
      (reset! hilo h)
      (.start h))))

(defn detener!
  "Detiene el hilo de simulación."
  []
  (when-let [h @hilo]
    (.interrupt ^Thread h)
    (reset! hilo nil)))
