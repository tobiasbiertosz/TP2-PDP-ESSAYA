# TP2-PDP-ESSAYA

# Trabajo Práctico 2: Simulador de materiales (Falling Sand)

## Información académica

- **Universidad:** Universidad de Buenos Aires (UBA).
- **Facultad:** Facultad de Ingeniería (FIUBA).
- **Materia:** Algoritmos y Programación 3.

### Docentes y corrector

- **Docentes:** Diego Essaya y Santiago Maraggi.
- **Corrector:** Matías Rea.

### Integrantes

- Tobias Juan Biertosz.
- Selene Maidana Martínez.

## Descripción del proyecto

El proyecto consiste en desarrollar un simulador interactivo de materiales en una grilla de píxeles. Cada píxel contiene un material —como arena, agua, lava o vapor— que se comporta según reglas propias: la arena cae, el agua fluye, el vapor asciende y algunos materiales reaccionan al entrar en contacto. El usuario puede dibujar materiales con el mouse y observar cómo evoluciona la simulación.


## Requisitos
- JDK 21
- Leiningen

## Ejecución
    lein run
## Tests
    lein test

## Instrucciones de uso

- **Pintar:** click y arrastrar sobre la grilla.
- **Material:** selector de la derecha. "aire" funciona como goma de borrar.
- **Radio:** slider de 1 a 12 celdas.
- **Pausa / Reanudar:** detiene o reanuda; en pausa se puede seguir pintando.
- **Paso:** avanza exactamente un paso (solo en pausa).
- **Limpiar:** vacía la grilla.
- Indicadores: número de paso y cantidad de partículas.
