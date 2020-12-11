(ns advent-of-code-2019.n-body-problem.energy
  (:gen-class)
  (:require
    [advent-of-code-2019.n-body-problem.time-steps :as time-steps]))

(defn energy [coords]
  (reduce #(+ (Math/abs %1) (Math/abs %2)) coords))

(defn potential-energy [moon]
  (energy (:pos moon)))

(defn kenetic-energy [moon]
  (energy (:vel moon)))

(defn moon-energy [moon]
  (* (potential-energy moon) (kenetic-energy moon)))

(defn total-energy [moons steps]
  (reduce + (map moon-energy (time-steps/apply-time moons steps))))
