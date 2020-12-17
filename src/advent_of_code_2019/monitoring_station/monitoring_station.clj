(ns advent-of-code-2019.monitoring-station.monitoring-station
  (:gen-class)
  (:require
    [clojure.math.numeric-tower :as math]))

(def asteroid \#)

(defn create-coord [x y]
  {:x x :y y})

(defn loc [coord grid]
  (nth (nth grid (:y coord)) (:x coord)))

(defn row-coords [row-idx, row]
  (map-indexed (fn [idx, _] {:x idx :y row-idx}) row))

(defn all-coords [grid]
  (reduce concat (map-indexed (fn [row-idx row] (row-coords row-idx row)) grid)))

(defn step [from-coord x-step y-step]
  (create-coord
    (+ x-step (:x from-coord))
    (+ y-step (:y from-coord))))

(defn x-step [from-coord to-coord]
  (let [x-diff (- (:x to-coord) (:x from-coord))
        y-diff (- (:y to-coord) (:y from-coord))]
    (cond
      (zero? y-diff) (if (pos? x-diff) 1 -1)
      (zero? x-diff) 0
      :else (let [angle (/ y-diff x-diff)
                  numerator-abs (math/abs (if (instance? Long angle) 1 (denominator angle)))]
              (if (pos? x-diff) numerator-abs (* -1 numerator-abs))))))

(defn y-step [from-coord to-coord]
  (let [x-diff (- (:x to-coord) (:x from-coord))
        y-diff (- (:y to-coord) (:y from-coord))]
    (cond
      (zero? x-diff) (if (pos? y-diff) 1 -1)
      (zero? y-diff) 0
      :else (let [angle (/ y-diff x-diff)
                  denominator-abs (math/abs (if (instance? Long angle) angle (numerator angle)))]
              (if (pos? y-diff) denominator-abs (* -1 denominator-abs))))))

(defn first-asteroid-between [from-coord to-coord x-step y-step grid]
  (loop [next-coord (step from-coord x-step y-step)]
    (assert (loc next-coord grid))
    (cond
      (= next-coord to-coord) nil
      (= asteroid (loc next-coord grid)) next-coord
      :else (recur (step next-coord x-step y-step)))))

(defn asteroids-between? [from-coord to-coord x-step y-step grid]
  (some? (first-asteroid-between from-coord to-coord x-step y-step grid)))

(defn visible? [from-coord coord-in-q grid]
  (let [x-step (x-step from-coord coord-in-q)
        y-step (y-step from-coord coord-in-q)]
    (not (asteroids-between? from-coord coord-in-q x-step y-step grid))))

(defn visible-asteroid-coords [coord grid]
  (let [all-coords (all-coords grid)
        other-coords (filter #(not= coord %) all-coords)
        other-asteroids (filter #(= asteroid (loc % grid)) other-coords)
        visible-asteroids (filter #(visible? coord % grid) other-asteroids)]
    visible-asteroids))

(defn visible-asteroid-count [coord grid]
  {:coord coord :count (count (visible-asteroid-coords coord grid))})

(defn best-location-w-count [grid]
  (let [all-coords (all-coords grid)
        all-asteroids (filter #(= asteroid (loc % grid)) all-coords)
        visible-froms (map #(visible-asteroid-count % grid) all-asteroids)]
    (apply max-key :count visible-froms)))

(defn count-visible-from-best-loc [grid]
  (let [best-loc (best-location-w-count grid)]
    (:count best-loc)))

(defn best-location [grid]
  (let [best-loc (best-location-w-count grid)]
    (:coord best-loc)))