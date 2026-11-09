(ns simulador.sim.decision-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.decision :as d]))

(deftest fase-test
  (is (= 13 (d/fase 0 1 2)))
  (is (= 13 (d/fase 3 1 2)))   ; ⌊3/4⌋ = 0, igual que el paso 0
  (is (= 14 (d/fase 4 1 2))))  ; ⌊4/4⌋ = 1

(deftest se-cumple-test
  (testing "ejemplos del enunciado"
    (is (d/se-cumple? 3 1 10))        ; fuego decae
    (is (not (d/se-cumple? 0 1 10)))
    (is (d/se-cumple? 93 1 100))      ; vapor condensa
    (is (not (d/se-cumple? 92 1 100))))
  (testing "N = 1 siempre se cumple"
    (is (d/se-cumple? 12345 3 1)))
  (testing "ejemplo del bloque (1,2) con k=5, N=3"
    (is (= [true true true true false false false false]
           (map #(d/se-cumple? (d/fase % 1 2) 5 3) (range 8))))))