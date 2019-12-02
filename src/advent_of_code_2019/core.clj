(ns advent-of-code-2019.core
  (:gen-class)
  (:require [advent-of-code-2019.rocket-equation :as re]
            [advent-of-code-2019.input-handling :as input]))

(defn day1pt1 []
  (re/fuel-for-modules (input/day1-num-seq)))

(defn day1pt2 []
  (re/total-fuel (input/day1-num-seq)))
