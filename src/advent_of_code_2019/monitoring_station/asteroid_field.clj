(ns advent-of-code-2019.monitoring-station.asteroid-field
  (:gen-class)
  (:require
    [advent-of-code-2019.grid.grid :as g]
    [clojure.math.numeric-tower :as math]))

(def asteroid \#)

(defn asteroid? [coord grid]
  (= asteroid (g/loc coord grid)))

(defn remove-asteroid-row [coord grid]
  (assoc (g/get-row coord grid) (:x coord) \.))

(defn maybe-remove-asteroid [coord grid]
  (if (nil? coord)
    grid
    (assoc (vec grid) (:y coord) (remove-asteroid-row coord grid))))

(defn step [from-coord x-step y-step]
  (g/create-coord
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
    (assert (g/loc next-coord grid))
    (cond
      (= next-coord to-coord) nil
      (asteroid? next-coord grid) next-coord
      :else (recur (step next-coord x-step y-step)))))

(defn asteroids-between? [from-coord to-coord x-step y-step grid]
  (some? (first-asteroid-between from-coord to-coord x-step y-step grid)))
