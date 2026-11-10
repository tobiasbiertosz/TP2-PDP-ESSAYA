(ns simulador.sim.bloque-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.materiales :as m]
            [simulador.sim.bloque :as bl]))

(defn- bloque-de
  "Convierte un texto de bloque. Ej: \"a...\" -> [:arena :aire :aire :aire]"
  [texto]
  (mapv m/simbolo->material texto))

(defn- verificar
  "Cada caso es [entrada fase salida-esperada], todos como texto."
  [casos]
  (doseq [[entrada fase salida] casos]
    (is (= (bloque-de salida)
           (bl/procesar-bloque (bloque-de entrada) fase))
        (str "entrada " entrada ", fase " fase ", esperado " salida))))

(deftest ejemplos-basicos
  (verificar
    [["a..." 0 "..a."]
     [".a.." 0 "...a"]
     ["a.#." 0 "..#a"]
     ["a.g." 0 "..ag"]
     ["..g." 0 "...g"]
     ["..g." 1 "..g."]
     ["..^." 0 "^..."]
     ["g.l." 0 "^.p."]
     ["g.l." 1 "..lg"]
     ["l..." 0 "l..."]
     ["l..." 1 "l..."]
     ["l..." 2 "...l"]
     ["f..." 0 "...."]]))

(deftest bloques-que-no-cambian
  (verificar
    [["...." 0 "...."]
     ["####" 0 "####"]
     ["#..." 0 "#..."]
     ["aa##" 0 "aa##"]
     ["a.##" 0 "a.##"]
     ["llpp" 0 "llpp"]
     ["gggg" 0 "gggg"]]))

(deftest caida
  (verificar
    [["a..#" 0 "..a#"]
     ["a.#." 0 "..#a"]
     ["a.#." 1 "..#a"]
     [".a#." 1 "..#a"]
     ["a.p." 0 "..pa"]
     ["vap." 0 "v.pa"]
     ["aa#." 0 "a.#a"]
     ["aa.." 0 "..aa"]
     ["gg.." 0 "..gg"]
     ["g..." 1 "..g."]
     ["g..." 0 "...g"]]))

(deftest hundimiento-por-densidad
  (verificar
    [["a.g." 1 "..ag"]
     ["aagg" 1 "ggaa"]
     ["a.l." 0 "..al"]
     ["l.g." 1 "l.g."]
     ["l.g." 5 "..lg"]]))

(deftest deslizamiento-horizontal
  (verificar
    [["g.##" 0 "g.##"]
     ["g.##" 1 "g.##"]
     ["##g." 0 "##.g"]
     ["##g." 1 "##g."]
     ["##gg" 0 "##gg"]
     ["..#g" 0 "..#g"]
     ["..l." 1 "..l."]
     ["..l." 0 "...l"]]))

(deftest gases
  (verificar
    [["..^^" 0 "^^.."]
     ["^^.." 0 ".^.g"]
     ["^.##" 1 ".^##"]
     ["..f." 0 "f..."]
     ["..f." 1 ".f.."]
     ["..^f" 1 "^f.."]
     ["..f^" 0 "f^.."]
     ["^f##" 1 "^f##"]
     ["ff##" 3 ".f##"]]))

(deftest reacciones
  (verificar
    [["fg##" 2 ".^##"]
     ["fg##" 3 "fg##"]
     ["gl##" 2 "^p##"]
     ["al##" 12 "vl##"]
     ["al##" 11 "al##"]
     ["lm##" 2 "lf##"]
     ["lm##" 1 "lm##"]
     ["fm##" 12 "ff##"]
     ["fm##" 11 "fm##"]
     ["##lm" 0 "##lf"]
     ["fgl#" 2 ".^l#"]]))

(deftest decaimiento
  (verificar
    [["#f##" 3 "#.##"]
     ["#f##" 0 "#f##"]
     ["#^##" 93 "#g##"]
     ["#^##" 92 "#^##"]]))