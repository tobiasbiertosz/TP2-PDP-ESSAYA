(ns simulador.sim.pintar-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.grilla :as g]
            [simulador.sim.pintar :as pi]))

(deftest disco-test
  (testing "cantidad de celdas según el radio"
    (is (= 1  (count (pi/celdas-del-disco 5 5 0))))
    (is (= 5  (count (pi/celdas-del-disco 5 5 1))))
    (is (= 13 (count (pi/celdas-del-disco 5 5 2)))))
  (testing "el radio 1 es una cruz"
    (is (= #{[5 5] [4 5] [6 5] [5 4] [5 6]}
           (set (pi/celdas-del-disco 5 5 1))))))

(deftest segmento-test
  (testing "un solo punto"
    (is (= [[2 3]] (pi/puntos-del-segmento 2 3 2 3))))
  (testing "extremos incluidos"
    (is (= [[0 0] [1 0] [2 0] [3 0]] (pi/puntos-del-segmento 0 0 3 0))))
  (testing "sin huecos: puntos consecutivos son vecinos"
    (let [pts (pi/puntos-del-segmento 0 0 9 3)]
      (is (= [0 0] (first pts)))
      (is (= [9 3] (last pts)))
      (is (every? (fn [[[xa ya] [xb yb]]]
                    (and (<= (Math/abs (- xb xa)) 1)
                         (<= (Math/abs (- yb ya)) 1)))
                  (partition 2 1 pts))))))

(deftest pintar-disco-test
  (let [vacia (g/grilla-vacia 3 3)]
    (testing "disco de radio 1 en el centro"
      (is (= [".a." "aaa" ".a."]
             (g/a-textos (pi/pintar-disco vacia 1 1 1 :arena)))))
    (testing "las celdas fuera de la grilla se ignoran"
      (is (= ["aa." "a.." "..."]
             (g/a-textos (pi/pintar-disco vacia 0 0 1 :arena)))))
    (testing "centro totalmente fuera de la grilla, sin error"
      (is (= vacia (pi/pintar-disco vacia 50 50 1 :arena))))
    (testing "la grilla original no cambia"
      (pi/pintar-disco vacia 1 1 1 :arena)
      (is (= ["..." "..." "..."] (g/a-textos vacia))))))

(deftest pintar-reemplaza-test
  (let [gr (g/desde-textos ["ggg" "ggg"])]
    (is (= ["ggg" "g.g"] (g/a-textos (pi/pintar-disco gr 1 1 0 :aire))))
    (is (= ["ggg" "gpg"] (g/a-textos (pi/pintar-disco gr 1 1 0 :piedra))))))

(deftest pintar-segmento-test
  (let [vacia (g/grilla-vacia 5 3)]
    (testing "segmento horizontal"
      (is (= ["....." "aaaaa" "....."]
             (g/a-textos (pi/pintar-segmento vacia 0 1 4 1 0 :arena)))))
    (testing "segmento diagonal"
      (is (= ["a..." ".a.." "..a." "...a"]
             (g/a-textos (pi/pintar-segmento (g/grilla-vacia 4 4) 0 0 3 3 0 :arena)))))
    (testing "el sentido no importa"
      (is (= (pi/pintar-segmento vacia 0 0 4 2 1 :agua)
             (pi/pintar-segmento vacia 4 2 0 0 1 :agua))))
    (testing "segmento de un solo punto equivale a un disco"
      (is (= (pi/pintar-disco vacia 2 1 1 :agua)
             (pi/pintar-segmento vacia 2 1 2 1 1 :agua))))))