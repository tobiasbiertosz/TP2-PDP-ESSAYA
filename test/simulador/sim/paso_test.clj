(ns simulador.sim.paso-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.grilla :as g]
            [simulador.sim.paso :as p]))

(defn- estados
  "Los estados de la simulación en n = 0..n-max, como vectores de textos."
  [filas n-max]
  (->> (p/crear-mundo (g/desde-textos filas))
       (iterate p/avanzar)
       (take (inc n-max))
       (mapv #(g/a-textos (:grilla %)))))

(defn- verificar
  "Compara los estados reales con los esperados (uno por cada n)."
  [filas esperados]
  (is (= esperados (estados filas (dec (count esperados))))))

(deftest numero-de-paso
  (let [mundo (p/crear-mundo (g/grilla-vacia 4 4))]
    (is (= 0 (:paso mundo)))
    (is (= 3 (:paso (nth (iterate p/avanzar mundo) 3))))))

(deftest desplazamientos
  (is (= [[0 0] [1 1] [1 0] [0 1] [0 0] [1 1]]
         (map p/desplazamiento (range 6)))))

(deftest grano-de-arena-8x5
  (let [e0 ["...a...." "........" "........" "........" "########"]
        e1 ["........" "...a...." "........" "........" "########"]
        e2 ["........" "........" "...a...." "........" "########"]
        e3 ["........" "........" "........" "...a...." "########"]]
    (verificar e0 [e0 e1 e2 e3 e3 e3])))

(deftest fila-de-agua-4x4
  (let [e0 ["gggg" "...." "...." "####"]
        e1 ["...." "gggg" "...." "####"]
        e2 ["...." "g..g" ".gg." "####"]
        e4 ["...." "...." "gggg" "####"]]
    (verificar e0 [e0 e1 e2 e2 e4 e4])))

(deftest grilla-de-ancho-impar-5x4
  (let [e0 ["....a" "....." "....." "#####"]
        e3 ["....." "....a" "....." "#####"]]
    (verificar e0 [e0 e0 e0 e3 e3 e3])))

(deftest grilla-minima-3x3
  (verificar ["a.." "..." "###"]
             [["a.." "..." "###"]
              ["..." "a.." "###"]]))

(deftest arena-que-se-hunde-en-agua
  (let [e0 ["a." ".." "gg" "gg" "##"]
        e1 [".." "a." "gg" "gg" "##"]
        e4 [".." "g." "ag" "gg" "##"]
        e5 [".." "g." "gg" "ag" "##"]]
    (verificar e0 [e0 e1 e1 e1 e4 e5 e5])))

(deftest vapor-que-sube
  (let [e0 ["...." "...." "...." "^..."]
        e1 ["...." "...." ".^.." "...."]
        e2 ["...." ".^.." "...." "...."]
        e3 [".^.." "...." "...." "...."]
        e5 ["^..." "...." "...." "...."]]
    (verificar e0 [e0 e1 e2 e3 e3 e5 e5])))

(deftest gota-en-columna-aislada
  (let [e0 ["g..." "...." "...." "...." "####"]
        e1 ["...." ".g.." "...." "...." "####"]
        e2 ["...." "...." "..g." "...." "####"]
        e3 ["...." "...." "...." "..g." "####"]
        e7 ["...." "...." "...." ".g.." "####"]]
    (verificar e0 [e0 e1 e2 e3 e3 e3 e3 e7 e7])))

(deftest la-grilla-original-no-cambia
  (let [mundo (p/crear-mundo (g/desde-textos ["a." ".." "##"]))
        _     (p/avanzar mundo)]
    (is (= ["a." ".." "##"] (g/a-textos (:grilla mundo))))))
