(ns advent-of-code-2019.core
  (:gen-class)
  (:require [advent-of-code-2019.rocket-equation :as re]
            [advent-of-code-2019.intcode :as intcode]
            [advent-of-code-2019.input-handling :as input]))

(defn day1pt1 []
  (re/fuel-for-modules (input/day1-num-seq)))

(defn day1pt2 []
  (re/total-fuel (input/day1-num-seq)))

(defn day2pt1 []
  ((intcode/execute (input/day2-num-vec)) 0))

(defn day2pt2 []
  (let [inputs (intcode/find-inputs 19690720 (input/day2-unmodified-num-vec))
        noun (first inputs)
        verb (second inputs)]
    (+ (* 100 noun) verb)))
