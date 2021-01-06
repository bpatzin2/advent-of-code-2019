(ns advent-of-code-2019.monitoring-station.laser
  (:require
    [advent-of-code-2019.grid.grid :as g]
    [advent-of-code-2019.monitoring-station.monitoring-station :as ms]))

(defn representative? [vector-coord fixed-point all-coords]
  (let [x-step (ms/x-step fixed-point vector-coord)
        y-step (ms/y-step fixed-point vector-coord)
        hypothetical-x (+ (:x vector-coord) x-step)
        hypothetical-y (+ (:y vector-coord) y-step)
        better-rep (g/create-coord hypothetical-x hypothetical-y)]
    (not (contains? all-coords better-rep))))

(defn remove-duplicate-vectors [coord all-coords]
  (loop [rest all-coords
         result []]
    (if (empty? rest)
      result
      (let [this-coord (first rest)
            is-rep (representative? this-coord coord all-coords)
            new-result (if is-rep (conj result this-coord) result)]
        (recur (drop 1 rest) new-result)))))

(defn quadrant [x-step y-step]
  (cond
    (and (>= x-step 0) (< y-step 0)) 1
    (and (>= x-step 0) (>= y-step 0)) 2
    (and (<= x-step 0) (> y-step 0)) 3
    :else 4))

(defn slope [x-step y-step]
  (if (= 0 x-step) (/ y-step 0.0) (/ y-step x-step)))

(defn is-clockwise-of [a b fixed]
  (let [a-x-step (ms/x-step fixed a)
        a-y-step (ms/y-step fixed a)
        a-quad (quadrant a-x-step a-y-step)
        b-x-step (ms/x-step fixed b)
        b-y-step (ms/y-step fixed b)
        b-quad (quadrant b-x-step b-y-step)]
    (if (not= a-quad b-quad)
      (> a-quad b-quad)
      (> (slope a-x-step a-y-step)
         (slope b-x-step b-y-step)))))

(defn sort-clockwise [coords fixed]
  (sort #(is-clockwise-of %2 %1 fixed) coords))

(defn clockwise-vectors [coord grid]
  (let [all-coords (g/all-coords grid)
        all-coords (set (remove #(= coord %) all-coords))
        vectors (remove-duplicate-vectors coord all-coords)]
    (sort-clockwise vectors coord)))

(defn first-asteroid-on-path [from-coord to-coord grid]
  (let [x-step (ms/x-step from-coord to-coord)
        y-step (ms/y-step from-coord to-coord)
        first-a (ms/first-asteroid-between from-coord to-coord x-step y-step grid)
        to-coord-a (ms/asteroid? to-coord grid)]
    (cond
      (some? first-a) first-a
      to-coord-a to-coord
      :else nil)))

(defn fire-laser [laser edge-coord grid]
  (first-asteroid-on-path laser edge-coord grid))

(defn vaporize-asteroids
  ([grid n] (vaporize-asteroids grid n (ms/best-location grid)))
  ([grid n laser-coord]
   (let [b-cycle (cycle (clockwise-vectors laser-coord grid))]
     (loop [curr-grid grid
            laser-steps 0
            vaped []]
       (if (= n (count vaped))
         (last vaped)
         (let [laser-edge (nth b-cycle laser-steps)
               asteroid-hit-coord (fire-laser laser-coord laser-edge curr-grid)
               next-vaped (if (some? asteroid-hit-coord) (conj vaped asteroid-hit-coord) vaped)
               next-grid (ms/maybe-remove-asteroid asteroid-hit-coord curr-grid)]
           (recur next-grid (inc laser-steps) next-vaped)))))))

