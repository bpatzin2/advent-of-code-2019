(ns advent-of-code-2019.monitoring-station.monitoring-station
  (:gen-class)
  (:require
    [advent-of-code-2019.grid.grid :as g]
    [advent-of-code-2019.monitoring-station.asteroid-field :as af]))

(defn visible-asteroid-coords [coord grid]
  (let [all-coords (g/all-coords grid)
        other-coords (filter #(not= coord %) all-coords)
        other-asteroids (filter #(af/asteroid? % grid) other-coords)
        visible-asteroids (filter #(af/asteroids-between? coord % grid) other-asteroids)]
    visible-asteroids))

(defn visible-asteroid-count [coord grid]
  {:coord coord :count (count (visible-asteroid-coords coord grid))})

(defn best-location-w-count [grid]
  (let [all-coords (g/all-coords grid)
        all-asteroids (filter #(af/asteroid? % grid) all-coords)
        visible-froms (map #(visible-asteroid-count % grid) all-asteroids)]
    (apply max-key :count visible-froms)))

(defn count-visible-from-best-loc [grid]
  (let [best-loc (best-location-w-count grid)]
    (:count best-loc)))

(defn best-location [grid]
  (let [best-loc (best-location-w-count grid)]
    (:coord best-loc)))