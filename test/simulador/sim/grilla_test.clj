(ns simulador.sim.grilla-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.grilla :as g]))

(deftest grilla-vacia-test
  (let [gr (g/grilla-vacia 4 3)]
    (is (= 4 (:ancho gr)))
    (is (= 3 (:alto gr)))
    (is (= 12 (count (:celdas gr))))
    (is (= 0 (g/contar-particulas gr)))))

(deftest obtener-poner-test
  (let [gr  (g/grilla-vacia 3 2)
        gr2 (g/poner-celda gr 1 0 :arena)]
    (testing "poner-celda escribe y obtener-celda lee"
      (is (= :arena (g/obtener-celda gr2 1 0)))
      (is (= :aire  (g/obtener-celda gr2 0 0))))
    (testing "la grilla original no se modifica"
      (is (= :aire (g/obtener-celda gr 1 0))))
    (testing "fuera de la grilla"
      (is (nil? (g/obtener-celda gr -1 0)))
      (is (nil? (g/obtener-celda gr 3 0)))
      (is (nil? (g/obtener-celda gr 0 2)))
      (is (= gr (g/poner-celda gr 9 9 :arena))))))

(deftest contar-particulas-test
  (is (= 3 (g/contar-particulas (g/desde-textos [".a." "#g."])))))

(deftest textos-ida-y-vuelta-test
  (let [filas [".a.#" "gl^f" "pvm."]]
    (is (= filas (g/a-textos (g/desde-textos filas))))))