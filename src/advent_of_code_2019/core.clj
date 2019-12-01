(ns advent-of-code-2019.core
  (:gen-class)
  (:require [advent-of-code-2019.rocket-equation :as re]
            [advent-of-code-2019.input-handling :as input]))

(defn day1pt1 []
  (re/total-fuel-required (input/day1-num-seq)))
