(ns advent-of-code-2019.monitoring-station.monitoring-station
  (:gen-class)
  (:require
    [clojure.math.numeric-tower :as math]))

(def asteroid \#)

(defn create-coord [x y]
  {:x x :y y})

(defn get-grid [coord grid]
  (get (get grid (:y coord)) (:x coord)))

(defn step [from-coord x-step y-step]
  (create-coord
    (+ x-step (:x from-coord))
    (+ y-step (:y from-coord))))

(defn any-asteroids-between? [from-coord to-coord x-step y-step, grid]
  (loop [next-coord (step from-coord x-step y-step)]
    (assert (get-grid next-coord grid))
    (cond
      (= next-coord to-coord) false
      (= asteroid (get-grid next-coord grid)) true
      :else (recur (step next-coord x-step y-step)))))

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

(defn is-visible [from-coord coord-in-q grid]
  (let [x-step (x-step from-coord coord-in-q)
        y-step (y-step from-coord coord-in-q)]
    (not (any-asteroids-between? from-coord coord-in-q x-step y-step grid))))

(defn row-coords [row-idx, row]
  (map-indexed (fn [idx, _] {:x idx :y row-idx}) row))

(defn all-coords [grid]
  (reduce concat (map-indexed (fn [row-idx row] (row-coords row-idx row)) grid)))

(defn get-visible-asteroid-coords [coord grid]
  (let [all-coords (all-coords grid)
        other-coords (filter #(not= coord %) all-coords)
        other-asteroids (filter #(= asteroid (get-grid % grid)) other-coords)
        visible-asteroids (filter #(is-visible coord % grid) other-asteroids)]
    visible-asteroids))