(ns advent-of-code-2019.n-body-problem.cycle-length
  (:gen-class)
  (:require
    [advent-of-code-2019.n-body-problem.time-steps :as time-steps]
    [clojure.math.numeric-tower :as math]))

(defn x-val [coords]
  (nth coords 0))

(defn y-val [coords]
  (nth coords 1))

(defn z-val [coords]
  (nth coords 2))

(def axis-selector {:x x-val :y y-val :z z-val})

(defn lcm [ns]
  (reduce math/lcm ns))

(defn get-axis-state [moons, axis]
  (let [axis-selector (axis axis-selector)
        all-pos (map axis-selector (map :pos moons))
        all-vels (map axis-selector (map :vel moons))]
    {:pos all-pos :vel all-vels}))

(defn axis-cycle-length [moons, axis]
  (loop [moons moons
         step-num 0
         prev-axis-states #{}]
    (let [axis-state (get-axis-state moons axis)]
      (if
        (contains? prev-axis-states axis-state)
        step-num
        (let [updated-prev-states (conj prev-axis-states axis-state)
              next-moons (time-steps/apply-time moons)]
          (recur next-moons (inc step-num) updated-prev-states))))))

(defn cycle-length [moons]
  (let [cycle-x (axis-cycle-length moons :x)
        cycle-y (axis-cycle-length moons :y)
        cycle-z (axis-cycle-length moons :z)]
    (lcm [cycle-x cycle-y cycle-z])))