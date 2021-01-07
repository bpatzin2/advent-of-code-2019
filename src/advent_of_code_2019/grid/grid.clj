(ns advent-of-code-2019.grid.grid
  (:require
    [clojure.math.numeric-tower :as math]))

(defn create-coord [x y]
  {:x x :y y})

(defn get-row [coord grid]
  (vec (nth grid (:y coord))))

(defn loc [coord grid]
  (nth (get-row coord grid) (:x coord)))

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
