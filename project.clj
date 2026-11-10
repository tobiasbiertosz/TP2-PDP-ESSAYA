(defproject simulador "0.1.0-SNAPSHOT"
  :description "TP2 - Simulador de materiales (falling sand)"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [org.openjfx/javafx-base     "21.0.2"]
                 [org.openjfx/javafx-graphics "21.0.2"]
                 [org.openjfx/javafx-controls "21.0.2"]
                 [org.openjfx/javafx-base     "21.0.2" :classifier "win"]
                 [org.openjfx/javafx-graphics "21.0.2" :classifier "win"]
                 [org.openjfx/javafx-controls "21.0.2" :classifier "win"]
                 [org.openjfx/javafx-base     "21.0.2" :classifier "linux"]
                 [org.openjfx/javafx-graphics "21.0.2" :classifier "linux"]
                 [org.openjfx/javafx-controls "21.0.2" :classifier "linux"]
                 [org.openjfx/javafx-base     "21.0.2" :classifier "mac"]
                 [org.openjfx/javafx-graphics "21.0.2" :classifier "mac"]
                 [org.openjfx/javafx-controls "21.0.2" :classifier "mac"]]
  :main simulador.core
  :source-paths ["src"]
  :test-paths ["test"]
  :jvm-opts ["-Dprism.order=sw"])