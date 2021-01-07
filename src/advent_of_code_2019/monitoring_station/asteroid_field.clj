(ns advent-of-code-2019.monitoring-station.asteroid-field
  (:gen-class)
  (:require
    [advent-of-code-2019.grid.grid :as g]))

(def asteroid \#)

(defn asteroid? [coord grid]
  (= asteroid (g/loc coord grid)))

(defn remove-asteroid-row [coord grid]
  (assoc (g/get-row coord grid) (:x coord) \.))

(defn maybe-remove-asteroid [coord grid]
  (if (nil? coord)
    grid
    (assoc (vec grid) (:y coord) (remove-asteroid-row coord grid))))

(defn first-asteroid-between [from-coord to-coord x-step y-step grid]
  (loop [next-coord (g/step from-coord x-step y-step)]
    (assert (g/loc next-coord grid))
    (cond
      (= next-coord to-coord) nil
      (asteroid? next-coord grid) next-coord
      :else (recur (g/step next-coord x-step y-step)))))

(defn first-asteroid-on-path [from-coord to-coord grid]
  (let [x-step (g/x-step from-coord to-coord)
        y-step (g/y-step from-coord to-coord)
        first-a (first-asteroid-between from-coord to-coord x-step y-step grid)
        to-coord-a (asteroid? to-coord grid)]
    (cond
      (some? first-a) first-a
      to-coord-a to-coord
      :else nil)))

(defn asteroids-between?
  ([from-coord coord-in-q grid]
   (let [x-step (g/x-step from-coord coord-in-q)
         y-step (g/y-step from-coord coord-in-q)]
     (not (asteroids-between? from-coord coord-in-q x-step y-step grid))))
  ([from-coord to-coord x-step y-step grid]
   (some? (first-asteroid-between from-coord to-coord x-step y-step grid))))