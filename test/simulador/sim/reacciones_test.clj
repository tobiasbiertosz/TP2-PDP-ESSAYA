(ns simulador.sim.reacciones-test
  (:require [clojure.test :refer [deftest is testing]]
            [simulador.sim.reacciones :as r]))

(deftest buscar-test
  (testing "el orden de los materiales no importa"
    (is (= (r/buscar :fuego :agua) (r/buscar :agua :fuego)))
    (is (some? (r/buscar :lava :madera))))
  (testing "pares sin reacción"
    (is (nil? (r/buscar :arena :agua)))
    (is (nil? (r/buscar :aire :aire)))
    (is (nil? (r/buscar :vapor :lava)))))

(deftest periodos-test
  (is (= 2  (r/periodo (r/buscar :fuego :agua))))
  (is (= 2  (r/periodo (r/buscar :agua :lava))))
  (is (= 20 (r/periodo (r/buscar :arena :lava))))
  (is (= 20 (r/periodo (r/buscar :fuego :madera))))
  (is (= 5  (r/periodo (r/buscar :lava :madera)))))

(deftest resultados-test
  (let [fuego-agua (r/buscar :fuego :agua)]
    (is (= :aire  (r/resultado fuego-agua :fuego)))
    (is (= :vapor (r/resultado fuego-agua :agua))))
  (let [agua-lava (r/buscar :agua :lava)]
    (is (= :vapor  (r/resultado agua-lava :agua)))
    (is (= :piedra (r/resultado agua-lava :lava))))
  (let [arena-lava (r/buscar :arena :lava)]
    (is (= :vidrio (r/resultado arena-lava :arena)))
    (is (= :lava   (r/resultado arena-lava :lava))))
  (let [fuego-madera (r/buscar :fuego :madera)]
    (is (= :fuego (r/resultado fuego-madera :fuego)))
    (is (= :fuego (r/resultado fuego-madera :madera))))
  (let [lava-madera (r/buscar :lava :madera)]
    (is (= :lava  (r/resultado lava-madera :lava)))
    (is (= :fuego (r/resultado lava-madera :madera)))))