(ns advent-of-code-2019.monitoring-station.monitoring-station
  (:gen-class)
  (:require
    [clojure.math.numeric-tower :as math]))

(def asteroid \#)

(defn create-coord [x y]
  {:x x :y y})

(defn get-row [coord grid]
  (vec (nth grid (:y coord))))

(defn loc [coord grid]
  (nth (get-row coord grid) (:x coord)))

(defn asteroid? [coord grid]
  (= asteroid (loc coord grid)))

(defn row-coords [row-idx, row]
  (map-indexed (fn [idx, _] {:x idx :y row-idx}) row))

(defn all-coords [grid]
  (reduce concat (map-indexed (fn [row-idx row] (row-coords row-idx row)) grid)))

(defn remove-asteroid-row [coord grid]
  (assoc (get-row coord grid) (:x coord) \.))

(defn maybe-remove-asteroid [coord grid]
  (if (nil? coord)
    grid
    (assoc (vec grid) (:y coord) (remove-asteroid-row coord grid))))

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
      (asteroid? next-coord grid) next-coord
      :else (recur (step next-coord x-step y-step)))))

(defn asteroids-between? [from-coord to-coord x-step y-step grid]
  (some? (first-asteroid-between from-coord to-coord x-step y-step grid)))

(defn visible? [from-coord coord-in-q grid]
  (let [x-step (x-step from-coord coord-in-q)
        y-step (y-step from-coord coord-in-q)]
    (not (asteroids-between? from-coord coord-in-q x-step y-step grid))))

(defn first-asteroid-on-path [from-coord to-coord grid]
  (let [x-step (x-step from-coord to-coord)
        y-step (y-step from-coord to-coord)
        first-a (first-asteroid-between from-coord to-coord x-step y-step grid)
        to-coord-a (asteroid? to-coord grid)]
    (cond
      (some? first-a) first-a
      to-coord-a to-coord
      :else nil)))

(defn visible-asteroid-coords [coord grid]
  (let [all-coords (all-coords grid)
        other-coords (filter #(not= coord %) all-coords)
        other-asteroids (filter #(asteroid? % grid) other-coords)
        visible-asteroids (filter #(visible? coord % grid) other-asteroids)]
    visible-asteroids))

(defn visible-asteroid-count [coord grid]
  {:coord coord :count (count (visible-asteroid-coords coord grid))})

(defn best-location-w-count [grid]
  (let [all-coords (all-coords grid)
        all-asteroids (filter #(asteroid? % grid) all-coords)
        visible-froms (map #(visible-asteroid-count % grid) all-asteroids)]
    (apply max-key :count visible-froms)))

(defn count-visible-from-best-loc [grid]
  (let [best-loc (best-location-w-count grid)]
    (:count best-loc)))

(defn best-location [grid]
  (let [best-loc (best-location-w-count grid)]
    (:coord best-loc)))

(defn border [grid x]
  (let [row-size (count (first grid))
        right-edge-idx (- row-size 1)
        col-size (count grid)
        btm-idx (- col-size 1)
        start-top (map #(create-coord % 0) (range x row-size))
        right-edge (map #(create-coord right-edge-idx %) (range 0 col-size))
        bottom (map #(create-coord % btm-idx) (range right-edge-idx -1 -1))
        left-edge (map #(create-coord 0 %) (range btm-idx -1 -1))
        end-top (map #(create-coord % 0) (range 0 x))]
    (distinct (concat start-top right-edge bottom left-edge end-top))))

(defn representative? [vector-coord fixed-point all-coords]
  (let [x-step (x-step fixed-point vector-coord)
        y-step (y-step fixed-point vector-coord)
        hypothetical-x (+ (:x vector-coord) x-step)
        hypothetical-y (+ (:y vector-coord) y-step)
        better-rep (create-coord hypothetical-x hypothetical-y)]
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

(defn v-is-clockwise-of [ax ay bx by quad]
  (let [a-slope (if (= 0 ax) (/ ay 0.0) (/ ay ax))
        b-slope (if (= 0 bx) (/ by 0.0) (/ by bx))]
    (condp = quad
      1 (> a-slope b-slope)
      2 (> a-slope b-slope)
      3 (> a-slope b-slope)
      4 (> a-slope b-slope))))

(defn is-clockwise-of [a b fixed]
  (let [a-x-step (x-step fixed a)
        a-y-step (y-step fixed a)
        a-quad (quadrant a-x-step a-y-step)
        b-x-step (x-step fixed b)
        b-y-step (y-step fixed b)
        b-quad (quadrant b-x-step b-y-step)]
    (if (not= a-quad b-quad)
      (> a-quad b-quad)
      (v-is-clockwise-of a-x-step a-y-step b-x-step b-y-step a-quad))))


(defn sort-clockwise [coords fixed]
  (sort #(is-clockwise-of %2 %1 fixed) coords))

(defn clockwise-vectors [coord grid]
  (let [all-coords (all-coords grid)
        all-coords (set (remove #(= coord %) all-coords))
        vectors (remove-duplicate-vectors coord all-coords)]
    (sort-clockwise vectors coord)))

(defn fire-laser [laser edge-coord grid]
  (first-asteroid-on-path laser edge-coord grid))

(defn vaporize-asteroids
  ([grid n] (vaporize-asteroids grid n (best-location grid)))
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
                next-grid (maybe-remove-asteroid asteroid-hit-coord curr-grid)]
            (recur next-grid (inc laser-steps) next-vaped)))))))

