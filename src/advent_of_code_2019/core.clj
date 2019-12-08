(ns advent-of-code-2019.core
  (:gen-class)
  (:require [advent-of-code-2019.rocket-equation :as re]
            [advent-of-code-2019.intcode :as intcode]
            [advent-of-code-2019.intcode-harness :as intcode-harness]
            [advent-of-code-2019.crossed-wires :as cw]
            [advent-of-code-2019.image-decoding :as decoding]            
            [advent-of-code-2019.input-handling :as input]))

(defn day1pt1 []
  (re/fuel-for-modules (input/day1-num-seq)))

(defn day1pt2 []
  (re/total-fuel (input/day1-num-seq)))

(defn day2pt1 []
  ((intcode/execute (input/day2-num-vec)) 0))

(defn day2pt2 []
  (let [inputs (intcode-harness/find-inputs 19690720 (input/day2-unmodified-num-vec))
        noun (first inputs)
        verb (second inputs)]
    (+ (* 100 noun) verb)))

(defn day3pt1 []
  (let [string-pair (input/day3-string-pair)] 
    (cw/closet-overlap-dist (first string-pair) (second string-pair))))

(defn day3pt2 []
  (let [string-pair (input/day3-string-pair)]
    (cw/shortest-overlap (first string-pair) (second string-pair))))

(defn day5pt1 []
  (intcode/diagnostic-code (input/day5-num-vec) [1]))

(defn day5pt2 []
  (intcode/diagnostic-code (input/day5-num-vec) [5]))

(defn day7pt1 []
  (intcode-harness/find-largest-output (input/day7-num-vec) [0 1 2 3 4]))

(defn day7pt2 []
  (intcode-harness/find-largest-loop-output (input/day7-num-vec) [5 6 7 8 9]))

(defn day8pt1 []
  (decoding/checksum (input/day8-num-vec) 6 25))

(defn day8pt2 []
  (decoding/decode-combined (input/day8-num-vec) 6 25))