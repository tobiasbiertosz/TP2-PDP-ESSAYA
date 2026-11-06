(defproject simulador "0.1.0-SNAPSHOT"
  :description "TP2 - Simulador de materiales (falling sand)"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 ;; JavaFX no viene dentro del JDK, hay que traerlo.
                 ;; "win" porque estás en Windows.
                 [org.openjfx/javafx-base     "21.0.2" :classifier "win"]
                 [org.openjfx/javafx-graphics "21.0.2" :classifier "win"]
                 [org.openjfx/javafx-controls "21.0.2" :classifier "win"]]
  :main simulador.core
  :source-paths ["src"]
  :test-paths ["test"])